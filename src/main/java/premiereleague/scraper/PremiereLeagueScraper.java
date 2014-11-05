package premiereleague.scraper;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.dao.DAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import premiereleague.config.Config;
import premiereleague.config.sections.MongoSection;
import premiereleague.model.Match;

import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Scraper to parse data from the Premiere League website
 * @author Andrés Sánchez
 */
public class PremiereLeagueScraper
{
    // Logger
    private static final Logger LOG = LoggerFactory.getLogger(PremiereLeagueScraper.class);

    // MongoClient Object
    private MongoClient mongo;

    // Morphia Object
    private Morphia morphia;

    // Match DAO
    private DAO<Match, String> matchDAO;

    /**
     * Create a new instance of PremiereLeagueScraper
     */
    public PremiereLeagueScraper()
    {
        // Get mongo configuration
        MongoSection mongoConfig = Config.getMongoSection();

        // MongoClient
        try {
            mongo = new MongoClient(new ServerAddress(mongoConfig.host));
        } catch (UnknownHostException e) {
            LOG.error(e.getLocalizedMessage());
        }

        // Morphia
        morphia = new Morphia();
        morphia.map(Match.class);

        // Match DAO
        matchDAO = new BasicDAO<>(Match.class, mongo, morphia, mongoConfig.db);
    }

    /**
     * This method parses a Match page
     * @param page Fetched page
     */
    public void parseMatchPage(String page)
    {
        try
        {
            Document doc = Jsoup.parse(page, "UTF-8");

            // Home team
            String homeTeam = doc.select(".club.home").text();

            // Away team
            String awayTeam = doc.select(".club.away").text();

            // Score
            Integer homeScore = Integer.parseInt(doc.select(".homeScore").text());
            Integer awayScore = Integer.parseInt(doc.select(".awayScore").text());

            // Date (in timestamp format)
            Long timestamp = Long.parseLong(doc.select(".fixtureinfo > span").attr("timestamp"));

            // Match Object
            Match match = new Match(timestamp, homeTeam, awayTeam, homeScore, awayScore);

            // Goals
            Elements homeGoals = doc.select(".home.goals > ul > li");
            for (Element homeGoal : homeGoals) {
                String player = homeGoal.text().split(" \\(")[0];
                Pattern p = Pattern.compile("\\d+");
                Matcher m = p.matcher(homeGoal.text());
                while (m.find()) match.addHomeGoal(player, homeTeam, Integer.parseInt(m.group()));
            }

            Elements awayGoals = doc.select(".away.goals > ul > li");
            for (Element awayGoal : awayGoals) {
                String player = awayGoal.text().split(" \\(")[0];
                Pattern p = Pattern.compile("\\d+");
                Matcher m = p.matcher(awayGoal.text());
                int prevMinute = 0;
                while (m.find()) {
                    int currMinute = Integer.parseInt(m.group());
                    if (currMinute >= prevMinute) {
                        match.addAwayGoal(player, awayTeam, currMinute);
                        prevMinute = currMinute;
                    }
                }
            }

            // Statistic tables
            Elements tables = doc.select(".contentTable:not(.section)");
            Iterator<Element> tablesIt = tables.iterator();

            while (tablesIt.hasNext()) {
                Element table = tablesIt.next();

                Elements headers = table.select("thead").first().select("th:not(.ignore)");
                Elements valuesHome = table.select("tbody").first().select("tr").first().select("td");
                Elements valuesAway = table.select("tbody").first().select("tr").get(1).select("td");

                Iterator<Element> headerIt = headers.iterator();
                Iterator<Element> valuesHomeIt = valuesHome.iterator();
                Iterator<Element> valuesAwayIt = valuesAway.iterator();

                while (headerIt.hasNext() && valuesHomeIt.hasNext() && valuesAwayIt.hasNext()) {
                    String header = headerIt.next().text();
                    Integer valueHome = Integer.parseInt(valuesHomeIt.next().text());
                    Integer valueAway = Integer.parseInt(valuesAwayIt.next().text());
                    match.addHomeStatistic(header, valueHome);
                    match.addAwayStatistic(header, valueAway);
                    match.addTotalStatistic(header, valueHome + valueAway);
                }
            }

            // Save the match
            matchDAO.save(match);
            LOG.info("Match [ts=" + timestamp + ", homeTeam=" + homeTeam + ", awayTeam=" + awayTeam + "] successfully parsed!");
        }
        catch (NullPointerException e)
        {
            LOG.error(e.getLocalizedMessage());
        }
    }
}
