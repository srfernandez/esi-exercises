package es.uvigo.esei.dgss.exercises.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@IdClass(CommentId.class)
public class Comment {
	@Id
	@ManyToOne
	@JoinColumn(name = "userId")
	private User user;

	@Id
	@ManyToOne
	@JoinColumn(name = "postId")
	private Post post;

	@Id
	private Date date;

	private String content;

	public Comment() {
	}

	public Comment(User user, Post post) {
		this.user = user;
		this.post = post;
		this.date = new Date();
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getDate() {
		return date;
	}

	public User getUser() {
		return user;
	}

	public Post getPost() {
		return post;
	}
}