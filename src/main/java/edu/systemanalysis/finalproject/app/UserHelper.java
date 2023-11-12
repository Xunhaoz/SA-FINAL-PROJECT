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
            String sql = "INSERT INTO `final_project`.`user`(`first_name`, `last_name`, `email`, `password`, `role`)" + " VALUES(?, ?, ?, ?, ?)";
            pres = conn.prepareStatement(sql);
            pres.setString(1, m.getFirstName());
            pres.setString(2, m.getLastName());
            pres.setString(3, m.getEmail());
            pres.setString(4, m.getPassword());
            pres.setInt(5, m.getRole());
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
            /** 印出JDBC SQL指令錯誤 **/
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
            e.printStackTrace();
        } finally {
            /** 關閉連線並釋放所有資料庫相關之資源 **/
            DBMgr.close(rs, pres, conn);
        }
        return id;
    }
}
