package com.eggedu.tinder.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class NotificacionService {
	/*
	@Autowired
	private JavaMailSender mailSender;
	
	@Async
	public void enviar(String cuerpo, String titulo, String email) {
		
		SimpleMailMessage mensaje = new SimpleMailMessage();
		mensaje.setTo(email);
		mensaje.setFrom("noreply@tinder-mascota.com");
		mensaje.setSubject(titulo);
		mensaje.setText(cuerpo);
		
		mailSender.send(mensaje);
	}
	*/
	
}
