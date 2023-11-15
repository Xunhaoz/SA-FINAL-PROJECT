package edu.systemanalysis.finalproject.app;

import edu.systemanalysis.finalproject.util.DBMgr;

import org.json.*;
import edu.systemanalysis.finalproject.tools.JsonReader;
import edu.systemanalysis.finalproject.app.User;

import java.sql.*;
import java.time.LocalDateTime;

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

    public boolean checkEmailDuplicate(String email) {
        int row = -1;
        ResultSet rs = null;
        try {
            conn = DBMgr.getConnection();
            String sql = "SELECT count(*) FROM `final_project`.`user` WHERE `email` = ?";
            pres = conn.prepareStatement(sql);
            pres.setString(1, email);
            rs = pres.executeQuery();
            rs.next();
            row = rs.getInt("count(*)");
            System.out.print(row);

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBMgr.close(rs, pres, conn);
        }
        return row != 0;
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

    public int getIDByEmail(String email) {
        ResultSet rs = null;
        int id = -1;

        try {
            conn = DBMgr.getConnection();
            String sql = "SELECT * FROM `final_project`.`user` WHERE `email` = ? LIMIT 1";
            pres = conn.prepareStatement(sql);
            pres.setString(1, email);
            rs = pres.executeQuery();
            if (rs.next()) {
                id = rs.getInt("id");
            }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBMgr.close(rs, pres, conn);
        }
        return id;
    }

    public String getSaltByEmail(String email) {
        ResultSet rs = null;
        String salt = "";

        try {
            conn = DBMgr.getConnection();
            String sql = "SELECT * FROM `final_project`.`user` WHERE `email` = ? LIMIT 1";
            pres = conn.prepareStatement(sql);
            pres.setString(1, email);
            rs = pres.executeQuery();
            if (rs.next()) {
                salt = rs.getString("salt");
            }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBMgr.close(rs, pres, conn);
        }
        return salt;
    }

    public String getHashPasswordByEmail(String email) {
        ResultSet rs = null;
        String hashPassword = "";

        try {
            conn = DBMgr.getConnection();
            String sql = "SELECT * FROM `final_project`.`user` WHERE `email` = ? LIMIT 1";
            pres = conn.prepareStatement(sql);
            pres.setString(1, email);
            rs = pres.executeQuery();
            if (rs.next()) {
                hashPassword = rs.getString("password");
            }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBMgr.close(rs, pres, conn);
        }
        return hashPassword;
    }

    public boolean checkEmailExists(String email) {
        ResultSet rs = null;
        try {
            conn = DBMgr.getConnection();
            String sql = "SELECT * FROM `final_project`.`user` WHERE `email` = ? LIMIT 1";
            pres = conn.prepareStatement(sql);
            pres.setString(1, email);
            rs = pres.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBMgr.close(rs, pres, conn);
        }
        return false;
    }
}
