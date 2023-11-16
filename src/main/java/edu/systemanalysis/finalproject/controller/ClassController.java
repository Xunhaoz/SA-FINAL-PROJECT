package edu.systemanalysis.finalproject.controller;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import edu.systemanalysis.finalproject.app.ClassHelper;
import edu.systemanalysis.finalproject.app.Class;
import edu.systemanalysis.finalproject.app.User;
import edu.systemanalysis.finalproject.tools.JsonReader;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;

import cn.hutool.core.date.*;
;

@WebServlet(name = "class", value = "/class")
public class ClassController extends HttpServlet {
    ClassHelper ch = ClassHelper.getHelper();

    public void init() {

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JsonReader jsr = new JsonReader(request);
        String classIdString = request.getHeader("id");
        JSONObject resp = new JSONObject();

        resp.put("status", 200);
        resp.put("message", "資料獲取成功");

        if (classIdString != null && !classIdString.isEmpty()) {
            int classId = Integer.parseInt(classIdString);
            resp.put("response", ch.selectOne(classId).getData());
        } else {
            resp.put("response", ch.selectAll());
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

        int teacherId = u.getId();
        String content = jso.getString("class_content");
        String topic = jso.getString("class_topic");
        Timestamp midtermTime = DateUtil.parse(jso.getString("midterm_time")).toTimestamp();
        Timestamp finalTime = DateUtil.parse(jso.getString("final_time")).toTimestamp();

        Class c = new Class(teacherId, content, topic, midtermTime, finalTime);
        c.insert();

        JSONObject resp = new JSONObject();

        resp.put("status", 200);
        resp.put("message", "資料新增成功");
        resp.put("response", "");
        jsr.response(resp, response);
    }

    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();

        int id = jso.getInt("id");
        Class c = new Class(id);

        if (jso.has("teacher_id")) c.setTeacherId(jso.getInt("teacher_id"));
        if (jso.has("content")) c.setContent(jso.getString("content"));
        if (jso.has("topic")) c.setTopic(jso.getString("topic"));
        if (jso.has("midterm_time")) c.setMidtermTime(DateUtil.parse(jso.getString("midterm_time")).toTimestamp());
        if (jso.has("final_time")) c.setFinalTime(DateUtil.parse(jso.getString("final_time")).toTimestamp());

        c.update();

        JSONObject resp = new JSONObject();
        resp.put("status", 200);
        resp.put("message", "更新資料成功");
        resp.put("response", "");
        jsr.response(resp, response);
    }

}
