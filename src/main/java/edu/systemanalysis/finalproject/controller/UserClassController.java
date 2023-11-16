package edu.systemanalysis.finalproject.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import edu.systemanalysis.finalproject.app.*;
import edu.systemanalysis.finalproject.app.Class;
import edu.systemanalysis.finalproject.tools.JsonReader;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Timestamp;

@WebServlet(name = "user_class", value = "/user_class")
public class UserClassController extends HttpServlet {
    UserClassHelper uch = UserClassHelper.getHelper();

    public void init() {

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JsonReader jsr = new JsonReader(request);

        String headerValue = request.getHeader("Authentication");
        JWT jwt = JWTUtil.parseToken(headerValue);
        String email = jwt.getPayload("userName").toString();
        String teacherIdString = request.getHeader("teacher_id");

        JSONObject resp = new JSONObject();

        resp.put("status", 200);
        resp.put("message", "資料獲取成功");

        if (email != null && !email.isEmpty()) {
            User u = new User(email);
            resp.put("response", uch.selectAll(u.getId()));
        }

        jsr.response(resp, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();

        String headerValue = request.getHeader("Authentication");
        JWT jwt = JWTUtil.parseToken(headerValue);
        String email = jwt.getPayload("userName").toString();
        User u = new User(email);

        int studentId = u.getId();
        int classId = jso.getInt("class_id");

        UserClass uc = new UserClass(studentId, classId);
        uc.insert();

        JSONObject resp = new JSONObject();
        resp.put("status", 200);
        resp.put("message", "資料新增成功");
        resp.put("response", "");
        jsr.response(resp, response);
    }

    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
    }
}
