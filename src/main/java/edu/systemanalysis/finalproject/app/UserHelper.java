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


    public User getAllByEmail(String email) {
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

    public void update(User m) {
        try {
            conn = DBMgr.getConnection();
            String sql = "Update `final_project`.`user` SET `email` = ?, `password` = ? , `first_name` = ? , `last_name` = ? WHERE  `id` = ?";

            pres = conn.prepareStatement(sql);
            pres.setString(1, m.getEmail());
            pres.setString(2, m.getPassword());
            pres.setString(3, m.getFirstName());
            pres.setString(4, m.getLastName());
            pres.setInt(5, m.getId());
            pres.executeUpdate();
        } catch (SQLException e) {
            /** 印出JDBC SQL指令錯誤 **/
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
            e.printStackTrace();
        } finally {
            /** 關閉連線並釋放所有資料庫相關之資源 **/
            DBMgr.close(pres, conn);
        }

    }
}
