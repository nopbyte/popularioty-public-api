package popularioty.api.rest.messages.input;
import javax.validation.constraints.NotNull;

public class MetaFeedbackCreate 
{
	
	@NotNull
	private boolean rating;
	
	private String title;
	
	private String text;
	

	public boolean isRating()
	{
		return rating;
	}

	public void setRating(boolean rating)
	{
		this.rating = rating;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}
	
	
	
}
