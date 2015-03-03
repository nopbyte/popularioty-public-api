package popularioty.api.rest.messages.input;
import java.util.List;

import javax.validation.constraints.NotNull;

public class SubReputationBatchSearch 
{
	@NotNull
	private List<Entity> entities;	

	@NotNull
	private List<Attribute> attributes;

	public List<Entity> getEntities() {
		return entities;
	}

	public void setEntities(List<Entity> entities) {
		this.entities = entities;
	}

	public List<Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}	
	
	
	
}
