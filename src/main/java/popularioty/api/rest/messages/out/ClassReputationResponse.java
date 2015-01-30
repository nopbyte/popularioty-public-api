package popularioty.api.rest.messages.out;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;



public class ClassReputationResponse {

	private String entity_type;
	private String entity_id;
	private Collection<Map> attributes;
	public ClassReputationResponse()
	{
		attributes = new LinkedList<>();
	}
	public String getEntity_type() {
		return entity_type;
	}
	public void setEntity_type(String entity_type) {
		this.entity_type = entity_type;
	}
	public String getEntity_id() {
		return entity_id;
	}
	public void setEntity_id(String entity_id) {
		this.entity_id = entity_id;
	}
	public Collection<Map> getAttributes() {
		return attributes;
	}
	public void setAttributes(Collection<Map> attributes) {
		this.attributes = attributes;
	}
	public void addAttribute(Map att)
	{
		attributes.add(att);
	}
	
	
	
	
}
