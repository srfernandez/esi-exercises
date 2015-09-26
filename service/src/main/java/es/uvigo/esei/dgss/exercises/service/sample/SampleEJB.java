package es.uvigo.esei.dgss.exercises.service.sample;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class SampleEJB {

	@PersistenceContext
	private EntityManager em;
	
	public Long countUsers() {
		return em.createQuery("SELECT count(u) FROM User u", Long.class).getSingleResult();
	}
}
