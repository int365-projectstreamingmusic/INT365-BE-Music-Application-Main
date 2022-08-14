package com.application.utilities;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailServiceUtility {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private SpringTemplateEngine templateEngine;

	@Value("${spring.mail.username}")
	private String senderEmail;

	public void sendHtmlEmail() {

	}

	public void sendHtmlRmail(String reciever) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
				StandardCharsets.UTF_8.name());

		Context context = new Context();
		context.setVariables(verificationContentPlacer());

		messageHelper.setSubject("Hello " + reciever + "!");
		messageHelper.setFrom(this.senderEmail);
		messageHelper.setTo(reciever);

		String mailHtmltemplate = templateEngine.process("verification-email.html", context);
		boolean isHtml = true;
		messageHelper.setText(mailHtmltemplate, isHtml);

		mailSender.send(message);

	}

	public Map<String, Object> verificationContentPlacer() {
		Map<String, Object> properties = new HashMap<>();
		//properties.put("name", "Ashish");
		//properties.put("subscriptionDate", LocalDate.now().toString());
		//properties.put("technologies", Arrays.asList("Python", "Go", "C#"));
		properties.put("link-rel", "awdwd");
		properties.put("registered-username", "DUMMY!");
		return properties;
	}

}