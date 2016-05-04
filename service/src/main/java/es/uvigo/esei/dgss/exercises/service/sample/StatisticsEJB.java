package es.uvigo.esei.dgss.exercises.service.sample;

import javax.annotation.PostConstruct;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Singleton
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class StatisticsEJB {

	@PersistenceContext
	private EntityManager em;

	private Long totalUsers;
	private Long totalPosts;

	@PostConstruct
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void init() {
		totalUsers = em.createQuery("SELECT COUNT(u) FROM User u", Long.class).getSingleResult();
		totalPosts = em.createQuery("SELECT COUNT(p) FROM Post p", Long.class).getSingleResult();
	}

	@Lock(LockType.READ)
	public Long getTotalUsers() {
		return totalUsers;
	}

	@Lock(LockType.READ)
	public Long getTotalPosts() {
		return totalPosts;
	}

	@Lock(LockType.WRITE)
	public void addUser() {
		++totalUsers;
	}

	@Lock(LockType.WRITE)
	public void removeUser() {
		--totalUsers;
	}

	@Lock(LockType.WRITE)
	public void addPost() {
		++totalPosts;
	}

	@Lock(LockType.WRITE)
	public void removePost() {
		--totalPosts;
	}

}