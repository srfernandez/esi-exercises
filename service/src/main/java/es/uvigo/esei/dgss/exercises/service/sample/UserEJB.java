package es.uvigo.esei.dgss.exercises.service.sample;

import java.util.ArrayList;
import java.util.Date;
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
	private EmailServiceEJB emailejb;

	@Resource
	private SessionContext ctx;

	public User findUserById(String login) {
		return em.find(User.class, login);
	}

	public User addUser(String login, String name, String password, byte[] picture) {
		User user = new User(login);

		user.setName(name);
		user.setPassword(password);
		user.setPicture(picture);

		em.persist(user);
		return user;
	}

	public void updateUser(User user) {
		User updated = em.find(User.class, user.getLogin());
		updated.setName(user.getName());
		updated.setPassword(user.getPassword());
		updated.setPicture(user.getPicture());
	}

	private void removeRelationships(User user) {
		user.setLikes(new ArrayList<Post>());
		em.createQuery("DELETE FROM Friend f WHERE f.friend1 = :user OR f.friend2 = :user").setParameter("user", user)
				.executeUpdate();
		em.createQuery("DELETE FROM Comment c WHERE c.user = :user").setParameter("user", user).executeUpdate();
	}

	public void removeUser(String login) {
		removeRelationships(em.find(User.class, login));
		em.createQuery("DELETE FROM User u WHERE u.login = :login").setParameter("login", login).executeUpdate();
	}

	public void addLike(User user, Post post) {
		List<Post> likedPosts = user.getLikes();
		likedPosts.add(post);
		user.setLikes(likedPosts);

		List<User> postLikers = post.getLikers();
		postLikers.add(user);
		post.setLikers(postLikers);

		emailejb.sendEmail(post.getPoster(), "New like", user.getName() + " has liked your post");
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

	public List<User> getPotentialFriends(User user) {
		List<User> friends = new ArrayList<User>();
		getFriends(user).forEach(u -> friends.addAll(getFriends(u)));

		return friends;
	}
}
