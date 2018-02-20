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

    @NotEmpty(message = "Email Required.")
    @Email
    private String email;

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
