package popularioty.api.rest.messages.input;
import javax.validation.constraints.NotNull;

public class FeedbackCreate 
{
	@NotNull
	private String title;
	
	@NotNull
	private String text;
	
	@NotNull
	private int rating;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}
    
	
}
