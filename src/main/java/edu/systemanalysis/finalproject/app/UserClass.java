package edu.systemanalysis.finalproject.app;

import org.json.JSONObject;

import java.sql.Timestamp;


public class UserClass {
    private int id = -1, studentId = -1, classId = -1;
    private Timestamp createTime, updateTime;

    private UserClassHelper uch = UserClassHelper.getHelper();

    public UserClass(int studentId, int classId) {
        this.studentId = studentId;
        this.classId = classId;
    }

    public UserClass(int id, int studentId, int classId, Timestamp createTime, Timestamp updateTime) {
        this.id = id;
        this.studentId = studentId;
        this.classId = classId;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public UserClass(int id) {
        this.id = id;
        copy(uch.selectOne(id));
    }

    public JSONObject getData() {
        JSONObject jso = new JSONObject();
        jso.put("id", getId());
        jso.put("student_id", getStudentId());
        jso.put("class_id", getClassId());
        jso.put("create_time", getCreateTime());
        jso.put("update_time", getUpdateTime());
        return jso;
    }

    public void insert() {
        uch.insert(this);
    }

    public void update() {
        uch.update(this);
    }


    public void copy(UserClass c) {
        if (c == null) return;
        this.id = c.id;
        this.studentId = c.studentId;
        this.classId = c.classId;
        this.createTime = c.createTime;
        this.updateTime = c.updateTime;
    }

    public int getId() {
        return id;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }
}
