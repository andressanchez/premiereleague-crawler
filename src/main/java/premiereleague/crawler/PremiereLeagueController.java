package premiereleague.crawler;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import premiereleague.config.Config;
import premiereleague.config.sections.CrawlerSection;

/**
 * Controller to handle our Premiere League Crawler
 * @author Andrés Sánchez
 */
public class PremiereLeagueController
{
    public static void main(String[] args) throws Exception
    {
        // User configuration
        CrawlerSection userConfig = Config.getCrawlerSection();

        // Folder where intermediate crawl data is stored.
        String crawlStorageFolder = "tmp";

        // Number of concurrent threads that should be initiated for crawling.
        int numberOfCrawlers = 1;

        // Crawler configuration.
        CrawlConfig crawlConfig = new CrawlConfig();

        // Set intermediate crawl data folder.
        crawlConfig.setCrawlStorageFolder(crawlStorageFolder);

        // We just want pages linked from the seed urls.
        crawlConfig.setMaxDepthOfCrawling(1);

        // Be polite! Wait 500 ms for the next request!
        crawlConfig.setPolitenessDelay(500);

        // Custom User-Agent.
        crawlConfig.setUserAgentString(Constants.USER_AGENT);

        // We must to set this cookie to be able to crawl data.
        crawlConfig.addCustomCookie("pllocale", "en_GB");

        // Up to 1 000 pages can be crawled.
        crawlConfig.setMaxPagesToFetch(1000);

        // Instantiate the controller for this crawl.
        PageFetcher pageFetcher = new PageFetcher(crawlConfig);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(crawlConfig, pageFetcher, robotstxtServer);

        org.apache.log4j.BasicConfigurator.configure();

        /*
         * For each crawl, we need to add some seed urls. These are the first
         * URLs that are fetched and then the crawler starts following links
         * which are found in these pages.
         */
        controller.addSeed(userConfig.seedURL);

        /*
         * Start the crawl. This is a blocking operation, meaning that your code
         * will reach the line after this only when crawling is finished.
         */
        controller.start(PremiereLeagueCrawler.class, numberOfCrawlers);

        // Send the shutdown request and then wait for finishing
        controller.shutdown();
        controller.waitUntilFinish();
    }
}
