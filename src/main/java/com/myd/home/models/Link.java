package com.myd.home.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Link {

    @Id
    @GeneratedValue
    private Integer id;

    @NotNull
    private String name;

    @NotNull
    private String url;

    @NotNull
    private String filter;

    @ManyToMany(mappedBy = "services")
    private List<User> serviceUsers;

    public Link(){

    }

    public Link(String name, String url, String filter) {
        this.name = name;
        this.url = url;
        this.filter = filter;
    }

    public Link(String url, String filter) {
        this.url = url;
        this.filter = filter;
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public List<User> getServiceUsers() {
        return serviceUsers;
    }

    public void setServiceUsers(List<User> serviceUsers) {
        this.serviceUsers = serviceUsers;
    }
}
