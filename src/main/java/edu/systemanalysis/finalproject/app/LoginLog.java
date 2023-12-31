package edu.systemanalysis.finalproject.app;

import cn.hutool.crypto.digest.DigestUtil;

import org.json.*;

import java.sql.Timestamp;

public class LoginLog {
    private int id;
    private String ip;
    private String country;
    private String city;
    private String operatingSystem;
    private Timestamp createTime;
    private int userId;

    private LoginLogHelper llh = LoginLogHelper.getHelper();

    public LoginLog(String ip, String country, String city, String operatingSystem, int userId) {
        this.ip = ip;
        this.country = country;
        this.city = city;
        this.operatingSystem = operatingSystem;
        this.userId = userId;
    }

    public LoginLog(String ip, String country, String city, String operatingSystem, Timestamp createTime) {
        this.ip = ip;
        this.country = country;
        this.city = city;
        this.operatingSystem = operatingSystem;
        this.createTime = createTime;
    }

    public LoginLog(int userId) {
        this.userId = userId;

    }

    public void create() {
        llh.create(this);
    }

    public int getId() {
        return id;
    }

    public String getIp() {
        return ip;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public int getUserId() {
        return userId;
    }

    public JSONObject getData() {
        JSONObject jso = new JSONObject();
        jso.put("ip", getIp());
        jso.put("country", getCountry());
        jso.put("city", getCity());
        jso.put("create_time", getCreateTime());
        jso.put("operating_system", getOperatingSystem());
        return jso;
    }
}
