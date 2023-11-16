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

    /**
     * 建立使用者
     */

    public User(String email, String password, String firstName, String lastName, int role) {
        this.email = email;
        this.password = password;
        this.salt = this.genSalt();
        this.hashPassword = DigestUtil.sha256Hex(this.salt + password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    /**
     * 完整物件 from sql
     */
    public User(int id, String email, String hashPassword, String firstName, String lastName, int role, String salt) {
        this.id = id;
        this.email = email;
        this.hashPassword = hashPassword;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.salt = salt;
    }

    /**
     * get all user data
     */
    public User(String email) {
        User u = uh.getAllByEmail(email);
        if (u != null) this.copy(u);
    }


    public String genSalt() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[20];
        secureRandom.nextBytes(salt);
        return HexUtil.encodeHexStr(salt);
    }

    public int getId() {
        return this.id;
    }

    public int getRole() {
        return this.role;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getPassword() {
        return this.password;
    }

    public String getHashPassword() {
        return this.hashPassword;
    }

    public String getEmail() {
        return this.email;
    }

    public String getSalt() {
        if (this.salt.isEmpty()) {
            this.copy(uh.getAllByEmail(this.email));
        }
        return this.salt;
    }

    public boolean checkAttrEmpty() {
        if ((this.role != 0 && this.role != 1) || this.email.isEmpty() || this.password.isEmpty() || this.firstName.isEmpty() || this.lastName.isEmpty()) {
            return true;
        }
        return false;
    }

    public boolean checkEmailDuplicate() {
        return uh.checkEmailDuplicate(this.email);
    }

    public boolean checkEmailExist() {
        return uh.checkEmailExists(this.email);
    }

    public boolean checkPasswordCorrect(String password) {
        return this.hashPassword.equals(DigestUtil.sha256Hex(this.salt + password));
    }

    public void create() {
        uh.create(this);
        this.copy(uh.getAllByEmail(this.email));
    }

    public void update() {
        uh.update(this);
        this.copy(uh.getAllByEmail(this.email));
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public void copy(User u) {
        this.email = u.email;
        this.salt = u.salt;
        this.hashPassword = u.hashPassword;
        this.firstName = u.firstName;
        this.lastName = u.lastName;
        this.password = u.password;
        this.id = u.id;
    }

    public void updateRandomPassword() {
        this.password = getSalt();
        this.hashPassword = DigestUtil.sha256Hex(this.salt + password);
        this.uh.update(this);
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

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", email='" + email + '\'' + ", password='" + password + '\'' + ", firstName='" + firstName + '\'' + ", lastName='" + lastName + '\'' + ", role=" + role + '}';
    }
}
