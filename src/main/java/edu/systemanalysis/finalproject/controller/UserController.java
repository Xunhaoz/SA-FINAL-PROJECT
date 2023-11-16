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
        u.setPassword(jso.getString("password"));
        u.setFirstName(jso.getString("first_name"));
        u.setLastName(jso.getString("last_name"));
        u.setRole(jso.getInt("role"));
        u.update();

        String resp = "{\"status\": 200, \"message\": \"update successful\", \"response\": \"\"}";
        jsr.response(resp, response);
        return;
    }

    public void destroy() {
    }
}

