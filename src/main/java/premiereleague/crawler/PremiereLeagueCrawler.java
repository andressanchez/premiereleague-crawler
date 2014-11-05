package premiereleague.crawler;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.url.WebURL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import premiereleague.config.Config;
import premiereleague.config.sections.CrawlerSection;
import premiereleague.scraper.PremiereLeagueScraper;

import java.io.IOException;

/**
 * Crawler to grab information from the Premiere League website
 * @author Andrés Sánchez
 */
public class PremiereLeagueCrawler extends WebCrawler
{
    // Scraper to parse a fetched page.
    private PremiereLeagueScraper scraper;

    // User configuration
    private CrawlerSection userConfig;

    // Logger
    private static final Logger LOG = LoggerFactory.getLogger(PremiereLeagueCrawler.class);

    /**
     * Create a new Premiere League Crawler
     */
    public PremiereLeagueCrawler() {
        super();
        scraper = new PremiereLeagueScraper();
        userConfig = Config.getCrawlerSection();
    }

    @Override
    public boolean shouldVisit(WebURL url)
    {
        String href = url.getURL().toLowerCase();
        return href.contains(userConfig.pattern);
    }

    @Override
    public void visit(Page page)
    {
        String url = page.getWebURL().getURL().toLowerCase();
        if (!url.contains(userConfig.pattern)) return;

        LOG.info("Visiting => " + url);

        try {
            String content = new String(page.getContentData(), "UTF-8");
            scraper.parseMatchPage(content);
        } catch (IOException e) {
            LOG.error(e.getLocalizedMessage());
        }
    }

}
