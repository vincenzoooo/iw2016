/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.data.impl;

import it.univaq.iw.bibliomanager.data.model.BiblioManagerDataLayer;
import it.univaq.iw.bibliomanager.data.model.User;

/**
 *
 * @author Vincenzo Lanzieri
 */
public class UserImpl extends BeanImpl implements User{    
    private String name;
    private String surname;
    private String password;
    private String email;
    private int state;
    protected BiblioManagerDataLayer ownerDataLayer;

    public UserImpl(BiblioManagerDataLayer ownerDataLayer) {
        super();
        this.ownerDataLayer = ownerDataLayer;        
        name = "";
        surname = "";
        password = "";
        email = "";
        state = 2;              
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getSurname() {
        return surname;
    }

    @Override
    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getEmail() {
        return email;      
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int getState() {
        return state;
    }

    @Override
    public void setState(int state) {
        this.state = state;
    }
    
    
}
