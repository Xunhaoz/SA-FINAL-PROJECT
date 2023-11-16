package edu.systemanalysis.finalproject.controller;

import java.io.IOException;
import java.util.Properties;

import edu.systemanalysis.finalproject.app.User;
import edu.systemanalysis.finalproject.app.Auth;
import edu.systemanalysis.finalproject.tools.JsonReader;

import javax.mail.*;
import javax.mail.internet.*;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

import org.json.JSONObject;

import cn.hutool.core.lang.*;

@WebServlet(name = "mail", value = "/mail")
public class MailController extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();
        String email = jso.getString("email");
        User u = new User(email);

        if (!Validator.isEmail(email)) {
            String resp = "{\"status\": 400, \"message\": \"Email 規則不符，請重新輸入Email\", \"response\": \"\"}";
            jsr.response(resp, response);
            return;
        }

        if (!u.checkEmailExist()) {
            String resp = "{\"status\": 400, \"message\": \"Email 郵件不存在\", \"response\": \"\"}";
            jsr.response(resp, response);
            return;
        }

        u.updateRandomPassword();
        Properties prop = new Properties();
        prop.setProperty("mail.transport.protocol", "smtp");
        prop.setProperty("mail.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "465");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        prop.put("mail.smtp.socketFactory.port", "465");
        prop.put("mail.debug", "true");

        Auth auth = new Auth("leo20020529@gmail.com", "revtexjpeqwjqflo");
        Session session = Session.getDefaultInstance(prop, auth);
        MimeMessage message = new MimeMessage(session);

        try {
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
            message.setSubject("mentor 更改密碼");
            message.setContent("你的新密碼為：" + u.getPassword() + "<br>請盡速修正您的密碼", "text/html;charset = UTF-8");
            Transport transport = session.getTransport();
            Transport.send(message);
            transport.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        String resp = "{\"status\": 200, \"message\": \"密碼更新成功\", \"response\": ''}";
        jsr.response(resp, response);
    }
}
