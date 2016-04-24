package es.uvigo.esei.dgss.exercises.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Photo")
public class Photo extends Post {
	private String content;

	public Photo() {
		super();
	}

	public Photo(User user, String content) {
		super(user);
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
