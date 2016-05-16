package es.uvigo.esei.dgss.exercises.service.sample;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import es.uvigo.esei.dgss.exercises.domain.Friend;
import es.uvigo.esei.dgss.exercises.domain.Post;
import es.uvigo.esei.dgss.exercises.domain.User;

@Stateless
public class UserEJB {

	@PersistenceContext
	private EntityManager em;

	@EJB
	private PostEJB postejb;

	@EJB
	private StatisticsEJB statsejb;

	@EJB
	private EmailServiceEJB emailejb;

	@Resource
	private SessionContext ctx;

	private void checkUserAccess(String login, String message) {
		if (!ctx.getCallerPrincipal().getName().equals(login) && !ctx.isCallerInRole("admin")) {
			throw new SecurityException(message);
		}
	}

	public User findUserById(String login) {
		return em.find(User.class, login);
	}

	public List<User> getMatchingUsers(String pattern) {
		return this.em.createQuery("SELECT u FROM User u WHERE u.name LIKE :pattern", User.class)
				.setParameter("pattern", "%" + pattern + "%").getResultList();
	}

	public User addUser(String login, String name, String password, byte[] picture) {
		User user = new User(login);

		user.setName(name);
		user.setPassword(password);
		user.setPicture(picture);

		em.persist(user);
		statsejb.addUser();

		return user;
	}

	public User updateUser(User user) {
		checkUserAccess(user.getLogin(), "Security Error: You cannot modify data from other users.");

		User updated = em.find(User.class, user.getLogin());
		updated.setName(user.getName());
		updated.setPassword(user.getPassword());
		updated.setPicture(user.getPicture());

		return updated;
	}

	private void removeRelationships(User user) {
		user.setLikes(new ArrayList<Post>());
		em.createQuery("DELETE FROM Friend f WHERE f.friend1 = :user OR f.friend2 = :user").setParameter("user", user)
				.executeUpdate();
		em.createQuery("DELETE FROM Comment c WHERE c.user = :user").setParameter("user", user).executeUpdate();
	}

	public void removeUser(String login) {
		checkUserAccess(login, "Security Error: You cannot remove data from other users.");

		removeRelationships(em.find(User.class, login));
		em.createQuery("DELETE FROM User u WHERE u.login = :login").setParameter("login", login).executeUpdate();
		statsejb.removeUser();
	}

	public List<Post> getWall(User user) {
		List<Post> wall = this.postejb.getPosts(user);
		wall.addAll(this.postejb.getFriendPosts(user));

		return wall;
	}

	public void addLike(User user, Post post) {
		checkUserAccess(user.getLogin(), "Security Error: You cannot like posts on behalf of other users.");

		if (this.getWall(user).contains(post)) {
			List<Post> likedPosts = user.getLikes();
			likedPosts.add(post);
			user.setLikes(likedPosts);

			List<User> postLikers = post.getLikers();
			postLikers.add(user);
			post.setLikers(postLikers);

			emailejb.sendEmail(post.getPoster(), "New like", user.getName() + " has liked your post");
		} else {
			throw new SecurityException("You can only like posts from your wall.");
		}
	}

	public void removeLike(User user, Post post) {
		checkUserAccess(user.getLogin(), "Security Error: You cannot remove likes from other users.");

		if (this.getWall(user).contains(post)) {
			List<Post> likedPosts = user.getLikes();
			likedPosts.remove(post);
			user.setLikes(likedPosts);

			List<User> postLikers = post.getLikers();
			postLikers.remove(user);
			post.setLikers(postLikers);
		} else {
			throw new SecurityException("You can only remove likes from your wall.");
		}
	}

	public Friend addFriendship(User user1, User user2) {
		Friend friendship = new Friend(user1, user2);

		em.persist(friendship);
		return friendship;
	}

	public void acceptFriendship(User user1, User user2) {
		em.createQuery("UPDATE Friend f SET accepted = TRUE WHERE f.friend1 = :user2 AND f.friend2 = :user1")
				.setParameter("user1", user1).setParameter("user2", user2).executeUpdate();
	}

	public void rejectFriendship(User user1, User user2) {
		em.createQuery("DELETE FROM Friend f WHERE f.friend1 = :user2 AND f.friend2 = :user1")
				.setParameter("user1", user1).setParameter("user2", user2).executeUpdate();
	}

	@SuppressWarnings("unchecked")
	public List<User> getFriendRequests(final User user) {
		return (List<User>) em.createQuery("SELECT f.friend1 FROM Friend f WHERE f.friend2 = :user AND accept = FALSE")
				.setParameter("user", user).getResultList();
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

	public List<User> getPotentialFriends(User user) {
		checkUserAccess(user.getLogin(), "Security Error: You cannot access data from other users.");

		List<User> friends = new ArrayList<User>();
		getFriends(user).forEach(u -> friends.addAll(getFriends(u)));

		return friends;
	}
}
