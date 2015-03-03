package popularioty.api.rest.messages.input;

import javax.validation.constraints.NotNull;

public class Entity {

	private String entity_type;
	@NotNull
	private String entity_id;
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
	
	
	
}
