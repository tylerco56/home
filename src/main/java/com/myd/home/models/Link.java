package com.myd.home.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Set;

@Entity
public class Link {

    @Id
    @GeneratedValue
    private Integer id;

    @NotNull
    private String subject;

    @NotNull
    private String url;

    @NotNull
    private String filter;

    public Link(){

    }

    public Link(String subject, String url, String filter) {
        this.subject = subject;
        this.url = url;
        this.filter = filter;
    }

    public Link(String url, String filter) {
        this.url = url;
        this.filter = filter;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
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

}
