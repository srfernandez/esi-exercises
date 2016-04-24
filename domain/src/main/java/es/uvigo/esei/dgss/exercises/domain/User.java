package es.uvigo.esei.dgss.exercises.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class User {
	@Id
	private String login;
	private String name;
	private String password;
	private byte[] picture;

	@OneToMany(mappedBy = "user")
	private List<Comment> comments;

	@OneToMany(mappedBy = "poster")
	private List<Post> posts;

	@OneToMany(mappedBy = "friend1")
	private List<Friend> friends;

	@OneToMany(mappedBy = "friend2")
	private List<Friend> befriendedBy;

	@ManyToMany
	@JoinTable(name = "Likes", joinColumns = {
			@JoinColumn(name = "userId", referencedColumnName = "login") }, inverseJoinColumns = {
					@JoinColumn(name = "postId", referencedColumnName = "id") })
	private List<Post> likes;

	User() {
	}

	public User(String login) {
		this.login = login;
		this.likes = new ArrayList<Post>();
	}

	public String getLogin() {
		return login;
	}

	public String getPassword() {
		return password;
	}

	public byte[] getPicture() {
		return picture;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPicture(byte[] picture) {
		this.picture = picture;
	}

	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

	public List<Post> getLikes() {
		return likes;
	}

	public void setLikes(List<Post> likes) {
		this.likes = likes;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public List<Friend> getFriends() {
		return friends;
	}

	public void setFriends(List<Friend> friends) {
		this.friends = friends;
	}

	public List<Friend> getBefriendedBy() {
		return befriendedBy;
	}

	public void setBefriendedBy(List<Friend> befriendedBy) {
		this.befriendedBy = befriendedBy;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((login == null) ? 0 : login.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (login == null) {
			if (other.login != null)
				return false;
		} else if (!login.equals(other.login))
			return false;
		return true;
	}
}