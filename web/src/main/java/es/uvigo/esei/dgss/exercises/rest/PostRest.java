package es.uvigo.esei.dgss.exercises.rest;

import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import es.uvigo.esei.dgss.exercises.domain.Link;
import es.uvigo.esei.dgss.exercises.domain.Photo;
import es.uvigo.esei.dgss.exercises.domain.Post;
import es.uvigo.esei.dgss.exercises.domain.Video;
import es.uvigo.esei.dgss.exercises.service.sample.PostEJB;
import es.uvigo.esei.dgss.exercises.service.sample.UserEJB;

@Path("/user/{login}/post")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PostRest {

	@EJB
	private UserEJB userejb;
	@EJB
	private PostEJB postejb;

	@GET
	@Path("{login}/wall")
	public Response getWall(@PathParam("login") String login) {
		final Collection<Post> wall = userejb.getWall(userejb.findUserById(login));

		if (wall.isEmpty()) {
			return Response.noContent().build();
		}

		return Response.ok(wall).build();
	}

	@GET
	public Response getPosts(@PathParam("login") String login) {
		List<Post> posts = this.postejb.getPosts(this.userejb.findUserById(login));

		if (posts.isEmpty()) {
			return Response.noContent().build();
		} else {
			return Response.ok(posts).build();
		}
	}

	@POST
	@Path("{id}/like")
	public Response addLike(@PathParam("login") String login, @PathParam("id") int id) {
		this.userejb.addLike(this.userejb.findUserById(login), this.postejb.findPostById(id));

		return Response.ok().build();
	}

	@DELETE
	@Path("{id}/like")
	public Response deleteLike(@PathParam("login") String login, @PathParam("id") int id) {
		this.userejb.removeLike(this.userejb.findUserById(login), this.postejb.findPostById(id));

		return Response.ok().build();
	}

	@POST
	@Path("link")
	public Response postLink(@PathParam("login") String login, String url) {
		this.postejb.addLink(new Link(this.userejb.findUserById(login), url));

		return Response.ok().build();
	}

	@POST
	@Path("photo")
	public Response postPhoto(@PathParam("login") String login, String content) {
		this.postejb.addPhoto(new Photo(this.userejb.findUserById(login), content));

		return Response.ok().build();
	}

	@POST
	@Path("video")
	public Response postVideo(@PathParam("login") String login, int duration) {
		this.postejb.addVideo(new Video(this.userejb.findUserById(login), duration));

		return Response.ok().build();
	}

	@DELETE
	public Response deletePost(@PathParam("login") String login, int id) {
		this.postejb.removePost(id);

		return Response.ok().build();
	}

	@PUT
	@Path("photo")
	public Response modifyPhoto(@PathParam("login") String login, Photo post) {
		this.postejb.updatePost(post);

		return Response.ok().build();
	}

	@PUT
	@Path("link")
	public Response modifyLink(@PathParam("login") String login, Link post) {
		this.postejb.updatePost(post);

		return Response.ok().build();
	}

	@PUT
	@Path("video")
	public Response modifyVideo(@PathParam("login") String login, Video post) {
		this.postejb.updatePost(post);

		return Response.ok().build();
	}

	@POST
	@Path("comment")
	public Response addComment(@PathParam("login") String login, int id, String content) {
		this.postejb.addComment(this.userejb.findUserById(login), this.postejb.findPostById(id), content);

		return Response.ok().build();
	}
}
