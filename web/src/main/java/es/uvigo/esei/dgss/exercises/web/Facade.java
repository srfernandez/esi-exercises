package es.uvigo.esei.dgss.exercises.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import es.uvigo.esei.dgss.exercises.domain.Comment;
import es.uvigo.esei.dgss.exercises.domain.Friend;
import es.uvigo.esei.dgss.exercises.domain.Photo;
import es.uvigo.esei.dgss.exercises.domain.Post;
import es.uvigo.esei.dgss.exercises.domain.User;

@Dependent
public class Facade {

	private EntityManager em;

	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	public User addUser(String login, String name, String password, byte[] picture) {
		User user = new User(login);

		user.setName(name);
		user.setPassword(password);
		user.setPicture(picture);

		em.persist(user);
		return user;
	}

	public Friend addFriendship(User user1, User user2) {
		Friend friendship = new Friend(user1, user2);
		friendship.setDate(new Date());

		em.persist(friendship);
		return friendship;
	}

	@SuppressWarnings("unchecked")
	public List<User> getFriends(User user) {
		List<User> friends = (List<User>) em.createQuery("SELECT f.friend2 FROM Friend f WHERE f.friend1 = :user")
				.setParameter("user", user).getResultList();

		List<User> befriendedBy = (List<User>) em.createQuery("SELECT f.friend1 FROM Friend f WHERE f.friend2 = :user")
				.setParameter("user", user).getResultList();

		friends.addAll(befriendedBy);
		return friends;
	}

	@SuppressWarnings("unchecked")
	private List<Post> getPosts(User user) {
		return (List<Post>) em.createQuery("SELECT p FROM Post p WHERE p.poster = :user").setParameter("user", user)
				.getResultList();
	}

	public List<Post> getFriendPosts(User user) {
		List<Post> posts = new ArrayList<Post>();

		getFriends(user).forEach(f -> posts.addAll(getPosts(f)));

		return posts;
	}

	@SuppressWarnings("unchecked")
	private List<Comment> getComments(User user) {
		return (List<Comment>) em.createQuery("SELECT c FROM Comment c WHERE c.user = :user").setParameter("user", user)
				.getResultList();
	}

	public List<Post> getPostsCommentedAfterDate(User user, Date date) {
		List<Post> posts = new ArrayList<Post>();

		getFriends(user).forEach(f -> getComments(f).forEach(c -> {
			if (c.getDate().after(date))
				posts.add(c.getPost());
		}));

		return posts;
	}

	@SuppressWarnings("unchecked")
	private List<Post> getLiked(User user) {
		return (List<Post>) em.createQuery("SELECT u.likes FROM User u WHERE u.login = :user")
				.setParameter("user", user.getLogin()).getResultList();
	}

	public List<User> getFriendsWhoLikedPost(User user, Post post) {
		List<User> users = new ArrayList<User>();

		getFriends(user).forEach(f -> {
			if (getLiked(f).contains(post))
				users.add(f);
		});

		return users;
	}

	public List<Photo> getPicturesLiked(User user) {
		List<Photo> photos = new ArrayList<Photo>();

		getLiked(user).forEach(p -> {
			if (p.getClass() == Photo.class)
				photos.add((Photo) p);
		});

		return photos;
	}

	public List<User> getPotentialFriends(User user) {
		List<User> friends = new ArrayList<User>();
		getFriends(user).forEach(u -> friends.addAll(getFriends(u)));

		return friends;
	}

	public Post addPost(User user, String content) {
		Photo post = new Photo(user, content);
		em.persist(post);
		return post;
	}

	public Comment addComment(User user, Post post, String content) {
		Comment comment = new Comment(user, post);
		comment.setContent(content);
		em.persist(comment);
		return comment;
	}

	public void addLike(User user, Post post) {
		List<Post> likedPosts = user.getLikes();
		likedPosts.add(post);
		user.setLikes(likedPosts);

		List<User> postLikers = post.getLikers();
		postLikers.add(user);
		post.setLikers(postLikers);
	}
}