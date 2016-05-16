package es.uvigo.esei.dgss.exercises.jsf.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import es.uvigo.esei.dgss.exercises.domain.Post;
import es.uvigo.esei.dgss.exercises.domain.User;
import es.uvigo.esei.dgss.exercises.service.sample.PostEJB;
import es.uvigo.esei.dgss.exercises.service.sample.UserEJB;

@ManagedBean(name = "userController")
@SessionScoped
public class UserController {

	@EJB
	private UserEJB userejb;

	@EJB
	private PostEJB postejb;

	private String pattern;
	private User currentUser;
	private List<User> users;
	private List<Post> posts;

	@PostConstruct
	public void init() {
		this.pattern = "";
		this.users = new ArrayList<User>();
		this.posts = new ArrayList<Post>();
	}

	public UserEJB getUserEjb() {
		return userejb;
	}

	public void setUserEjb(UserEJB userEjb) {
		this.userejb = userEjb;
	}

	public PostEJB getPostEjb() {
		return postejb;
	}

	public void setPostEjb(PostEJB postEjb) {
		this.postejb = postEjb;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

	public String getViewId() {
		return FacesContext.getCurrentInstance().getViewRoot().getViewId();
	}

	public String redirectTo(String url) {
		return url + "?faces-redirect=true";
	}

	public String doSearch() {
		this.users = this.userejb.getMatchingUsers(this.pattern);

		return redirectTo(this.getViewId());
	}

	public String selectUser(User user) {
		this.setCurrentUser(user);
		this.setPosts(this.postejb.getPosts(user));

		return redirectTo(this.getViewId());
	}
}
