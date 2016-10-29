/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.data.impl;

import it.univaq.iw.bibliomanager.data.model.BiblioManagerDataLayer;
import it.univaq.iw.bibliomanager.data.model.Section;
import it.univaq.iw.framework.data.DataLayerException;

/**
 *
 * @author Vincenzo Lanzieri
 */
public class SectionImpl implements Section {

    private int key;
    private int number;
    private String title;
    private int chapter_key;
    private boolean dirty;
    protected BiblioManagerDataLayer ownerDataLayer;

    public SectionImpl(BiblioManagerDataLayer ownerDataLayer) {
        this.ownerDataLayer = ownerDataLayer;
        this.key = 0;
        this.number = 0;
        this.title = "";
        chapter_key = 0;
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
    public int getChapterKey() {
        return chapter_key;
    }

    @Override
    public void setChapterKey(int chapter_key) {
        this.chapter_key = chapter_key;
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
    public void copyFrom(Section section) throws DataLayerException {
        key = section.getKey();
        number = section.getNumber();
        title = section.getTitle();
        chapter_key = section.getChapterKey();
        dirty = false;
    }
}
