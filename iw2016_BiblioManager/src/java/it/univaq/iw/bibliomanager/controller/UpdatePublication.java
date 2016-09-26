/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.controller;

import it.univaq.iw.bibliomanager.data.model.Editor;
import it.univaq.iw.bibliomanager.data.model.History;
import it.univaq.iw.bibliomanager.data.model.Publication;
import it.univaq.iw.bibliomanager.data.model.User;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 *
 * @author Vincenzo Lanzieri, Angelo Iezzi
 */
public class UpdatePublication extends BiblioManagerBaseController {

    private int publicationId;
    private final String url = "managePublication";

    private void action_updatePublication(HttpServletRequest request, HttpServletResponse response) {
        try {
            if (publicationId > 0) {
                Publication publication = getDataLayer().getPublication(publicationId);
                Map<String, String> params = new HashMap<String, String>();
                params.put("publicationTitle", Utils.checkString(request.getParameter("publicationTitle")));
                params.put("publicationDescription", Utils.checkString(request.getParameter("publicationDescription")));
                params.put("publicationLanguage", Utils.checkString(request.getParameter("publicationLanguage")));
                params.put("publicationDate", Utils.checkString(request.getParameter("publicationDate")));
                params.put("publicationIsbn", Utils.checkString(request.getParameter("publicationIsbn")));
                params.put("publicationPages", Utils.checkString(request.getParameter("publicationPages")));
                params.put("editors", request.getParameter("editors"));
                if (!validator(params, request, response)) {
                    publication.setTitle(params.get("publicationTitle"));
                    publication.setDescription(params.get("publicationDescription"));
                    publication.setLanguage(params.get("publicationLanguage"));
                    DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                    java.util.Date date = df.parse(params.get("publicationDate"));
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                    publication.setPublicationDate(sqlDate);
                    publication.setIsbn(params.get("publicationIsbn"));
                    publication.setPageNumber(Integer.parseInt(params.get("publicationPages")));
                    publication.setEditor(getDataLayer().getEditor(Integer.parseInt(params.get("editors"))));
                    publication.setIncomplete(false);
                    getDataLayer().storePublication(publication);
                    this.action_updateHistory(request, response);
                    request.setAttribute("publicationId", publication.getKey());
                    publicationId = 0;
                    request.setAttribute("publicationUpdated", 1);
                    action_redirect(request, response, "/details");
                }
            }
        } catch (ServletException | IOException | DataLayerException | NumberFormatException | ParseException ex) {
            action_error(request, response, "Error while updating: " + ex.getMessage(), 510);
        }
    }

    private void action_updateHistory(HttpServletRequest request, HttpServletResponse response) throws DataLayerException {
        HttpSession session = SecurityLayer.checkSession(request);
        User user = getDataLayer().getUser((int) session.getAttribute("userId"));
        History history = getDataLayer().createHistory();
        history.setEntry("Modificata la pubblicazione");
        history.setType(1);
        history.setUser(user);
        history.setPublicationKey(publicationId);
        history.setDate(new java.sql.Timestamp(System.currentTimeMillis()));
        getDataLayer().storeHistory(history);
    }

    @Override
    protected boolean validator(Map<String, String> params, HttpServletRequest request, HttpServletResponse response) {
        boolean error = super.validator(params, request, response);
        if (!error) {
            try {
                String isbn = params.get("publicationIsbn");
                if (!Utils.isNumeric(isbn) || isbn.length() < 13 && isbn.length() >= 14) {
                    request.setAttribute("errorPublicationIsbn", "Non è un codice valido");
                    error = true;
                }
                if (!getDataLayer().getPublication(publicationId).getIsbn().equals(isbn) && getDataLayer().getPublicationByISBN(isbn) != null) {
                    request.setAttribute("errorPublicationIsbn", "ISBN già presente");
                    error = true;
                }
                if (!Utils.isNumeric(params.get("publicationPages"))) {
                    request.setAttribute("errorPublicationPages", "Non è un numero valido");
                    error = true;
                }
                if (!Utils.isDate(params.get("publicationDate"))) {
                    request.setAttribute("errorPublicationDate", "Non è una data valida, si aspetta il formato dd-mm-yyyy");
                    error = true;
                }
            } catch (DataLayerException ex) {
                action_error(request, response, "Error query ISBN: " + ex.getMessage(), 503);
            }
        }
        return error;
    }

    private void action_unlinkAuthor(HttpServletRequest request, HttpServletResponse response)
            throws DataLayerException {
        getDataLayer().deleteAuthorFromPublication(publicationId, Integer.parseInt(request.getParameter("unlinkAuthor")));
    }

    private void action_unlinkKeyword(HttpServletRequest request, HttpServletResponse response)
            throws DataLayerException {
        getDataLayer().deleteKeywordFromPublication(publicationId, Integer.parseInt(request.getParameter("unlinkKeyword")));
    }

    private void action_deletePublication(HttpServletRequest request, HttpServletResponse response)
            throws DataLayerException, ServletException, IOException {
        Publication publication = getDataLayer().getPublication(publicationId);
        getDataLayer().deletePublication(publication);
        action_redirect(request, response, "/catalog");
    }

    @Override
    protected void action_redirect(HttpServletRequest request, HttpServletResponse response, String url) throws ServletException, IOException {
        request.setAttribute("publicationId", publicationId);
        request.setAttribute("url", this.url);
        super.action_redirect(request, response, url);

    }

    private void action_view(HttpServletRequest request, HttpServletResponse response) {
        try {
            List<Editor> editors = getDataLayer().getEditors();
            Publication publication = getDataLayer().getPublication(publicationId);
            request.setAttribute("publication", publication);
            request.setAttribute("editors", editors);
            request.setAttribute("publicationId", publicationId);
            request.setAttribute("url", url);
            TemplateResult res = new TemplateResult(getServletContext());
            res.activate("managePublication.ftl.html", request, response);
        } catch (DataLayerException | ServletException | IOException ex) {
            action_error(request, response, "Error build the template: " + ex.getMessage(), 511);
        }
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
            request.setAttribute("page_title", "Modifica Pubblicazione");
            HttpSession session = SecurityLayer.checkSession(request);
            if (session != null) {
                if (request.getParameter("publicationId") != null) {
                    publicationId = Integer.parseInt(request.getParameter("publicationId"));
                }
                if (request.getParameter("addEditor") != null) {
                    request.setAttribute("publicationId", publicationId);
                    action_redirect(request, response, "/editor");
                }
                if (request.getParameter("addAuthor") != null) {
                    request.setAttribute("publicationId", publicationId);
                    action_redirect(request, response, "/author");
                }
                if (request.getParameter("unlinkAuthor") != null) {
                    action_unlinkAuthor(request, response);
                }
                if (request.getParameter("addKeyword") != null) {
                    request.setAttribute("publicationId", publicationId);
                    action_redirect(request, response, "/keyword");
                }
                if (request.getParameter("unlinkKeyword") != null) {
                    action_unlinkKeyword(request, response);
                }
                if (request.getParameter("addIndex") != null) {
                    request.setAttribute("publicationId", publicationId);
                    action_redirect(request, response, "/index");
                }
                if (request.getParameter("addSource") != null) {
                    request.setAttribute("publicationId", publicationId);
                    action_redirect(request, response, "/source");
                }
                if (request.getParameter("addReprint") != null) {
                    request.setAttribute("publicationId", publicationId);
                    action_redirect(request, response, "/reprint");
                }
                if (request.getParameter("delete") != null) {
                    action_deletePublication(request, response);
                }
                if (request.getParameter("submitPublication") != null) {
                    action_updatePublication(request, response);
                }
                action_view(request, response);
            } else {
                action_default(request, response);
            }
        } catch (DataLayerException | IOException ex) {
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
