package com.github.dbourdette.otto.service.user;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotEmpty;

import com.github.dbourdette.otto.web.util.Constants;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Property;

/**
 * @author damien bourdette
 */
@Entity(Constants.USERS)
public class User {
    @Id
    private ObjectId id;

    @NotEmpty
    @Property
    private String username;

    @Property
    private String password;

    @Property
    private boolean admin;

    @Property
    private String sources;

    public List<String> getSourcesAsList() {
        return Arrays.asList(StringUtils.split(sources, ","));
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSources() {
        return sources;
    }

    public void setSources(String sources) {
        this.sources = sources;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "User{" +
                "admin=" + admin +
                ", id=" + id +
                ", username='" + username + '\'' +
                ", password='***'" +
                ", sources='" + sources + '\'' +
                '}';
    }


}
