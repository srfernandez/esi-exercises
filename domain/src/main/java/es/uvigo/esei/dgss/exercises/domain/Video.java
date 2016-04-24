package es.uvigo.esei.dgss.exercises.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Video")
public class Video extends Post {
	private int duration;

	public Video() {
		super();
	}

	public Video(User user, int duration) {
		super(user);
		this.duration = duration;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}
}