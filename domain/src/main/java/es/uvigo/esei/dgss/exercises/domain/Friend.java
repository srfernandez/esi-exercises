package es.uvigo.esei.dgss.exercises.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@IdClass(FriendId.class)
public class Friend {
	public Friend() {
	}

	public Friend(User friend1, User friend2) {
		this.friend1 = friend1;
		this.friend2 = friend2;
	}

	@Id
	@ManyToOne
	@JoinColumn(name = "friend1Id")
	private User friend1;

	@Id
	@ManyToOne
	@JoinColumn(name = "friend2Id")
	private User friend2;

	private Date date;

	private boolean accept;

	public User getFriend1() {
		return friend1;
	}

	public User getFriend2() {
		return friend2;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public boolean isAccept() {
		return accept;
	}

	public void setAccept(boolean accept) {
		this.accept = accept;
	}
}