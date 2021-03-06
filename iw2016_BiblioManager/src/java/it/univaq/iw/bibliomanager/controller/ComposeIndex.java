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

    /**
     * Notify messages
     */
    private final String updateChapterMessage = "Capitolo modificato con successo.";
    private final String updateSectionMessage = "Sezione modificata con successo.";
    private final String addChapterMessage = "Inserimento del capitolo avvenuto con successo.";
    private final String addSectionMessage = "Inserimento della sezione avvenuto con successo.";
    private final String deleteChapterMessage = "Capitolo eliminato con successo.";
    private final String errorDeleteChapterMessage = "Non è possibile eliminare il capitolo. Ha almeno una sezione.";
    private final String deleteSectionMessage = "Sezione eliminata con successo.";
    /**
     * ID of publication request
     */
    private int publicationId;
    /**
     * Url from which the request came
     */
    private String url;
    /**
     * ID of current Chapter if any
     */
    private int chapterId;
    /**
     * ID of current Section if any
     */
    private int sectorId;

    /**
     * Verify and save a new Chapter
     *
     * @param request
     * @param response
     */
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
                action_createNotifyMessage(request, response, SUCCESS, addChapterMessage, true);
            }
        } catch (DataLayerException ex) {
            action_error(request, response, "Errore nel salvare il capitolo: " + ex.getMessage(), 510);
        }
    }

    /**
     * Verify and update a Chapter
     *
     * @param request
     * @param response
     */
    private void action_updateChapter(HttpServletRequest request, HttpServletResponse response) {
        try {
            HttpSession session = SecurityLayer.checkSession(request);
            Chapter chapter = getDataLayer().getChapter(chapterId);
            Map<String, String> params = new HashMap<String, String>();
            params.put("chapterTitle", Utils.checkString(request.getParameter("chapterTitle")));
            params.put("chapterNumber", Utils.checkString(request.getParameter("chapterNumber")));
            if (!validator(params, request, response)) {
                if (chapter.getNumber() != Integer.parseInt(params.get("chapterNumber"))) {
                    chapter.setNumber(Integer.parseInt(params.get("chapterNumber")));
                    chapter.setDirty(true);
                }
                if (!chapter.getTitle().equals(params.get("chapterTitle"))) {
                    chapter.setTitle(params.get("chapterTitle"));
                    chapter.setDirty(true);
                }
                getDataLayer().storeChapter(chapter);
                action_createNotifyMessage(request, response, SUCCESS, updateChapterMessage, true);
                chapterId = 0;
            }
        } catch (DataLayerException ex) {
            action_error(request, response, "Errore nel salvare il capitolo: " + ex.getMessage(), 510);
        }
    }

    /**
     * Verify and save a new Section
     *
     * @param request
     * @param response
     */
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
                action_createNotifyMessage(request, response, SUCCESS, addSectionMessage, true);
            }
        } catch (DataLayerException ex) {
            action_error(request, response, "Errore nel salvare il sezione: " + ex.getMessage(), 510);
        }
    }

    /**
     * Verify and update a Section
     *
     * @param request
     * @param response
     */
    private void action_updateSection(HttpServletRequest request, HttpServletResponse response) {
        try {
            Section section = getDataLayer().getSection(sectorId);
            Map<String, String> params = new HashMap<String, String>();
            params.put("chapter", Utils.checkString(request.getParameter("chapter")));
            params.put("sectionTitle", Utils.checkString(request.getParameter("sectionTitle")));
            params.put("sectionNumber", Utils.checkString(request.getParameter("sectionNumber")));
            if (!validator(params, request, response)) {
                if (section.getNumber() != Integer.parseInt(params.get("sectionNumber"))) {
                    section.setNumber(Integer.parseInt(params.get("sectionNumber")));
                    section.setDirty(true);
                }
                if (!section.getTitle().equals(params.get("sectionTitle"))) {
                    section.setTitle(params.get("sectionTitle"));
                    section.setDirty(true);
                }
                if (section.getChapterKey() != Integer.parseInt(params.get("chapter"))) {
                    section.setChapterKey(Integer.parseInt(params.get("chapter")));
                    section.setDirty(true);
                }
                getDataLayer().storeSection(section);
                sectorId = 0;
                action_createNotifyMessage(request, response, SUCCESS, updateSectionMessage, true);
            }
        } catch (DataLayerException ex) {
            action_error(request, response, "Errore nel salvare il sezione: " + ex.getMessage(), 510);
        }
    }

    /**
     * Data validator
     *
     * @param params
     * @param request
     * @param response
     * @return
     */
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

    /**
     * Delete a specified Chapter
     *
     * @param request
     * @param response
     * @throws DataLayerException
     */
    private void action_deleteChapter(HttpServletRequest request, HttpServletResponse response) throws DataLayerException {
        Chapter chapter = getDataLayer().getChapter((int) request.getAttribute("idChapter"));
        if (chapter.getSections().size() > 0) {
            for (Section section : chapter.getSections()) {
                request.setAttribute("idSection", section.getKey());
            }
        }
        int deleted = getDataLayer().deleteChapter(chapter);
        if (deleted == 0) {
            action_createNotifyMessage(request, response, ERROR, errorDeleteChapterMessage, true);
        } else {
            action_createNotifyMessage(request, response, SUCCESS, deleteChapterMessage, true);
        }
    }

    /**
     * Delete a specified Section
     *
     * @param request
     * @param response
     * @throws DataLayerException
     */
    private void action_deleteSection(HttpServletRequest request, HttpServletResponse response) throws DataLayerException {
        Section section = getDataLayer().getSection((int) request.getAttribute("idSection"));
        getDataLayer().deleteSection(section);
        action_createNotifyMessage(request, response, SUCCESS, deleteSectionMessage, true);
    }

    /**
     * Compile the template for display it
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @throws DataLayerException
     */
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
                if (request.getAttribute("publicationId") != null) {
                    publicationId = (int) request.getAttribute("publicationId");
                }
                if (request.getAttribute("url") != null) {
                    url = (String) request.getAttribute("url");
                }
                if (request.getParameter("chapterId") != null) {
                    chapterId = Integer.parseInt(request.getParameter("chapterId"));
                }
                if (request.getParameter("sectionId") != null) {
                    sectorId = Integer.parseInt(request.getParameter("sectionId"));
                }
                if (request.getParameter("submitChapter") != null) {
                    if (chapterId == 0) {
                        action_composeChapter(request, response);
                    } else {
                        action_updateChapter(request, response);
                    }
                }
                if (request.getParameter("submitSection") != null) {
                    if (sectorId == 0) {
                        action_composeSection(request, response);
                    } else {
                        action_updateSection(request, response);
                    }
                }
                if (request.getParameter("idChapter") != null) {
                    request.setAttribute("idChapter", Integer.parseInt(request.getParameter("idChapter")));
                    action_deleteChapter(request, response);
                }
                if (request.getParameter("idSection") != null) {
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
}
