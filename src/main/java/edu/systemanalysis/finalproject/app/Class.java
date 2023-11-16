package edu.systemanalysis.finalproject.app;

import org.json.JSONObject;
import java.sql.*;
import cn.hutool.core.date.*;


public class Class {
    private int id = -1, teacherId = -1;
    private String content, topic;

    private Timestamp midtermTime, finalTime, createTime;

    private ClassHelper ch = ClassHelper.getHelper();


    public Class(int id) {
        this.id = id;
        copy(ch.selectOne(id));
    }

    public Class(int teacherId, String content, String topic, Timestamp midtermTime, Timestamp finalTime) {
        this.teacherId = teacherId;
        this.content = content;
        this.topic = topic;
        this.midtermTime = midtermTime;
        this.finalTime = finalTime;
    }

    public Class(int id, int teacherId, String content, String topic, Timestamp midtermTime, Timestamp finalTime, Timestamp createTime) {
        this.id = id;
        this.teacherId = teacherId;
        this.content = content;
        this.topic = topic;
        this.midtermTime = midtermTime;
        this.finalTime = finalTime;
        this.createTime = createTime;
    }

    public JSONObject getData() {
        JSONObject jso = new JSONObject();
        jso.put("id", getId());
        jso.put("teacher_id", getTeacherId());
        jso.put("content", getContent());
        jso.put("topic", getTopic());
        jso.put("midtermTime", getMidtermTime());
        jso.put("finalTime", getFinalTime());
        jso.put("createTime", getCreateTime());
        return jso;
    }

    public void insert() {
        ch.insert(this);
    }

    public void update() {
        ch.update(this);
    }

    public void copy(Class c) {
        if (c == null) return;
        this.id = c.id;
        this.teacherId = c.teacherId;
        this.content = c.content;
        this.topic = c.topic;
        this.midtermTime = c.midtermTime;
        this.finalTime = c.finalTime;
    }

    public int getId() {
        return id;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public String getContent() {
        return content;
    }

    public String getTopic() {
        return topic;
    }

    public Timestamp getMidtermTime() {
        return midtermTime;
    }

    public Timestamp getFinalTime() {
        return finalTime;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setMidtermTime(Timestamp midtermTime) {
        this.midtermTime = midtermTime;
    }

    public void setFinalTime(Timestamp finalTime) {
        this.finalTime = finalTime;
    }
}
