package popularioty.api.rest.messages.input;

import java.util.Collection;

import javax.validation.constraints.NotNull;

public class Attribute {

	@NotNull
	private String reputation_type;
	@NotNull
	private Collection<String> values;
	
	
	public String getReputation_type() {
		return reputation_type;
	}
	public void setReputation_type(String reputation_type) {
		this.reputation_type = reputation_type;
	}
	public Collection<String> getValues() {
		return values;
	}
	public void setValues(Collection<String> values) {
		this.values = values;
	}
	
	
	
}
