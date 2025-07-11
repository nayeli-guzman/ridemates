package com.ridemates.app.email.domain;

import com.ridemates.app.booking.domain.Booking;
import com.ridemates.app.user.domain.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;


    public void sendValidateEmail(String to, User user) throws MessagingException {

        final Context ctx = new Context();
        ctx.setVariable("name",
                user.getFirstName() + " " + user.getLastName());
        ctx.setVariable("verificationCode", user.getVerificationCode());

        final String htmlContent = this.templateEngine.process("html/emailTemplate.html", ctx);

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message
                , MimeMessageHelper.MULTIPART_MODE_MIXED
                , StandardCharsets.UTF_8.name());

        helper.setTo(to);
        helper.setSubject("Email de Verificación");
        helper.setText(htmlContent, true);

        emailSender.send(message);
    }

    public void sendStatusBookingEmail(Booking booking) throws MessagingException {

        final Context ctx = new Context();
        ctx.setVariable("nombreUsuario", booking.getPassenger().getFirstName() + " " + booking.getPassenger().getLastName());
        ctx.setVariable("nuevoEstado", booking.getStatus());

        final String htmlContent = this.templateEngine.process("html/emailStatusBooking.html", ctx);

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message
                , MimeMessageHelper.MULTIPART_MODE_MIXED
                , StandardCharsets.UTF_8.name());

        helper.setTo(booking.getPassenger().getEmail());
        helper.setSubject("Status de tu viaje solicitado");
        helper.setText(htmlContent, true);

        emailSender.send(message);
    }


    public void sendDeleteBookingEmail(Booking booking) throws MessagingException {

        final Context ctx = new Context();
        ctx.setVariable("nombreCliente", booking.getPassenger().getFirstName() + " " + booking.getPassenger().getLastName());
        ctx.setVariable("numeroReserva", booking.getId());
        ctx.setVariable("fechaViaje", booking.getRoute().getDateTime());

        final String htmlContent = this.templateEngine.process("html/emailDeleteBooking.html", ctx);

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message
                , MimeMessageHelper.MULTIPART_MODE_MIXED
                , StandardCharsets.UTF_8.name());

        helper.setTo(booking.getPassenger().getEmail());
        helper.setSubject("Cancelación de viaje");
        helper.setText(htmlContent, true);

        emailSender.send(message);
    }



}
