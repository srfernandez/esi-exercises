package es.uvigo.esei.dgss.exercises.service.sample;

import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import es.uvigo.esei.dgss.exercises.domain.User;

@Stateless
public class EmailServiceEJB {
	@PersistenceContext
	private EntityManager em;

	@Resource(name = "java:jboss/mail/gmail")
	private Session session;

	@Asynchronous
	public void sendEmail(User u, String subject, String body) {
		try {
			Message message = new MimeMessage(session);
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(u.getLogin()));
			message.setSubject(subject);
			message.setText(body);

			Transport.send(message);

		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
