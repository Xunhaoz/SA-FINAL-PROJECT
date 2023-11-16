package edu.systemanalysis.finalproject.app;

import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.core.util.HexUtil;
import edu.systemanalysis.finalproject.app.UserHelper;
import org.json.JSONObject;

import java.security.*;

public class User {
    private int id, role;
    private String email, password, firstName, lastName, hashPassword, salt;

    private UserHelper uh = UserHelper.getHelper();

    public User(String email, String password, String firstName, String lastName, int role) {
        this.email = email;
        this.password = password;
        this.salt = this.genSalt();
        this.hashPassword = DigestUtil.sha256Hex(this.salt + password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    public User(int id, String email, String hashPassword, String firstName, String lastName, int role, String salt) {
        this.id = id;
        this.email = email;
        this.hashPassword = hashPassword;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.salt = salt;
    }

    public User(String email) {
        this.copy(uh.selectOneByEmail(email));
    }

    public String genSalt() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[20];
        secureRandom.nextBytes(salt);
        return HexUtil.encodeHexStr(salt);
    }


    public boolean checkAttrEmpty() {
        if ((this.role != 0 && this.role != 1) || this.email.isEmpty() || this.password.isEmpty() || this.firstName.isEmpty() || this.lastName.isEmpty()) {
            return true;
        }
        return false;
    }

    public boolean checkEmailExist() {
        return uh.checkEmailExists(this.email);
    }

    public boolean checkPasswordCorrect(String password) {return this.hashPassword.equals(DigestUtil.sha256Hex(this.salt + password));}

    public void create() {
        uh.create(this);
        this.copy(uh.selectOneByEmail(this.email));
    }

    public void update() {
        uh.update(this);
        this.copy(uh.selectOneByEmail(this.email));
    }

    public void updateRandomPassword() {
        this.password = genSalt();
        this.hashPassword = DigestUtil.sha256Hex(this.salt + this.password);
        this.uh.update(this);
    }
    public void copy(User u) {
        if (u == null) return;
        this.email = u.email;
        this.salt = u.salt;
        this.hashPassword = u.hashPassword;
        this.firstName = u.firstName;
        this.lastName = u.lastName;
        this.password = u.password;
        this.id = u.id;
    }
    public JSONObject getData() {
        JSONObject jso = new JSONObject();
        jso.put("id", getId());
        jso.put("first_name", getFirstName());
        jso.put("last_name", getLastName());
        jso.put("email", getEmail());
        jso.put("hash_password", getHashPassword());
        jso.put("role", getRole());
        jso.put("salt", getSalt());
        return jso;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        this.hashPassword = DigestUtil.sha256Hex(this.salt + this.password);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getHashPassword() {
        return hashPassword;
    }


    public String getSalt() {
        return salt;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", role=" + role +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", hashPassword='" + hashPassword + '\'' +
                ", salt='" + salt + '\'' +
                '}';
    }
}
