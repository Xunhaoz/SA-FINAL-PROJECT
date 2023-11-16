package edu.systemanalysis.finalproject.controller;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import org.json.*;
import cn.hutool.crypto.digest.DigestUtil;
import edu.systemanalysis.finalproject.app.User;
import edu.systemanalysis.finalproject.tools.JsonReader;

import javax.mail.*;
import java.util.*;
import javax.mail.internet.*;


@WebServlet(name = "user", value = "/user")
public class UserController extends HttpServlet {
    public void init() {

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JsonReader jsr = new JsonReader(request);

        String headerValue = request.getHeader("Authentication");
        JWT jwt = JWTUtil.parseToken(headerValue);
        String email = jwt.getPayload("userName").toString();
        User u = new User(email);

        if (!jwt.setKey(u.getSalt().getBytes()).verify()) {
            String resp = "{\"status\": 401, \"message\": \"jwt 登入失敗\", \"response\": \"\"}";
            jsr.response(resp, response);
            return;
        }

        JSONObject resp = new JSONObject();
        resp.put("status", 200);
        resp.put("message", "jwt 登入成功");
        resp.put("response", u.getData());
        jsr.response(resp, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();

        String email = jso.getString("email");
        String password = jso.getString("password");
        String firstName = jso.getString("first_name");
        String lastName = jso.getString("last_name");

        int role = jso.getInt("role");
        User u = new User(email, password, firstName, lastName, role);

        if (u.checkAttrEmpty()) {
            String resp = "{\"status\": 400, \"message\": '欄位不能有空值', 'response': ''}";
            jsr.response(resp, response);
            return;
        }

        if (u.checkEmailDuplicate()) {
            String resp = "{\"status\": 400, \"message\": 'Email 不能重複', 'response': ''}";
            jsr.response(resp, response);
            return;
        }

        u.create();
        String resp = "{\"status\": 200, \"message\": 'doPost 成功', 'response': ''}";
        jsr.response(resp, response);
    }


    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();

        String email = jso.getString("email");

        User u = new User(email);
        u.setPassword(jso.getString("password"));
        u.setFirstName(jso.getString("first_name"));
        u.setLastName(jso.getString("last_name"));
        u.setRole(jso.getInt("role"));
        u.update();

        String resp = "{\"status\": 200, \"message\": \"update successful\", \"response\": \"\"}";
        jsr.response(resp, response);
        return;
    }


    public void doHead(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String headerValue = request.getHeader("Email"), txt;

        User u = new User(headerValue);
        System.out.println("test");
        JsonReader jsr = new JsonReader(request);

        Properties prop = new Properties();
        prop.setProperty("mail.transport.protocol", "smtp");
        prop.setProperty("mail.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "465");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        prop.put("mail.smtp.socketFactory.port", "465");
        prop.put("mail.debug", "true");

        String userName = "leo20020529@gmail.com";
        String password = "revtexjpeqwjqflo";

        System.out.println(headerValue);
        Auth auth = new Auth(userName, password);
        Session session = Session.getDefaultInstance(prop, auth);
        MimeMessage message = new MimeMessage(session);

        System.out.println("txt");
        System.out.println(u.checkEmailExist());

        if (!u.checkEmailExist()) {
            txt = "帳號不存在";
        } else {
            u.updateRandomPassword();
            txt = "你的新密碼為：" + u.getPassword() + "<br>請盡速修正您的密碼";
        }

        try {
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(headerValue));
            message.setSubject("mentor 更改密碼");
            message.setContent(txt, "text/html;charset = UTF-8");
            Transport transport = session.getTransport();
            Transport.send(message);
            transport.close();

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void destroy() {
    }
}

class Auth extends Authenticator {

    final private String userName;
    final private String password;

    public Auth(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        PasswordAuthentication pa = new PasswordAuthentication(userName, password);
        return pa;
    }
}