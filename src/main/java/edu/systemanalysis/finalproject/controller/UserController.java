package edu.systemanalysis.finalproject.controller;

import java.io.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import org.json.*;
import cn.hutool.jwt.*;

import edu.systemanalysis.finalproject.app.User;
import edu.systemanalysis.finalproject.tools.JsonReader;


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

        JSONObject resp = new JSONObject();
        resp.put("status", 200);
        resp.put("message", "登入成功");
        resp.put("response", u.getData());
        jsr.response(resp, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();

        String email = jso.getString("email");
        String password = jso.getString("password");
        String repeatPassword = jso.getString("repeat_password");
        String firstName = jso.getString("first_name");
        String lastName = jso.getString("last_name");
        int role = jso.getInt("role");

        User u = new User(email, password, firstName, lastName, role);

        if (u.checkAttrEmpty()) {
            String resp = "{\"status\": 400, \"message\": '欄位不能有空值', 'response': ''}";
            jsr.response(resp, response);
            return;
        }

        if (u.checkEmailExist()) {
            String resp = "{\"status\": 400, \"message\": 'Email 不能重複', 'response': ''}";
            jsr.response(resp, response);
            return;
        }

        if (!password.equals(repeatPassword)) {
            String resp = "{\"status\": 400, \"message\": '驗證密碼不確', 'response': ''}";
            jsr.response(resp, response);
            return;
        }

        u.create();
        String resp = "{\"status\": 200, \"message\": \"註冊成功！！\", 'response': ''}";
        jsr.response(resp, response);
    }


    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();

        String headerValue = request.getHeader("Authentication");
        JWT jwt = JWTUtil.parseToken(headerValue);
        String email = jwt.getPayload("userName").toString();

        User u = new User(email);
        if (!jso.getString("email").isEmpty()) u.setEmail(jso.getString("email"));
        if (!jso.getString("password").isEmpty()) u.setPassword(jso.getString("password"));
        if (!jso.getString("first_name").isEmpty()) u.setFirstName(jso.getString("first_name"));
        if (!jso.getString("last_name").isEmpty()) u.setLastName(jso.getString("last_name"));

        u.setRole(jso.getInt("role"));
        u.update();

        String resp = "{\"status\": 200, \"message\": \"update successful\", \"response\": \"\"}";
        jsr.response(resp, response);
        return;
    }

    public void destroy() {
    }
}

