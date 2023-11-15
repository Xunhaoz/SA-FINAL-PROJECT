package edu.systemanalysis.finalproject.app;

import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.core.util.HexUtil;
import edu.systemanalysis.finalproject.app.UserHelper;

import java.security.*;

public class User {
    private int id, role;
    private String email, password, firstName, lastName, hashPassword, salt;

    private UserHelper uh = UserHelper.getHelper();

    /**
     * 建立使用者
     */
    public User(String email, String password, String first_name, String last_name, int role) {
        this.email = email;
        this.password = password;
        this.salt = this.genSalt();
        this.hashPassword = DigestUtil.sha256Hex(this.salt + password);
        this.firstName = first_name;
        this.lastName = last_name;
        this.role = role;
    }

    /**
     * 使用者登入
     */
    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.id = uh.getIDByEmail(this.email);
        this.salt = uh.getSaltByEmail(this.email);
        this.hashPassword = DigestUtil.sha256Hex(this.salt + password);
    }

    public User(String email) {
        this.email = email;
        this.id = uh.getIDByEmail(this.email);
        this.salt = uh.getSaltByEmail(this.email);
        this.hashPassword = DigestUtil.sha256Hex(this.salt + password);
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
            this.salt = uh.getSaltByEmail(this.email);
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

    public boolean checkPasswordCorrect() {
        return this.hashPassword.equals(uh.getHashPasswordByEmail(this.email));
    }

    public void create() {
        uh.create(this);
        this.id = uh.getIDByEmail(this.email);
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", email='" + email + '\'' + ", password='" + password + '\'' + ", firstName='" + firstName + '\'' + ", lastName='" + lastName + '\'' + ", role=" + role + '}';
    }
}
