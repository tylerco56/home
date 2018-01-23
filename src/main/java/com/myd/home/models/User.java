package com.myd.home.models;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Calendar;


/**
 * Created by: Tyler Langenfeld
 */



@Entity
public class User {

    @Id
    @GeneratedValue
    private Integer id;

    @NotEmpty(message = "Please enter your first name.")
    private String firstName;

    @NotEmpty(message = "Please enter your last name")
    private String lastName;

    @NotEmpty(message = "Email Required.")
    @Email
    private String email;

    @NotEmpty(message = "User Name Required")
    private String userName;

    @NotEmpty(message = "Password Required")
    @Size(min = 6, message = "Please make your password at least 6 characters. - Thanks, Password Goblin")
    private String password;

    @NotEmpty(message = "Password Verification Required")
    @Size(min = 6, message = "Please make your password at least 6 characters. - Thanks, Password Goblin")
    private String passwordVerify;

    /**Get the date when user was created**/
    @NotNull
    private Calendar creationDate = Calendar.getInstance();

    public Integer getId() {
        return id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordVerify() {
        return passwordVerify;
    }

    public void setPasswordVerify(String passwordVerify) {
        this.passwordVerify = passwordVerify;
    }

    public Calendar getCreationDate() {
        return creationDate;
    }
}
