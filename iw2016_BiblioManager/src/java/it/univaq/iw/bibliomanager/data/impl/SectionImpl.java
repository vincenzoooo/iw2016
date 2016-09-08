/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.data.impl;

import it.univaq.iw.bibliomanager.data.model.BiblioManagerDataLayer;
import it.univaq.iw.bibliomanager.data.model.IndexElement;
import it.univaq.iw.bibliomanager.data.model.Publication;
import it.univaq.iw.framework.data.DataLayerException;
import java.util.List;

/**
 *
 * @author Vincenzo Lanzieri
 */
public class SectionImpl implements IndexElement{
    private int key;
    private int number;
    private String title;
    private IndexElement chapter;
    protected BiblioManagerDataLayer ownerDataLayer;
    
    public SectionImpl(BiblioManagerDataLayer ownerDataLayer){
        this.ownerDataLayer = ownerDataLayer;
        this.key = 0;
        this.number = 0;
        this.title = "";
        chapter = null;
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
    public int getNumber() {
        return number;
    }

    @Override
    public void setNumber(int number) {
        this.number = number;
    }
    
    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public Publication getPublication() throws DataLayerException {
        //Just not do anything
        return null;
    }

    @Override
    public void setPublication(Publication publication) {
        //Just not do anything
    }

    @Override
    public IndexElement getAncestor() throws DataLayerException {
        return chapter;
    }

    @Override
    public void setAncestor(IndexElement ancestor) {
        this.chapter = ancestor;
    }
    
    @Override
    public void copyFrom(IndexElement section) throws DataLayerException {
        key = section.getKey();
        number = section.getNumber();
        title = section.getTitle();
        chapter = section.getAncestor();
    }
}
