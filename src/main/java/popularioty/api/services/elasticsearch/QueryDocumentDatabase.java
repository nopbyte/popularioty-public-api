package popularioty.api.services.elasticsearch;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FuzzyLikeThisFieldQueryBuilder;
import org.elasticsearch.index.query.FuzzyLikeThisQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.node.Node;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static org.elasticsearch.node.NodeBuilder.*;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.core.io.ClassPathResource;

import popularioty.api.common.exception.ReputationAPIException;
import popularioty.api.common.exception.ReputationAPIException.Level;
import popularioty.api.commons.elasticsearch.ESCommons;
import popularioty.api.rest.client.HTTPClient2;
@Service
public class QueryDocumentDatabase implements DisposableBean{

	private static Logger LOG = LoggerFactory.getLogger(QueryDocumentDatabase.class);	
	private String prop_transport_host;
	private int prop_transport_port;
	private String prop_index_aggregated_rep;
	private String prop_index_feedback;
	private String prop_index_meta_feedback;
	private String prop_index_subreputation;
	
	private Node node;
	private Client client;
	public QueryDocumentDatabase()
	{
		// load properties file from classpath
        Properties properties = new Properties();
        ClassPathResource resource = new ClassPathResource("elasticsearch.properties");
        try {
            properties.load(resource.getInputStream());
            readProperties(properties);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOG.info("Attempting creation of ElasticSearch TransportClient with host:"+prop_transport_host+" and port "+prop_transport_port);
        
        //This should be changed if one wants to make the client part of the cluster...
        this.client = new TransportClient()
				.addTransportAddress(new InetSocketTransportAddress(prop_transport_host, prop_transport_port));
	}


	private void readProperties(Properties properties) {
		this.prop_transport_host= properties.getProperty("client.transport.host");
		this.prop_transport_port= Integer.parseInt(properties.getProperty("client.transport.port"));
		this.prop_index_aggregated_rep= properties.getProperty("index.aggregated");
		this.prop_index_feedback= properties.getProperty("index.feedback");
		this.prop_index_meta_feedback= properties.getProperty("index.metafeedback");
		this.prop_index_subreputation=properties.getProperty("index.subreputation");
	}
	

	public Map<String, Object> getFinalReputation(String entityId, String entityType) throws ReputationAPIException
	{
		
		QueryBuilder qb = QueryBuilders
                .boolQuery()
                .must(QueryBuilders.termQuery("entity_id", entityId));
        
		if(entityType !=null && !entityType.equals(""))
			qb=((BoolQueryBuilder) qb).must(QueryBuilders.termQuery("entity_type", entityType));
		
		
		SearchResponse scrollResp = client.prepareSearch()
				.setIndices(this.prop_index_aggregated_rep)
				.setQuery(qb)
				.setFrom(0)
				.setSize(1)
				.addSort("date",SortOrder.DESC)
				.execute().actionGet();
		
		for(SearchHit hit:scrollResp.getHits())
			return hit.getSource();
		
		throw new ReputationAPIException("No content found",null,LOG,"Reputation aggregated value not found for entity with id: "+entityId+" and type: "+entityType ,Level.DEBUG,204);
	}
	
	public Map<String, Object> getSingleClassReputation(String entityId, String entityType, String reputationType) throws ReputationAPIException
	{
		
		QueryBuilder qb = QueryBuilders
                .boolQuery()
                .must(QueryBuilders.termQuery("sub_reputation_type", reputationType))
                .must(QueryBuilders.termQuery("entity_id", entityId));
		
		if(entityType !=null && !entityType.equals(""))
			qb=((BoolQueryBuilder) qb).must(QueryBuilders.termQuery("entity_type", entityType));
		
		SearchResponse scrollResp = client.prepareSearch()
				.setIndices(this.prop_index_subreputation)
				.setQuery(qb)
				.setFrom(0)
				.setSize(1)
				.addSort("date",SortOrder.DESC)
				.execute().actionGet();
		
		for(SearchHit hit:scrollResp.getHits())
			return hit.getSource();
		
		throw new ReputationAPIException("No content found",null,LOG,"Reputation aggregated value not found for entity with id: "+entityId+" and type: "+entityType ,Level.DEBUG,204);
	}

	public List<Map<String, Object>> getFeedbackByEntity(String entityId, String entityType, int from, int size) throws ReputationAPIException
	{
		List<Map<String,Object>> ret = new LinkedList<>();
		Map<String, Object> tmp = null;
		QueryBuilder qb = QueryBuilders
                .boolQuery()
                .must(QueryBuilders.termQuery("entity_type", entityType))
                .must(QueryBuilders.termQuery("entity_id", entityId));
		SearchResponse scrollResp = client.prepareSearch()
				.setIndices(prop_index_feedback)
				.setQuery(qb)
				.setFrom(from)
				.setSize(size)
				.addSort("date",SortOrder.DESC)
				.execute().actionGet();
		
		for(SearchHit hit:scrollResp.getHits())
		{
			tmp = ESCommons.addId(hit,"feedback_id");
			ret.add(tmp);
		}
		
		if(ret.size()==0)
			throw new ReputationAPIException("No content found",null,LOG,"Feedback value not found for entity with id: "+entityId+" and type: "+entityType ,Level.DEBUG,204);
		return ret;
	}

	public List<Map<String, Object>> getMetaFeedbackByFeedback(String feedbackId, int from, int size) throws ReputationAPIException
	{
		List<Map<String,Object>> ret = new LinkedList<>();
		Map<String, Object> tmp = null;
		QueryBuilder qb = QueryBuilders
                .boolQuery()
                .must(QueryBuilders.termQuery("feedback_id", feedbackId));
		SearchResponse scrollResp = client.prepareSearch()
				.setIndices(prop_index_meta_feedback)
				.setQuery(qb)
				.setFrom(from)
				.setSize(size)
				.addSort("date",SortOrder.DESC)
				.execute().actionGet();
		
		for(SearchHit hit:scrollResp.getHits())
		{
			tmp = ESCommons.addId(hit,"meta_feedback_id");
			ret.add(tmp);
		}
		
		if(ret.size()==0)
			throw new ReputationAPIException("No content found",null,LOG,"Feedback value not found for feedback with id: "+feedbackId ,Level.DEBUG,204);
		return ret;
	}
	
	public List<Map<String, Object>> getFeedbackLevenshteinString(String text, int maxQuerySize, int levenshtein) throws ReputationAPIException
	{
		if(maxQuerySize>50)
			throw new ReputationAPIException("too many results requested",null,LOG,"too many results requested for fuzzy search of feedback" ,Level.DEBUG,422);
		
		List<Map<String,Object>> ret = new LinkedList<>();
		Map<String, Object> tmp = null;
		FuzzyLikeThisQueryBuilder qb = QueryBuilders
		.fuzzyLikeThisQuery("text", "title")
		.likeText(text)
		.fuzziness(ESCommons.getFuzziness(levenshtein));
        
		if(maxQuerySize>0)
			qb.maxQueryTerms(maxQuerySize); 
		
		
		SearchResponse scrollResp = client.prepareSearch()
				.setIndices(prop_index_feedback)
				.setQuery(qb)
				.addSort("date",SortOrder.DESC)
				.execute().actionGet();
		
		for(SearchHit hit:scrollResp.getHits())
		{
			tmp = ESCommons.addId(hit,"feedback_id");
			ret.add(tmp);
		}
		
		if(ret.size()==0)
			throw new ReputationAPIException("No content found",null,LOG,"Fuzzy search for text : "+text+" and levehnstein: "+levenshtein+" returned nothing...",Level.DEBUG,204);
		return ret;
		
	}
	@Override
	public void destroy() throws Exception {
		
		node.close();
		
	}
}
