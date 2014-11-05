package premiereleague.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import premiereleague.config.sections.*;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalINIConfiguration;

import java.io.File;

/**
 * Config reads the configuration file and create an object for each section.
 * @author Andrés Sánchez
 */
public class Config
{
    // Logger
    private static final Logger LOG = LoggerFactory.getLogger(Config.class);

    // Sections
    private CrawlerSection crawlerSection;
    private MongoSection mongoSection;

    // Instance
    private static Config instance;

    /**
     * Create a new instance (private due to Singleton pattern)
     */
    private Config() {
        try
        {
            // Get path for the configuration file
            String configFile = System.getProperty("config");
            if (configFile == null) configFile = getClass().getClassLoader().getResource("config/config.ini").getFile();
            LOG.info("Expecting config file in: " + configFile);

            // Load sections from the configuration file
            HierarchicalINIConfiguration iniConfig = new HierarchicalINIConfiguration(new File(configFile));
            this.crawlerSection = new CrawlerSection(iniConfig.getSection("crawler"));
            this.mongoSection = new MongoSection(iniConfig.getSection("mongo"));
        }
        catch (ConfigurationException e){
            LOG.error(e.getLocalizedMessage());
        }
    }

    /**
     * Get the 'crawler' section from the configuration file
     * @return Section with crawler options
     */
    public static CrawlerSection getCrawlerSection() {
        if (instance == null) instance = new Config();
        return instance.crawlerSection;
    }

    /**
     * Get the 'mongo' section from the configuration file
     * @return Section with MongoDB options
     */
    public static MongoSection getMongoSection() {
        if (instance == null) instance = new Config();
        return instance.mongoSection;
    }
}
