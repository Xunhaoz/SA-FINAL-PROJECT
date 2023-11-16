package edu.systemanalysis.finalproject.controller;

import cn.hutool.core.date.DateUtil;
import edu.systemanalysis.finalproject.app.Class;
import edu.systemanalysis.finalproject.app.ClassHelper;
import edu.systemanalysis.finalproject.app.UserClass;
import edu.systemanalysis.finalproject.app.UserClassHelper;
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
        String studentIdString = request.getHeader("student_id");
        String teacherIdString = request.getHeader("teacher_id");
        JSONObject resp = new JSONObject();

        resp.put("status", 200);
        resp.put("message", "資料獲取成功");

        if (studentIdString != null && !studentIdString.isEmpty()) {
            int studentId = Integer.parseInt(studentIdString);
            resp.put("response", uch.selectAllByStudentId(studentId));
        } else if (teacherIdString != null && !teacherIdString.isEmpty()) {
            int teacherId = Integer.parseInt(teacherIdString);
            resp.put("response", uch.selectAllByTeacherId(teacherId));

        } else {
            resp.put("response", uch.selectAll());
        }

        jsr.response(resp, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();

        int studentId = jso.getInt("student_id");
        int teacherId = jso.getInt("class_id");

        UserClass uc = new UserClass(studentId, teacherId);
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
