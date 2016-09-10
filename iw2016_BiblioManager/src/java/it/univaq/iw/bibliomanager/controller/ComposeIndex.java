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

    private void action_composeChapter(HttpServletRequest request, HttpServletResponse response) {
        try {
            HttpSession session = SecurityLayer.checkSession(request);
            Chapter chapter = getDataLayer().createChapter();
            Map<String, String> params = new HashMap<String, String>();
            params.put("chapterTitle", Utils.checkString(request.getParameter("chapterTitle")));
            params.put("chapterNumber", Utils.checkString(request.getParameter("chapterNumber")));
            if (!validator(params, request, response, session)) {
                chapter.setNumber(Integer.parseInt(params.get("chapterNumber")));
                chapter.setTitle(params.get("chapterTitle"));
                chapter.setPublicationKey((int) session.getAttribute("publicationId"));
                getDataLayer().storeChapter(chapter);
            }
        } catch (DataLayerException ex) {
            action_error(request, response, "Errore nel salvare il capitolo: " + ex.getMessage());
        }
    }

    private void action_updateChapter(HttpServletRequest request, HttpServletResponse response) {
        try {
            HttpSession session = SecurityLayer.checkSession(request);
            Chapter chapter = getDataLayer().getChapter(Integer.parseInt(request.getParameter("chapterId")));
            Map<String, String> params = new HashMap<String, String>();
            params.put("chapterTitle", Utils.checkString(request.getParameter("chapterTitle")));
            params.put("chapterNumber", Utils.checkString(request.getParameter("chapterNumber")));
            if (!validator(params, request, response, session)) {
                chapter.setNumber(Integer.parseInt(params.get("chapterNumber")));
                chapter.setTitle(params.get("chapterTitle"));
                getDataLayer().storeChapter(chapter);
            }
        } catch (DataLayerException ex) {
            action_error(request, response, "Errore nel salvare il capitolo: " + ex.getMessage());
        }
    }

    private void action_composeSection(HttpServletRequest request, HttpServletResponse response) {
        try {
            HttpSession session = SecurityLayer.checkSession(request);
            Section section = getDataLayer().createSection();
            Map<String, String> params = new HashMap<String, String>();
            params.put("chapter", Utils.checkString(request.getParameter("chapter")));
            params.put("sectionTitle", Utils.checkString(request.getParameter("sectionTitle")));
            params.put("sectionNumber", Utils.checkString(request.getParameter("sectionNumber")));
            if (!validator(params, request, response, session)) {
                section.setNumber(Integer.parseInt(params.get("sectionNumber")));
                section.setTitle(params.get("sectionTitle"));
                section.setChapterKey(Integer.parseInt(params.get("chapter")));
                getDataLayer().storeSection(section);
            }
        } catch (DataLayerException ex) {
            action_error(request, response, "Errore nel salvare il sezione: " + ex.getMessage());
        }
    }

    private void action_updateSection(HttpServletRequest request, HttpServletResponse response) {
        try {
            HttpSession session = SecurityLayer.checkSession(request);
            Section section = getDataLayer().getSection(Integer.parseInt(request.getParameter("sectionId")));
            Map<String, String> params = new HashMap<String, String>();
            params.put("chapter", Utils.checkString(request.getParameter("chapter")));
            params.put("sectionTitle", Utils.checkString(request.getParameter("sectionTitle")));
            params.put("sectionNumber", Utils.checkString(request.getParameter("sectionNumber")));
            if (!validator(params, request, response, session)) {
                section.setNumber(Integer.parseInt(params.get("sectionNumber")));
                section.setTitle(params.get("sectionTitle"));
                section.setChapterKey(Integer.parseInt(params.get("chapter")));
                getDataLayer().storeSection(section);
            }
        } catch (DataLayerException ex) {
            action_error(request, response, "Errore nel salvare il sezione: " + ex.getMessage());
        }
    }

    protected boolean validator(Map<String, String> params, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
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
                    List<Chapter> elements = getDataLayer().getChapters((int) session.getAttribute("publicationId"));
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
                action_error(request, response, "Error to get chapter or section: " + ex.getMessage());
            }
        }
        return error;
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
            request.setAttribute("page_title", "Gestione Indice");
            TemplateResult res = new TemplateResult(getServletContext());
            HttpSession session = SecurityLayer.checkSession(request);
            if (session != null) {
                currentUser(request, response, session);
                if (request.getParameter("submitChapter") != null) {
                    action_composeChapter(request, response);
                }
                if (request.getParameter("submitSection") != null) {
                    action_composeSection(request, response);
                }
                if (request.getParameter("submitChapter") != null && request.getAttribute("chapterId") != null) {
                    action_updateChapter(request, response);
                    request.removeAttribute("chapterId");
                }
                if (request.getParameter("submitSection") != null && request.getParameter("sectionId") != null) {
                    action_updateSection(request, response);
                    request.removeAttribute("sectionId");
                }
                List<Chapter> chapters = getDataLayer().getChapters((int) session.getAttribute("publicationId"));

                request.setAttribute("chapters", chapters);

                res.activate("index.ftl.html", request, response);

            } else {
                action_default(request, response);
            }
        } catch (DataLayerException | IOException | ServletException ex) {
            action_error(request, response, "Error: " + ex.getMessage());
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
