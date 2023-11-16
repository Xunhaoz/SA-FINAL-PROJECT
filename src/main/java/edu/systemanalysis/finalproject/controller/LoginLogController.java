package edu.systemanalysis.finalproject.controller;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import edu.systemanalysis.finalproject.app.LoginLog;
import edu.systemanalysis.finalproject.app.LoginLogHelper;
import edu.systemanalysis.finalproject.app.User;
import edu.systemanalysis.finalproject.tools.JsonReader;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@WebServlet(name = "log", value = "/log")
public class LoginLogController extends HttpServlet {
    LoginLogHelper llh = LoginLogHelper.getHelper();

    public void init() {

    }


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JsonReader jsr = new JsonReader(request);
        String emailString = request.getHeader("email");

        User u = new User(emailString);

        JSONObject resp = new JSONObject();
        resp.put("status", 200);
        resp.put("message", "資料獲取成功");
        resp.put("response", llh.getAll(u.getId()));

        jsr.response(resp, response);
    }

    public void destroy() {
    }
}
