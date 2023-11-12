package edu.systemanalysis.finalproject.app;

import edu.systemanalysis.finalproject.app.UserHelper;

public class User {
    private int id, role;
    private String email, password, firstName, lastName;

    private UserHelper uh = UserHelper.getHelper();

    public User(String email, String password, String first_name, String last_name, int role) {
        this.email = email;
        this.password = password;
        this.firstName = first_name;
        this.lastName = last_name;
        this.role = role;
    }

    public User(int id) {
        this.id = id;
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

    public String getEmail() {
        return this.email;
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

    public void create() {
        uh.create(this);
        this.id = uh.getIDByEmail(this.email);
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", email='" + email + '\'' + ", password='" + password + '\'' + ", firstName='" + firstName + '\'' + ", lastName='" + lastName + '\'' + ", role=" + role + '}';
    }
}
