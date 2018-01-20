package com.myd.home.models;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import sun.security.util.Password;

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
    private int id;

    @NotEmpty(message = "Email Required.")
    @Email
    private String email;

    @NotEmpty(message = "User Name Required")
    private String userName;

    @NotEmpty(message = "Password Required")
    @Size(min = 6, message = "Please make your password at least 6 characters. - Thanks, Password Goblin")
    private Password password;

    /**Get the date when user was created**/
    @NotNull
    private Calendar creationDate = Calendar.getInstance();

    public User(String email, String userName, Password password) {
        this.email = email;
        this.userName = userName;
        this.password = password;
    }

    public User(){}

    public int getId() {
        return id;
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

    public Password getPassword() {
        return password;
    }

    public void setPassword(Password password) {
        this.password = password;
    }

    public Calendar getCreationDate() {
        return creationDate;
    }
}
