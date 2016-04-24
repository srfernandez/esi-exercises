package es.uvigo.esei.dgss.exercises.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Link")
public class Link extends Post {
	private String url;

	public Link() {
		super();
	}

	public Link(User user, String url) {
		super(user);
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
