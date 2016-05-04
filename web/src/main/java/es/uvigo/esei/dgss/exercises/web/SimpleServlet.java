package es.uvigo.esei.dgss.exercises.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import es.uvigo.esei.dgss.exercises.domain.Photo;
import es.uvigo.esei.dgss.exercises.domain.Post;
import es.uvigo.esei.dgss.exercises.domain.User;
import es.uvigo.esei.dgss.exercises.service.sample.PostEJB;
import es.uvigo.esei.dgss.exercises.service.sample.UserEJB;

@SuppressWarnings("serial")
@WebServlet("/SimpleServlet")
public class SimpleServlet extends HttpServlet {
	@Inject
	private Facade facade;

	@EJB
	private UserEJB userejb;

	@EJB
	private PostEJB postejb;

	@Resource
	private UserTransaction transaction;

	private void facadeTests(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PrintWriter writer = resp.getWriter();

		writer.println("<html>");
		writer.println("<body>");
		writer.println("<h1>Facade tests</h1>");

		// work with Facade

		try {
			transaction.begin();

			User user1 = facade.addUser(UUID.randomUUID().toString(), "Leonardo", "splinter", new byte[] {});
			User user2 = facade.addUser(UUID.randomUUID().toString(), "Raphael", "splinter", new byte[] {});
			User user3 = facade.addUser(UUID.randomUUID().toString(), "Michelangelo", "splinter", new byte[] {});
			User user4 = facade.addUser(UUID.randomUUID().toString(), "Donatello", "splinter", new byte[] {});

			facade.addFriendship(user1, user2);
			facade.addFriendship(user1, user3);
			facade.addFriendship(user1, user4);
			facade.addFriendship(user2, user3);
			facade.addFriendship(user2, user4);
			facade.addFriendship(user3, user4);

			Post post = facade.addPost(user1, "test_post_photo");
			facade.addLike(user2, post);
			facade.addComment(user2, post, "test_post_comment_2");
			facade.addLike(user3, post);
			facade.addComment(user3, post, "test_post_comment_3");

			transaction.commit();

			writer.println("<h2>getFriends</h2></br>");
			List<User> friends = facade.getFriends(user1);
			friends.forEach(f -> writer.println(f.getLogin() + "</br>"));

			writer.println("<h2>getFriendPosts</h2></br>");
			List<Post> posts = facade.getFriendPosts(user2);
			posts.forEach(p -> writer.println(p.getId() + "</br>"));

			writer.println("<h2>getPostsCommentedAfterDate</h2></br>");
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
			List<Post> commenters = facade.getPostsCommentedAfterDate(user1, dateFormat.parse("21-10-2015 07:28:42"));
			commenters.forEach(c -> writer.println(c.getId() + "</br>"));

			writer.println("<h2>getFriendsWhoLikedPost</h2></br>");
			List<User> likes = facade.getFriendsWhoLikedPost(user1, post);
			likes.forEach(f -> writer.println(f.getLogin() + "</br>"));

			writer.println("<h2>getPicturesLiked</h2></br>");
			List<Photo> photos = facade.getPicturesLiked(user2);
			photos.forEach(p -> writer.println(p.getId() + "</br>"));

			writer.println("<h2>getPotentialFriends</h2></br>");
			List<User> potential = facade.getPotentialFriends(user2);
			potential.forEach(f -> writer.println(f.getLogin() + "</br>"));

		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException
				| HeuristicMixedException | HeuristicRollbackException | ParseException e) {
			try {
				transaction.rollback();
			} catch (IllegalStateException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SecurityException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SystemException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		writer.println("</body>");
		writer.println("</html>");
	}

	private void ejbTests(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PrintWriter writer = resp.getWriter();

		writer.println("<html>");
		writer.println("<body>");
		writer.println("<h1>EJB tests</h1>");

		// work with EJB

		try {
			transaction.begin();

			User user1 = userejb.addUser(UUID.randomUUID().toString(), "Leonardo", "splinter", new byte[] {});
			User user2 = userejb.addUser(UUID.randomUUID().toString(), "Raphael", "splinter", new byte[] {});
			User user3 = userejb.addUser(UUID.randomUUID().toString(), "Michelangelo", "splinter", new byte[] {});
			User user4 = userejb.addUser(UUID.randomUUID().toString(), "Donatello", "splinter", new byte[] {});

			userejb.addFriendship(user1, user2);
			userejb.addFriendship(user1, user3);
			userejb.addFriendship(user1, user4);
			userejb.addFriendship(user2, user3);
			userejb.addFriendship(user2, user4);
			userejb.addFriendship(user3, user4);

			Post post = postejb.addPhoto(new Photo(user1, "test_post_photo"));
			userejb.addLike(user2, post);
			postejb.addComment(user2, post, "test_post_comment_2");
			userejb.addLike(user3, post);
			postejb.addComment(user3, post, "test_post_comment_3");

			transaction.commit();

			writer.println("<h2>getFriends</h2></br>");
			List<User> friends = userejb.getFriends(user1);
			friends.forEach(f -> writer.println(f.getLogin() + "</br>"));

			writer.println("<h2>getFriendPosts</h2></br>");
			List<Post> posts = postejb.getFriendPosts(user2);
			posts.forEach(p -> writer.println(p.getId() + "</br>"));

			writer.println("<h2>getPostsCommentedAfterDate</h2></br>");
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
			List<Post> commenters = postejb.getPostsCommentedAfterDate(user1, dateFormat.parse("21-10-2015 07:28:42"));
			commenters.forEach(c -> writer.println(c.getId() + "</br>"));

			writer.println("<h2>getFriendsWhoLikedPost</h2></br>");
			List<User> likes = postejb.getFriendsWhoLikedPost(user1, post);
			likes.forEach(f -> writer.println(f.getLogin() + "</br>"));

			writer.println("<h2>getPicturesLiked</h2></br>");
			List<Photo> photos = postejb.getPicturesLiked(user2);
			photos.forEach(p -> writer.println(p.getId() + "</br>"));

			writer.println("<h2>getPotentialFriends</h2></br>");
			List<User> potential = userejb.getPotentialFriends(user2);
			potential.forEach(f -> writer.println(f.getLogin() + "</br>"));

		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException
				| HeuristicMixedException | HeuristicRollbackException | ParseException e) {
			try {
				transaction.rollback();
			} catch (IllegalStateException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SecurityException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SystemException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		writer.println("</body>");
		writer.println("</html>");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// facadeTests(req, resp);
		// ejbTests(req, resp);
	}
}