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
public class ChapterImpl implements IndexElement{
    private int key;
    private int number;
    private String title;
    private Publication publication;
    protected BiblioManagerDataLayer ownerDataLayer;
    
    public ChapterImpl(BiblioManagerDataLayer ownerDataLayer){
        this.ownerDataLayer = ownerDataLayer;
        this.key = 0;
        this.number = 0;
        this.title = "";
        publication = null;
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
        return publication;
    }

    @Override
    public void setPublication(Publication publication) {
        this.publication = publication;
    }

    @Override
    public IndexElement getAncestor() throws DataLayerException {
        //Just do not do anything
        return null;
    }

    @Override
    public void setAncestor(IndexElement ancestor) {
        //Just do not do anything
    }
    
    @Override
    public void copyFrom(IndexElement chapter) throws DataLayerException {
        key = chapter.getKey();
        number = chapter.getNumber();
        title = chapter.getTitle();
        publication = chapter.getPublication();
    }
}
