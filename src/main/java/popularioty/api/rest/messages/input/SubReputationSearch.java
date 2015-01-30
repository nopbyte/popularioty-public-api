package popularioty.api.rest.messages.input;
import javax.validation.constraints.NotNull;

public class SubReputationSearch 
{
	@NotNull
	private String entity_id;
	
	private String entity_type;
	
	private String sub_reputation_type;

	public String getEntity_id() {
		return entity_id;
	}

	public void setEntity_id(String entity_id) {
		this.entity_id = entity_id;
	}

	public String getSub_reputation_type() {
		return sub_reputation_type;
	}

	public void setSub_reputation_type(String sub_reputation_type) {
		this.sub_reputation_type = sub_reputation_type;
	}

	public String getEntity_type() {
		return entity_type;
	}

	public void setEntity_type(String entity_type) {
		this.entity_type = entity_type;
	}
	
	
	
}
