/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.data.impl;

import it.univaq.iw.bibliomanager.data.model.BiblioManagerDataLayer;
import it.univaq.iw.bibliomanager.data.model.Editor;

/**
 *
 * @author Vincenzo Lanzieri
 */
public class EditorImpl implements Editor{
    private int key;
    private String name;    
    protected BiblioManagerDataLayer ownerDataLayer;

    public EditorImpl(BiblioManagerDataLayer ownerDataLayer) {        
        this.ownerDataLayer = ownerDataLayer;
        key = 0;
        name = "";        
    }

    @Override
    public int getKey() {
        return key;
    }

    @Override
    public String getName(){
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
    
}
