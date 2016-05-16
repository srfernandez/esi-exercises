package es.uvigo.esei.dgss.exercises.jsf.controllers;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "myController")
@SessionScoped
public class PruebaController implements Serializable {
	private String campo1;
	private String campo2;
	private Date fecha;

	@PostConstruct
	public void inicializarFecha() {
		this.fecha = Calendar.getInstance().getTime();
	}

	// Getter and Setter for campo1, campo2, fecha
}
