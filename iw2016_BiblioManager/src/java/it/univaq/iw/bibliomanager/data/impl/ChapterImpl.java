/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.data.impl;

import it.univaq.iw.bibliomanager.data.model.BiblioManagerDataLayer;
import it.univaq.iw.bibliomanager.data.model.Chapter;
import it.univaq.iw.bibliomanager.data.model.Section;
import it.univaq.iw.framework.data.DataLayerException;
import java.util.List;

/**
 *
 * @author Vincenzo Lanzieri
 */
public class ChapterImpl implements Chapter {

    private int key;
    private int number;
    private String title;
    private int publication_key;
    private List<Section> sections;
    protected BiblioManagerDataLayer ownerDataLayer;

    public ChapterImpl(BiblioManagerDataLayer ownerDataLayer) {
        this.ownerDataLayer = ownerDataLayer;
        this.key = 0;
        this.number = 0;
        this.title = "";
        publication_key = 0;
        sections = null;
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
    public int getPublicationKey() {
        return publication_key;
    }

    @Override
    public void setPublicationKey(int publication_key) {
        this.publication_key = publication_key;
    }

    @Override
    public List<Section> getSections() throws DataLayerException {
        return sections;
    }

    @Override
    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    @Override
    public void copyFrom(Chapter chapter) throws DataLayerException {
        key = chapter.getKey();
        number = chapter.getNumber();
        title = chapter.getTitle();
        publication_key = chapter.getPublicationKey();
    }
}
