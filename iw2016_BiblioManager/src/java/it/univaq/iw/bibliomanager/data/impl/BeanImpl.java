/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.data.impl;

/**
 *
 * @author Vincenzo Lanzieri
 */
public abstract class BeanImpl {
    private int key;

    public BeanImpl(){
        this.key = 0;
    }
    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }
    
}
