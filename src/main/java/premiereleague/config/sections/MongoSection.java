package premiereleague.config.sections;

import org.apache.commons.configuration.SubnodeConfiguration;

/**
 * MongoSection contains the information to configure MongoDB
 * @author Andrés Sánchez
 */
public class MongoSection
{
    public final int port;
    public final String host;
    public final String db;

    public MongoSection(SubnodeConfiguration mongo)
    {
        this.port = mongo.getInt("port");
        this.host = mongo.getString("host");
        this.db = mongo.getString("db");
    }
}
