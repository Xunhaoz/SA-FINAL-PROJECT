package edu.systemanalysis.finalproject.app;

import edu.systemanalysis.finalproject.util.DBMgr;

import java.sql.*;
import java.sql.Timestamp;
import org.json.*;

public class LoginLogHelper {
    private LoginLogHelper() {

    }

    private static LoginLogHelper llh;
    private Connection conn = null;
    private PreparedStatement pres = null;


    public static LoginLogHelper getHelper() {
        if (llh == null) llh = new LoginLogHelper();
        return llh;
    }
    public void create(LoginLog ll) {
        try {
            conn = DBMgr.getConnection();
            String sql = "INSERT INTO `final_project`.`login_log`(`ip`, `country`, `city`, `operating_system`, `user_id`)" + " VALUES( ?, ?, ?, ?, ?)";
            pres = conn.prepareStatement(sql);
            pres.setString(1, ll.getIp());
            pres.setString(2, ll.getCountry());
            pres.setString(3, ll.getCity());
            pres.setString(4, ll.getOperatingSystem());
            pres.setInt(5, ll.getUserId());
            pres.execute();
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBMgr.close(pres, conn);
        }
    }

    public JSONObject getAll(int userId) {
        LoginLog ll = null;
        JSONArray jsa = new JSONArray();

        ResultSet rs = null;

        try {
            conn = DBMgr.getConnection();
            String sql = "SELECT * FROM `final_project`.`login_log` WHERE `user_id` = ?";
            pres = conn.prepareStatement(sql);
            pres.setInt(1, userId);
            rs = pres.executeQuery();

            while(rs.next()) {
                String ip = rs.getString("ip");
                String country = rs.getString("country");
                String city = rs.getString("city");
                String operatingSystem = rs.getString("operating_system");
                Timestamp createTime = rs.getTimestamp("create_time");

                ll = new LoginLog(ip, country, city, operatingSystem, createTime);
                jsa.put(ll.getData());
            }

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBMgr.close(rs, pres, conn);
        }
        JSONObject response = new JSONObject();
        response.put("data", jsa);
        return response;
    }
}
