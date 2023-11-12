package edu.systemanalysis.finalproject.controller;

import java.io.*;
import java.lang.reflect.Member;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import org.json.*;
import edu.systemanalysis.finalproject.tools.JsonReader;
import edu.systemanalysis.finalproject.app.User;
import edu.systemanalysis.finalproject.app.UserHelper;


@WebServlet(name = "user", value = "/user")
public class UserController extends HttpServlet {
    public void init() {

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + "This is user route" + "</h1>");
        out.println("</body></html>");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();

        String email = jso.getString("email");
        String password = jso.getString("password");
        String firstName = jso.getString("first_name");
        String lastName = jso.getString("last_name");

        int role = jso.getInt("role");
        User m = new User(email, password, firstName, lastName, role);

        if (m.checkAttrEmpty()) {
            String resp = "{\"status\": 400, \"message\": '欄位不能有空值', 'response': ''}";
            jsr.response(resp, response);
            return;
        }

        if (m.checkEmailDuplicate()) {
            String resp = "{\"status\": 400, \"message\": 'Email 不能重複', 'response': ''}";
            jsr.response(resp, response);
            return;
        }

        if (m.checkEmailDuplicate()) {
            String resp = "{\"status\": 400, \"message\": 'Email 不能重複', 'response': ''}";
            jsr.response(resp, response);
            return;
        }
        m.create();
        String resp = "{\"status\": 200, \"message\": 'doPost 成功', 'response': ''}";
        jsr.response(resp, response);
    }

    public void destroy() {
    }
}