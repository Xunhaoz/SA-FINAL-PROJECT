package edu.systemanalysis.finalproject.controller;

import java.io.*;
import java.util.*;
import javax.servlet.http.*;
import javax.servlet.http.Cookie;
import javax.servlet.annotation.*;

import cn.hutool.core.date.*;
import cn.hutool.jwt.*;

import org.json.*;
import edu.systemanalysis.finalproject.app.User;
import edu.systemanalysis.finalproject.app.LoginLog;
import edu.systemanalysis.finalproject.app.LoginLogHelper;
import edu.systemanalysis.finalproject.tools.JsonReader;


@WebServlet(name = "auth", value = "/auth")
public class AuthController extends HttpServlet {
    LoginLogHelper llh = LoginLogHelper.getHelper();

    public void init() {

    }


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JsonReader jsr = new JsonReader(request);
        String authentication = request.getHeader("Authentication");

        if (authentication == null || authentication.isEmpty()) {
            String resp = "{\"status\": 403, \"message\": \"你沒有權限進入這個頁面\", \"response\": \"\"}";
            jsr.response(resp, response);
            return;
        }

        JWT jwt = JWTUtil.parseToken(authentication);
        String email = jwt.getPayload("userName").toString();

        User u = new User(email);
        if (!jwt.setKey(u.getSalt().getBytes()).verify()) {
            String resp = "{\"status\": 403, \"message\": \"你沒有權限進入這個頁面\", \"response\": \"\"}";
            jsr.response(resp, response);
            return;
        }

        String resp = "{\"status\": 200, \"message\": \"進入頁面成功\", \"response\": \"\"}";
        jsr.response(resp, response);
    }


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();

        String email = jso.getString("email");
        String password = jso.getString("password");

        User u = new User(email);

        if (!u.checkEmailExist()) {
            String resp = "{\"status\": 401, \"message\": '帳號不存在', 'response': ''}";
            jsr.response(resp, response);
            return;
        }

        if (!u.checkPasswordCorrect(password)) {
            String resp = "{\"status\": 401, \"message\": '密碼不正確', 'response': ''}";
            jsr.response(resp, response);
            return;
        }

        String salt = u.getSalt();
        DateTime now = DateTime.now();
        DateTime newTime = now.offset(DateField.MINUTE, 10);

        Map<String, Object> payload = new HashMap<String, Object>();
        payload.put(JWTPayload.ISSUED_AT, now);
        payload.put(JWTPayload.EXPIRES_AT, newTime);
        payload.put(JWTPayload.NOT_BEFORE, now);
        payload.put("userName", email);
        String token = JWTUtil.createToken(payload, salt.getBytes());
        Cookie cookie = new Cookie("Authentication", token);
        response.addCookie(cookie);

        String ip = jso.getString("ip");
        String city = jso.getString("city");
        String country = jso.getString("country");
        String operatingSystem = jso.getString("platform");
        LoginLog ll = new LoginLog(ip, country, city, operatingSystem, u.getId());
        ll.create();

        String resp = "{\"status\": 200, \"message\": \"登入成功\", \"response\": \"\"}";
        jsr.response(resp, response);
    }

    public void destroy() {
    }
}
