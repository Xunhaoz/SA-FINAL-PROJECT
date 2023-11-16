package edu.systemanalysis.finalproject.app;

import edu.systemanalysis.finalproject.util.DBMgr;
import org.json.*;
import java.sql.*;

public class ClassHelper {
    private ClassHelper() {

    }

    private static ClassHelper ch;
    private Connection conn = null;
    private PreparedStatement pres = null;


    public static ClassHelper getHelper() {
        if (ch == null) ch = new ClassHelper();
        return ch;
    }

    public void insert(Class c) {
        try {
            conn = DBMgr.getConnection();
            String sql = "INSERT INTO `final_project`.`class`(`teacher_id`, `content`, `topic`, `midterm_time`, `final_time`)" + " VALUES(?, ?, ?, ?, ?)";
            pres = conn.prepareStatement(sql);
            pres.setInt(1, c.getTeacherId());
            pres.setString(2, c.getContent());
            pres.setString(3, c.getTopic());
            pres.setTimestamp(4, c.getMidtermTime());
            pres.setTimestamp(5, c.getFinalTime());
            pres.execute();
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBMgr.close(pres, conn);
        }
    }

    public JSONObject selectAll() {
        ResultSet rs = null;
        JSONArray jsa = new JSONArray();
        Class c = null;

        try {
            conn = DBMgr.getConnection();
            String sql = "SELECT * FROM `final_project`.`class`";
            pres = conn.prepareStatement(sql);

            rs = pres.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                int teacherId = rs.getInt("teacher_id");
                String content = rs.getString("content");
                String topic = rs.getString("topic");
                Timestamp midtermTime = rs.getTimestamp("midterm_time");
                Timestamp finalTime = rs.getTimestamp("final_time");
                Timestamp createTime = rs.getTimestamp("create_time");

                c = new Class(id, teacherId, content, topic, midtermTime, finalTime, createTime);
                jsa.put(c.getData());
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

    public Class selectOne(int id) {
        ResultSet rs = null;
        Class c = null;

        try {
            conn = DBMgr.getConnection();
            String sql = "SELECT * FROM `final_project`.`class` WHERE `id` = ?";
            pres = conn.prepareStatement(sql);
            pres.setInt(1, id);
            rs = pres.executeQuery();

            if (rs.next()) {
                int teacherId = rs.getInt("teacher_id");
                String content = rs.getString("content");
                String topic = rs.getString("topic");
                Timestamp midtermTime = rs.getTimestamp("midterm_time");
                Timestamp finalTime = rs.getTimestamp("final_time");
                Timestamp createTime = rs.getTimestamp("create_time");
                c = new Class(id, teacherId, content, topic, midtermTime, finalTime, createTime);
            }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBMgr.close(rs, pres, conn);
        }

        return c;
    }

    public void update(Class c) {
        try {
            conn = DBMgr.getConnection();
            String sql = "UPDATE `final_project`.`class` SET `teacher_id` = ?, `content`= ?, `topic`= ?, `midterm_time`= ?, `final_time`= ? WHERE `id` = ?";
            pres = conn.prepareStatement(sql);
            pres.setInt(1, c.getTeacherId());
            pres.setString(2, c.getContent());
            pres.setString(3, c.getTopic());
            pres.setTimestamp(4, c.getMidtermTime());
            pres.setTimestamp(5, c.getFinalTime());
            pres.setInt(6, c.getId());
            pres.execute();
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBMgr.close(pres, conn);
        }
    }
}
