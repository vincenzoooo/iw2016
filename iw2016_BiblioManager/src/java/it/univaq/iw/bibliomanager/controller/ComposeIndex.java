/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.controller;

import it.univaq.iw.bibliomanager.data.model.Chapter;
import it.univaq.iw.bibliomanager.data.model.Section;
import it.univaq.iw.framework.data.DataLayerException;
import it.univaq.iw.framework.result.TemplateResult;
import it.univaq.iw.framework.security.SecurityLayer;
import it.univaq.iw.framework.utils.Utils;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Vincenzo Lanzieri, Angelo Iezzi
 */
public class ComposeIndex extends BiblioManagerBaseController {

    private int publicationId;
    private String url;
    private int chapterId;
    private int sectorId;
    
    private void action_composeChapter(HttpServletRequest request, HttpServletResponse response) {
        try {
            Chapter chapter = getDataLayer().createChapter();
            Map<String, String> params = new HashMap<String, String>();
            params.put("chapterTitle", Utils.checkString(request.getParameter("chapterTitle")));
            params.put("chapterNumber", Utils.checkString(request.getParameter("chapterNumber")));
            if (!validator(params, request, response)) {
                chapter.setNumber(Integer.parseInt(params.get("chapterNumber")));
                chapter.setTitle(params.get("chapterTitle"));
                chapter.setPublicationKey(publicationId);
                getDataLayer().storeChapter(chapter);
                request.setAttribute("chapterAdded",1);
            }
        } catch (DataLayerException ex) {
            action_error(request, response, "Errore nel salvare il capitolo: " + ex.getMessage(), 510);
        }
    }

    private void action_updateChapter(HttpServletRequest request, HttpServletResponse response) {
        try {
            HttpSession session = SecurityLayer.checkSession(request);
            Chapter chapter = getDataLayer().getChapter(chapterId);
            Map<String, String> params = new HashMap<String, String>();
            params.put("chapterTitle", Utils.checkString(request.getParameter("chapterTitle")));
            params.put("chapterNumber", Utils.checkString(request.getParameter("chapterNumber")));
            if (!validator(params, request, response)) {
                chapter.setNumber(Integer.parseInt(params.get("chapterNumber")));
                chapter.setTitle(params.get("chapterTitle"));
                getDataLayer().storeChapter(chapter);
                request.setAttribute("chapterUpdated",1);
                chapterId = 0;
            }
        } catch (DataLayerException ex) {
            action_error(request, response, "Errore nel salvare il capitolo: " + ex.getMessage(), 510);
        }
    }

    private void action_composeSection(HttpServletRequest request, HttpServletResponse response) {
        try {
            Section section = getDataLayer().createSection();
            Map<String, String> params = new HashMap<String, String>();
            params.put("chapter", Utils.checkString(request.getParameter("chapter")));
            params.put("sectionTitle", Utils.checkString(request.getParameter("sectionTitle")));
            params.put("sectionNumber", Utils.checkString(request.getParameter("sectionNumber")));
            if (!validator(params, request, response)) {
                section.setNumber(Integer.parseInt(params.get("sectionNumber")));
                section.setTitle(params.get("sectionTitle"));
                section.setChapterKey(Integer.parseInt(params.get("chapter")));
                getDataLayer().storeSection(section);
                request.setAttribute("sectionUpdated",1);
            }
        } catch (DataLayerException ex) {
            action_error(request, response, "Errore nel salvare il sezione: " + ex.getMessage(), 510);
        }
    }

    private void action_updateSection(HttpServletRequest request, HttpServletResponse response) {
        try {
            Section section = getDataLayer().getSection(sectorId);
            Map<String, String> params = new HashMap<String, String>();
            params.put("chapter", Utils.checkString(request.getParameter("chapter")));
            params.put("sectionTitle", Utils.checkString(request.getParameter("sectionTitle")));
            params.put("sectionNumber", Utils.checkString(request.getParameter("sectionNumber")));
            if (!validator(params, request, response)) {
                section.setNumber(Integer.parseInt(params.get("sectionNumber")));
                section.setTitle(params.get("sectionTitle"));
                section.setChapterKey(Integer.parseInt(params.get("chapter")));
                getDataLayer().storeSection(section);
                sectorId = 0;
            }
        } catch (DataLayerException ex) {
            action_error(request, response, "Errore nel salvare il sezione: " + ex.getMessage(), 510);
        }
    }

    @Override
    protected boolean validator(Map<String, String> params, HttpServletRequest request, HttpServletResponse response) {
        boolean error = super.validator(params, request, response);
        if (!error) {
            try {
                if (!Utils.isNullOrEmpty(params.get("chapterNumber")) && !Utils.isNumeric(params.get("chapterNumber"))) {
                    request.setAttribute("errorChapterNumber", "Non è un numero valido");
                    error = true;
                }
                if (!Utils.isNullOrEmpty(params.get("sectionNumber")) && !Utils.isNumeric(params.get("sectionNumber"))) {
                    request.setAttribute("errorSectionNumber", "Non è un numero valido");
                    error = true;
                }
                if (!error && !Utils.isNullOrEmpty(params.get("chapterNumber"))) {
                    List<Chapter> elements = getDataLayer().getChapters(publicationId);
                    for (Chapter element : elements) {
                        if (Integer.parseInt(params.get("chapterNumber")) == element.getNumber()) {
                            request.setAttribute("errorChapterNumber", "Numero capitolo già creato");
                            error = true;
                        }
                    }
                }
                if (!error && !Utils.isNullOrEmpty(params.get("sectionNumber"))) {
                    List<Section> elements = getDataLayer().getSections(Integer.parseInt(params.get("chapter")));
                    for (Section element : elements) {
                        if (Integer.parseInt(params.get("sectionNumber")) == element.getNumber()) {
                            request.setAttribute("errorSectionNumber", "Numero sezione già creato");
                            error = true;
                        }
                    }
                }

            } catch (DataLayerException ex) {
                action_error(request, response, "Error to get chapter or section: " + ex.getMessage(), 502);
            }
        }
        return error;
    }

    private void action_deleteChapter(HttpServletRequest request, HttpServletResponse response) throws DataLayerException{
        Chapter chapter = getDataLayer().getChapter((int)request.getAttribute("idChapter"));
        if(chapter.getSections().size()>0){
            for (Section section : chapter.getSections()) {
                request.setAttribute("idSection", section.getKey());
            }
        }
        getDataLayer().deleteChapter(chapter);
    }
    
    private void action_deleteSection(HttpServletRequest request, HttpServletResponse response) throws DataLayerException{
        Section section = getDataLayer().getSection((int)request.getAttribute("idSection"));
        getDataLayer().deleteSection(section);
    }
    
    private void action_view(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DataLayerException {
        request.setAttribute("page_title", "Gestione Indice");
        TemplateResult res = new TemplateResult(getServletContext());
        List<Chapter> chapters = getDataLayer().getChapters(publicationId);
        request.setAttribute("publicationId", publicationId);
        request.setAttribute("url", url);
        request.setAttribute("chapters", chapters);
        res.activate("index.ftl.html", request, response);
    }
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     */
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        try {
            HttpSession session = SecurityLayer.checkSession(request);
            if (session != null) {
                currentUser(request, response, session);
                if(request.getAttribute("publicationId") != null){
                    publicationId = (int) request.getAttribute("publicationId");
                }
                if(request.getAttribute("url") != null){
                    url = (String) request.getAttribute("url");
                }
                if (request.getParameter("chapterId") != null){
                    chapterId = Integer.parseInt(request.getParameter("chapterId"));
                }
                if (request.getParameter("sectionId") != null){
                    sectorId = Integer.parseInt(request.getParameter("sectionId"));
                }
                if (request.getParameter("submitChapter") != null) {
                    if(chapterId == 0){
                    action_composeChapter(request, response);
                    }
                    else{
                        action_updateChapter(request, response);
                    }
                }
                if (request.getParameter("submitSection") != null) {
                    if(sectorId == 0){
                    action_composeSection(request, response);
                    }
                    else{
                        action_updateSection(request, response);
                    }
                }
                if(request.getParameter("idChapter") != null){
                    request.setAttribute("idChapter", Integer.parseInt(request.getParameter("idChapter")));
                    action_deleteChapter(request, response);
                }
                if(request.getParameter("idSection") != null){
                    request.setAttribute("idSection", Integer.parseInt(request.getParameter("idSection")));
                    action_deleteSection(request, response);
                }
                action_view(request, response);
            } else {
                action_default(request, response);
            }
        } catch (DataLayerException | IOException | ServletException ex) {
            action_error(request, response, "Error: " + ex.getMessage(), 501);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
