package edu.systemanalysis.finalproject.app;

import edu.systemanalysis.finalproject.util.DBMgr;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;

public class UserClassHelper {
    private UserClassHelper() {

    }

    private static UserClassHelper ch;
    private Connection conn = null;
    private PreparedStatement pres = null;


    public static UserClassHelper getHelper() {
        if (ch == null) ch = new UserClassHelper();
        return ch;
    }

    public void insert(UserClass c) {
        try {
            conn = DBMgr.getConnection();
            String sql = "INSERT INTO `final_project`.`user_class`(`student_id`, `class_id`) VALUES(?, ?)";
            pres = conn.prepareStatement(sql);
            pres.setInt(1, c.getStudentId());
            pres.setInt(2, c.getClassId());
            pres.execute();
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBMgr.close(pres, conn);
        }
    }

    public JSONObject selectAllByStudentId(int studentId) {
        ResultSet rs = null;
        JSONArray jsa = new JSONArray();
        UserClass c = null;

        try {
            conn = DBMgr.getConnection();
            String sql = "SELECT * FROM `final_project`.`user_class` WHERE `student_id` = ?";
            pres = conn.prepareStatement(sql);
            pres.setInt(1, studentId);
            rs = pres.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                int classId = rs.getInt("class_id");
                Timestamp createTime = rs.getTimestamp("create_time");
                Timestamp updateTime = rs.getTimestamp("update_time");

                c = new UserClass(id, studentId, classId, createTime, updateTime);
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

    public JSONObject selectAllByTeacherId(int teacherId) {
        ResultSet rs = null;
        JSONArray jsa = new JSONArray();
        UserClass uc = null;

        try {
            conn = DBMgr.getConnection();
            String sql = "SELECT * FROM `final_project`.`user_class` uc JOIN `final_project`.`class` c ON uc.class_id = c.id WHERE c.teacher_id = ?;";
            pres = conn.prepareStatement(sql);
            pres.setInt(1, teacherId);
            rs = pres.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                int studentId = rs.getInt("student_id");
                int classId = rs.getInt("class_id");
                Timestamp createTime = rs.getTimestamp("create_time");
                Timestamp updateTime = rs.getTimestamp("update_time");

                uc = new UserClass(id, studentId, classId, createTime, updateTime);
                jsa.put(uc.getData());
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

    public UserClass selectOne(int id) {
        ResultSet rs = null;
        UserClass uc = null;

        try {
            conn = DBMgr.getConnection();
            String sql = "SELECT * FROM `final_project`.`user_class` uc JOIN `final_project`.`class` c ON uc.class_id = c.id WHERE c.teacher_id = ?;";
            pres = conn.prepareStatement(sql);
            pres.setInt(1, id);
            rs = pres.executeQuery();

            if (rs.next()) {
                int studentId = rs.getInt("student_id");
                int classId = rs.getInt("class_id");
                Timestamp createTime = rs.getTimestamp("create_time");
                Timestamp updateTime = rs.getTimestamp("update_time");

                uc = new UserClass(id, studentId, classId, createTime, updateTime);
            }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBMgr.close(rs, pres, conn);
        }

        return uc;
    }

    public JSONObject selectAll() {
        ResultSet rs = null;
        UserClass uc = null;
        JSONArray jsa = new JSONArray();

        try {
            conn = DBMgr.getConnection();
            String sql = "SELECT * FROM `final_project`.`user_class`";
            pres = conn.prepareStatement(sql);
            rs = pres.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                int studentId = rs.getInt("student_id");
                int classId = rs.getInt("class_id");
                Timestamp createTime = rs.getTimestamp("create_time");
                Timestamp updateTime = rs.getTimestamp("update_time");

                uc = new UserClass(id, studentId, classId, createTime, updateTime);
                jsa.put(uc.getData());
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

    public void update(UserClass uc) {
        try {
            conn = DBMgr.getConnection();
            String sql = "UPDATE `final_project`.`class` SET `student_id` = ?, `class_id`= ? WHERE `id` = ?";
            pres = conn.prepareStatement(sql);
            pres.setInt(1, uc.getStudentId());
            pres.setInt(2, uc.getClassId());
            pres.setInt(3, uc.getId());
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
