package com.saintgobain.dsi.starter.security.wsip.entity;

import java.util.Set;

/**
 * The type Token info.
 */
public class TokenInfo {

    private String email;
    private Set<String> accessRights;
    private long expires;

    /**
     * Gets email.
     * 
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets email.
     * 
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets accessRights.
     *
     * @return the accessRights
     */
    public Set<String> getAccessRights() {
        return accessRights;
    }

    /**
     * Sets accessRights.
     *
     * @param accessRights the accessRights
     */
    public void setAccessRights(Set<String> accessRights) {
        this.accessRights = accessRights;
    }

    /**
     * Gets expires.
     *
     * @return the expires
     */
    public long getExpires() {
        return expires;
    }

    /**
     * Sets expires.
     *
     * @param expires the expires
     */
    public void setExpires(long expires) {
        this.expires = expires;
    }
}
