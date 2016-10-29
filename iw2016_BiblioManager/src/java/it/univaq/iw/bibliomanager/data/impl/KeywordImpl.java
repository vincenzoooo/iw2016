/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.data.impl;

import it.univaq.iw.bibliomanager.data.model.BiblioManagerDataLayer;
import it.univaq.iw.framework.data.DataLayerException;
import it.univaq.iw.bibliomanager.data.model.Keyword;

/**
 *
 * @author Vincenzo Lanzieri
 */
public class KeywordImpl implements Keyword {

    private int key;
    private String name;
    private boolean dirty;
    protected BiblioManagerDataLayer ownerDataLayer;

    public KeywordImpl(BiblioManagerDataLayer ownerDataLayer) {
        this.ownerDataLayer = ownerDataLayer;
        key = 0;
        name = "";
        dirty = false;
    }

    @Override
    public int getKey() {
        return key;
    }

    @Override
    public void setKey(int key) {
        this.key = key;
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
    public boolean isDirty() {
        return dirty;
    }

    @Override
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    @Override
    public void copyFrom(Keyword keyword) throws DataLayerException {
        key = keyword.getKey();
        name = keyword.getName();
        dirty = false;
    }
}
