package com.github.dbourdette.otto.service.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.github.dbourdette.otto.web.util.Constants;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Property;

/**
 * @author damien bourdette
 */
@Entity(Constants.USERS)
public class User implements Serializable {
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
        String[] array = StringUtils.split(sources, ",");

        if (array == null) {
            return new ArrayList<String>();
        }

        return Arrays.asList(array);
    }

    public List<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

        if (isAdmin()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        for (String source : getSourcesAsList()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_SOURCE_" + source.toUpperCase() + "_USER"));
        }

        return authorities;
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
