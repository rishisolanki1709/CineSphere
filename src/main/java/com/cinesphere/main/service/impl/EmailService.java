package com.cinesphere.main.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;

	@Async // Don't make the user wait for the email to send!
	public void sendBookingConfirmation(String toMail, String subject, String body) {

		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

			helper.setTo(toMail);
			helper.setSubject(subject);
			helper.setFrom("cinesphere.tickets@gmail.com", "CineSphere");

			helper.setText(body, true);
			mailSender.send(message);
		} catch (Exception e) {
			System.err.println("DEBUG: Mail Error - " + e.getMessage());
			e.printStackTrace();
		}
	}

}