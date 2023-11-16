package edu.systemanalysis.finalproject.app;

import edu.systemanalysis.finalproject.util.DBMgr;

import org.json.*;
import edu.systemanalysis.finalproject.tools.JsonReader;
import edu.systemanalysis.finalproject.app.User;

import java.sql.*;

public class UserHelper {
    private UserHelper() {

    }

    private static UserHelper mh;
    private Connection conn = null;
    private PreparedStatement pres = null;


    public static UserHelper getHelper() {
        if (mh == null) mh = new UserHelper();
        return mh;
    }

    public void create(User m) {
        try {
            conn = DBMgr.getConnection();
            String sql = "INSERT INTO `final_project`.`user`(`first_name`, `last_name`, `email`, `password`, `role`, `salt`)" + " VALUES(?, ?, ?, ?, ?, ?)";
            pres = conn.prepareStatement(sql);
            pres.setString(1, m.getFirstName());
            pres.setString(2, m.getLastName());
            pres.setString(3, m.getEmail());
            pres.setString(4, m.getHashPassword());
            pres.setInt(5, m.getRole());
            pres.setString(6, m.getSalt());
            pres.execute();
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBMgr.close(pres, conn);
        }
    }

    public User selectOneByEmail(String email) {
        ResultSet rs = null;
        User u = null;

        try {
            conn = DBMgr.getConnection();
            String sql = "SELECT * FROM `final_project`.`user` WHERE `email` = ? LIMIT 1";
            pres = conn.prepareStatement(sql);
            pres.setString(1, email);
            rs = pres.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String hashPassword = rs.getString("password");
                int role = rs.getInt("role");
                String salt = rs.getString("salt");
                u = new User(id, email, hashPassword, firstName, lastName, role, salt);
            }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBMgr.close(rs, pres, conn);
        }
        return u;
    }

    public User selectOneById(int id) {
        ResultSet rs = null;
        User u = null;

        try {
            conn = DBMgr.getConnection();
            String sql = "SELECT * FROM `final_project`.`user` WHERE `id` = ? LIMIT 1";
            pres = conn.prepareStatement(sql);
            pres.setInt(1, id);
            rs = pres.executeQuery();
            if (rs.next()) {
                String email = rs.getString("email");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String hashPassword = rs.getString("password");
                int role = rs.getInt("role");
                String salt = rs.getString("salt");
                u = new User(id, email, hashPassword, firstName, lastName, role, salt);
            }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBMgr.close(rs, pres, conn);
        }
        return u;
    }

    public boolean checkEmailExists(String email) {
        ResultSet rs = null;
        boolean exists = false;
        try {
            conn = DBMgr.getConnection();
            String sql = "SELECT * FROM `final_project`.`user` WHERE `email` = ? LIMIT 1";
            pres = conn.prepareStatement(sql);
            pres.setString(1, email);
            rs = pres.executeQuery();
            exists = rs.next();
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBMgr.close(rs, pres, conn);
        }
        return exists;
    }

    public void update(User m) {
        try {
            conn = DBMgr.getConnection();
            String sql = "Update `final_project`.`user` SET `email` = ?, `password` = ? , `first_name` = ? , `last_name` = ?, `role` = ? WHERE  `id` = ?";

            pres = conn.prepareStatement(sql);
            pres.setString(1, m.getEmail());
            pres.setString(2, m.getHashPassword());
            pres.setString(3, m.getFirstName());
            pres.setString(4, m.getLastName());
            pres.setInt(5, m.getRole());
            pres.setInt(6, m.getId());
            pres.executeUpdate();
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBMgr.close(pres, conn);
        }

    }
}
