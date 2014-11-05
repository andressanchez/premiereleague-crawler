package premiereleague.config.sections;

import org.apache.commons.configuration.SubnodeConfiguration;

/**
 * CrawlerSection contains the information to configure the crawler
 * @author Andrés Sánchez
 */
public class CrawlerSection
{
    public final String seedURL;
    public final String pattern;

    public CrawlerSection(SubnodeConfiguration crawler)
    {
        this.seedURL = crawler.getString("seedURL");
        this.pattern = crawler.getString("pattern");
    }
}
