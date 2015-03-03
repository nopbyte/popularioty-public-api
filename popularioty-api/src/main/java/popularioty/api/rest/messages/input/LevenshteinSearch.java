package popularioty.api.rest.messages.input;
import javax.validation.constraints.NotNull;

public class LevenshteinSearch 
{
	@NotNull
	private String text;
	
	private Integer max_query_terms = -1;
	
	private Integer levenshtein = -1;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getMax_query_terms() {
		return max_query_terms;
	}

	public void setMax_query_terms(int max_query_terms) {
		this.max_query_terms = max_query_terms;
	}

	public int getLevenshtein() {
		return levenshtein;
	}

	public void setLevenshtein(int levenshtein) {
		this.levenshtein = levenshtein;
	}
	
	
}
