package es.uvigo.esei.dgss.exercises.rest;

import java.net.URI;
import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import es.uvigo.esei.dgss.exercises.domain.User;
import es.uvigo.esei.dgss.exercises.service.sample.PostEJB;
import es.uvigo.esei.dgss.exercises.service.sample.UserEJB;

@Path("/user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserRest {
	@EJB
	private UserEJB userejb;

	@EJB
	private PostEJB postejb;

	@Context
	private UriInfo uriInfo;

	@POST
	public Response createUser(User user) {
		userejb.addUser(user.getLogin(), user.getName(), user.getPassword(), user.getPicture());
		URI userUri = uriInfo.getAbsolutePathBuilder().path(user.getLogin().toString()).build();

		return Response.created(userUri).build();
	}

	@POST
	@Path("{login}/friendship/request")
	public void requestFriendship(@PathParam("login") String userLogin, String friendLogin) {
		userejb.addFriendship(userejb.findUserById(userLogin), userejb.findUserById(friendLogin));
	}

	@GET
	@Path("{login}/friendship/requests")
	public Response getFriendshipRequests(@PathParam("login") String login) {
		List<User> requests = userejb.getFriendRequests(userejb.findUserById(login));

		if (requests.isEmpty()) {
			return Response.noContent().build();
		}

		return Response.ok(requests).build();
	}

	@PUT
	@Path("{login}/friendship/accept")
	public void acceptFriendship(@PathParam("login") String login, String friendLogin) {
		userejb.acceptFriendship(userejb.findUserById(login), userejb.findUserById(friendLogin));
	}

	@PUT
	@Path("{login}/friendship/reject")
	public void rejectFriendship(@PathParam("login") String login, String friendLogin) {
		userejb.rejectFriendship(userejb.findUserById(login), userejb.findUserById(friendLogin));
	}

	@GET
	@Path("{login}")
	public Response getUserDetail(@PathParam("login") String login) {
		User user = userejb.findUserById(login);

		if (user == null) {
			return Response.noContent().build();
		}

		return Response.ok(user).build();
	}

	@PUT
	@Path("{login}")
	public void updateUser(@PathParam("login") String login, User user) {
		userejb.updateUser(user);
	}

	@GET
	@Path("{login}/friends")
	public Response friends(@PathParam("login") String login) {
		List<User> users = userejb.getFriends(userejb.findUserById(login));

		if (users.isEmpty()) {
			return Response.noContent().build();
		}

		return Response.ok(users).build();
	}
}
