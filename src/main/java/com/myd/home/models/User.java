package com.myd.home.models;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;


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
    private String name;

    @NotEmpty(message = "Password Required")
    @Size(min = 6, message = "Please make your password at least 6 characters.")
    private String password;

    @NotEmpty(message = "Password Verification Required")
    @Size(min = 6, message = "Please make your password at least 6 characters.")
    private String passwordVerify;

    /**Get the date when user was created**/
    @NotNull
    private Calendar creationDate = Calendar.getInstance();

    @ManyToMany
    private List<Link> services;

    public User() {
    }

    public void addLink(Link service){
        services.add(service);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<Link> getServices() {
        return services;
    }

    public void setServices(List<Link> services) {
        this.services = services;
    }
}
