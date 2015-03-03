package popularioty.document.client;

import org.elasticsearch.common.settings.ImmutableSettings;

public class DocumentDatabaseClient 
{
	public DocumentDatabaseClient()
	{
		/*
		 // ElasticSearch client
        Settings settings = ImmutableSettings.settingsBuilder()
			.put("http.enabled", "false")
			.put("transport.tcp.port", elasticSearchPorts)
			.put("discovery.zen.ping.multicast.enabled", "false")
			.put("discovery.zen.ping.unicast.hosts", elasticSearchServers).build();

        Node node = nodeBuilder().clusterName(config.getProperty("elastic_cluster")).client(true).settings(settings).node();
        elastic_client = node.client();

        // Buckets config
        soupdates = config.getProperty("updates_bucket");
        subscriptions = config.getProperty("subscriptions_bucket");
        */
	}
}
