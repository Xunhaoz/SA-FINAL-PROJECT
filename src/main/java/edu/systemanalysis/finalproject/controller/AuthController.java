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
        resp.put("response", llh.getAll(u.getId()));
        jsr.response(resp, response);
    }


    /**
     * 登入驗證
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();

        String email = jso.getString("email");
        String password = jso.getString("password");
        boolean remember = jso.getBoolean("remember");

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

        if (remember) {
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
        }

        String ip = jso.getString("ip");
        String city = jso.getString("city");
        String country = jso.getString("country");
        String operatingSystem = jso.getString("platform");
        LoginLog ll = new LoginLog(ip, country, city, operatingSystem, u.getId());
        ll.create();

        String resp = "{\"status\": 200, \"message\": '登入成功', 'response': ''}";
        jsr.response(resp, response);
    }


    public void destroy() {
    }
}
