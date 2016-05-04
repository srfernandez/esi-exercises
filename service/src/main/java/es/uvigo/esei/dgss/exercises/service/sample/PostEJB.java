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

import es.uvigo.esei.dgss.exercises.domain.Comment;
import es.uvigo.esei.dgss.exercises.domain.Link;
import es.uvigo.esei.dgss.exercises.domain.Photo;
import es.uvigo.esei.dgss.exercises.domain.Post;
import es.uvigo.esei.dgss.exercises.domain.User;
import es.uvigo.esei.dgss.exercises.domain.Video;

@Stateless
public class PostEJB {

	@PersistenceContext
	private EntityManager em;

	@EJB
	private UserEJB userejb;

	@EJB
	private StatisticsEJB stats;

	@Resource
	private SessionContext ctx;

	private void checkUserAccess(String login, String message) {
		if (!ctx.getCallerPrincipal().getName().equals(login) && !ctx.isCallerInRole("admin")) {
			throw new SecurityException(message);
		}
	}

	public Post findPostById(int id) {
		return em.find(Post.class, id);
	}

	public Link addLink(Link link) {
		checkUserAccess(link.getPoster().getLogin(), "Security Error: You cannot add links on behalf of other users.");

		this.em.persist(link);
		stats.addPost();

		return link;
	}

	public Photo addPhoto(Photo photo) {
		checkUserAccess(photo.getPoster().getLogin(),
				"Security Error: You cannot add photos on behalf of other users.");

		this.em.persist(photo);
		stats.addPost();

		return photo;
	}

	public Video addVideo(Video video) {
		checkUserAccess(video.getPoster().getLogin(),
				"Security Error: You cannot add videos on behalf of other users.");

		this.em.persist(video);
		stats.addPost();

		return video;
	}

	public void updatePost(Post post) {
		checkUserAccess(post.getPoster().getLogin(), "Security Error: You cannot modify posts from other users.");

		Post updated = findPostById(post.getId());
		updated.setComments(post.getComments());
		updated.setDate(post.getDate());
		updated.setLikers(post.getLikers());
		updated.setPoster(post.getPoster());
	}

	public void removePost(int id) {
		checkUserAccess(findPostById(id).getPoster().getLogin(),
				"Security Error: You cannot remove posts from other users.");

		em.remove(findPostById(id));
	}

	@SuppressWarnings("unchecked")
	public List<Post> getPosts(User user) {
		checkUserAccess(user.getLogin(), "Security Error: You cannot access posts from other users.");

		return (List<Post>) em.createQuery("SELECT p FROM Post p WHERE p.poster = :user").setParameter("user", user)
				.getResultList();
	}

	public List<Post> getFriendPosts(User user) {
		List<Post> posts = new ArrayList<Post>();

		userejb.getFriends(user).forEach(f -> posts.addAll(getPosts(f)));

		return posts;
	}

	@SuppressWarnings("unchecked")
	private List<Comment> getComments(User user) {
		checkUserAccess(user.getLogin(), "Security Error: You cannot access comments from other users.");

		return (List<Comment>) em.createQuery("SELECT c FROM Comment c WHERE c.user = :user").setParameter("user", user)
				.getResultList();
	}

	public List<Post> getPostsCommentedAfterDate(User user, Date date) {
		checkUserAccess(user.getLogin(), "Security Error: You cannot access comments from other users.");

		List<Post> posts = new ArrayList<Post>();

		userejb.getFriends(user).forEach(f -> getComments(f).forEach(c -> {
			if (c.getDate().after(date))
				posts.add(c.getPost());
		}));

		return posts;
	}

	@SuppressWarnings("unchecked")
	private List<Post> getLiked(User user) {
		checkUserAccess(user.getLogin(), "Security Error: You cannot access likes from other users.");

		return (List<Post>) em.createQuery("SELECT u.likes FROM User u WHERE u.login = :user")
				.setParameter("user", user.getLogin()).getResultList();
	}

	public List<User> getFriendsWhoLikedPost(User user, Post post) {
		List<User> users = new ArrayList<User>();

		userejb.getFriends(user).forEach(f -> {
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

	public Comment addComment(User user, Post post, String content) {
		Comment comment = new Comment(user, post);
		comment.setContent(content);
		em.persist(comment);
		return comment;
	}
}
