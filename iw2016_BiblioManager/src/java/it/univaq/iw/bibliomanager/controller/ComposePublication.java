/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.controller;

import it.univaq.iw.bibliomanager.data.model.Author;
import it.univaq.iw.bibliomanager.data.model.Editor;
import it.univaq.iw.bibliomanager.data.model.History;
import it.univaq.iw.bibliomanager.data.model.Publication;
import it.univaq.iw.bibliomanager.data.model.Source;
import it.univaq.iw.bibliomanager.data.model.User;
import it.univaq.iw.framework.data.DataLayerException;
import it.univaq.iw.framework.result.TemplateResult;
import it.univaq.iw.framework.security.SecurityLayer;
import it.univaq.iw.framework.utils.Utils;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import it.univaq.iw.bibliomanager.data.model.Keyword;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author Vincenzo Lanzieri
 */
public class ComposePublication extends BiblioManagerBaseController {

    private void action_composePublication(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DataLayerException {
        try {
            HttpSession session = SecurityLayer.checkSession(request);
            Publication publication = getDataLayer().createPublication();
            if(session.getAttribute("publicationId") != null){
                publication = getDataLayer().getPublication((int)session.getAttribute("publicationId"), true);
            }
            Map<String, String> params = new HashMap<String, String>();
            params.put("publicationTitle", Utils.checkString(request.getParameter("publicationTitle")));
            params.put("publicationDescription", Utils.checkString(request.getParameter("publicationDescription")));
            params.put("publicationLanguage", Utils.checkString(request.getParameter("publicationLanguage")));
            params.put("publicationDate", Utils.checkString(request.getParameter("publicationDate")));
            //params.put("publicationIndex", Utils.checkString(request.getParameter("publicationIndex")));
            params.put("publicationIsbn", Utils.checkString(request.getParameter("publicationIsbn")));
            params.put("publicationPages", Utils.checkString(request.getParameter("publicationPages")));
            params.put("editors", request.getParameter("editors"));
            params.put("authors", request.getParameter("authors"));
            params.put("keywords", request.getParameter("keywords"));
            //params.put("sourceId", Utils.checkString(request.getParameter("sourceId")));
            if (!validator(params, request, response)) {
                publication.setTitle(params.get("publicationTitle"));
                publication.setDescription(params.get("publicationDescription"));
                publication.setLanguage(params.get("publicationLanguage"));
                DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                java.util.Date date = df.parse(params.get("publicationDate"));
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                publication.setPublicationDate(sqlDate);
                publication.setIndex("");
                //publication.setIndex(params.get("publicationIndex"));
                publication.setIsbn(params.get("publicationIsbn"));
                publication.setPageNumber(Integer.parseInt(params.get("publicationPages")));
                publication.setEditor(getDataLayer().getEditor(Integer.parseInt(params.get("editors"))));
                publication.setIncomplete(false);
                getDataLayer().storePublication(publication);
                request.setAttribute("publication", publication);
                this.action_composeHistory(request, response);
            }
        } catch (DataLayerException | ParseException ex) {
            action_error(request, response, "Error: " + ex.getMessage());
        }
    }

    private void action_composeHistory(HttpServletRequest request, HttpServletResponse response) throws DataLayerException {
        HttpSession session = SecurityLayer.checkSession(request);
        User user = getDataLayer().getUser((int) session.getAttribute("userId"));
        History history = getDataLayer().createHistory();
        history.setEntry("inserito una pubblicazione");
        history.setType(0);
        history.setUser(user);
        history.setPublication((Publication) request.getAttribute("publication"));
        history.setDate(new java.sql.Date(System.currentTimeMillis()));
        getDataLayer().storeHistory(history);
    }

    @Override
    protected boolean validator(Map<String, String> params, HttpServletRequest request, HttpServletResponse response){
        boolean error = super.validator(params, request, response);
        if(!error){
            try {
                String isbn = params.get("publicationIsbn");
                if(!Utils.isNumeric(isbn) || isbn.length() < 13 && isbn.length() >= 14){
                    request.setAttribute("errorPublicationIsbn", "Non è un codice valido");
                    error = true;
                }
                if(getDataLayer().getPublicationByISBN(isbn) != null){
                    request.setAttribute("errorPublicationIsbn", "ISBN già presente");
                    error = true;
                }
                if(!Utils.isNumeric(params.get("publicationPages"))){
                    request.setAttribute("errorPublicationPages", "Non è un numero valido");
                    error = true;
                }
                if(!Utils.isDate(params.get("publicationDate"))){
                    request.setAttribute("errorPublicationDate", "Non è una data valida, si aspetta il formato dd-mm-yyyy");
                    error = true;
                }
            } catch (DataLayerException ex) {
                action_error(request, response, "Error query ISBN: " + ex.getMessage());
            }
        }
        return error;
    }
    
    private void action_savePartially(HttpServletRequest request, HttpServletResponse response, String url) throws DataLayerException, ServletException, IOException, ParseException {
        HttpSession session = SecurityLayer.checkSession(request);
        Publication publication = getDataLayer().createPublication();
        if(request.getParameter("publicationId") != null){
            publication = getDataLayer().getPublication(Integer.parseInt(request.getParameter("publicationId")));
        }
        if(!request.getParameter("publicationTitle").isEmpty()){
            publication.setTitle(Utils.checkString(request.getParameter("publicationTitle")));
        }
        if(!request.getParameter("publicationDescription").isEmpty()){
            publication.setDescription(Utils.checkString(request.getParameter("publicationDescription")));
        }
        if(!request.getParameter("publicationLanguage").isEmpty()){
            publication.setLanguage(Utils.checkString(request.getParameter("publicationLanguage")));
        }
        if(!request.getParameter("publicationDate").isEmpty() && Utils.isDate(request.getParameter("publicationDate"))){
            DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            java.util.Date date = df.parse(request.getParameter("publicationDate"));
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            publication.setPublicationDate(sqlDate);
        }
        //params.put("publicationIndex", Utils.checkString(request.getParameter("publicationIndex")));
        if(!request.getParameter("publicationIsbn").isEmpty()&&getDataLayer().getPublicationByISBN(request.getParameter("publicationIsbn")) == null&&Utils.isNumeric(request.getParameter("publicationIsbn"))&&request.getParameter("publicationIsbn").length() >= 13 && request.getParameter("publicationIsbn").length() < 14){
            publication.setIsbn(request.getParameter("publicationIsbn"));
        }
        if(!request.getParameter("publicationPages").isEmpty()&&Utils.isNumeric(request.getParameter("publicationPages"))){
            publication.setPageNumber(Integer.parseInt(request.getParameter("publicationPages")));
        }
        if(!request.getParameter("editors").isEmpty()){
            Editor editor = getDataLayer().getEditor(Integer.parseInt(request.getParameter("editors")));
            publication.setEditor(editor);
        }
        publication.setIncomplete(true);
        getDataLayer().storePublication(publication);
        session.setAttribute("publicationId", publication.getKey());
        action_redirect(request, response, url);
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
            if(session != null){
                currentUser(request, response, session);
                request.setAttribute("page_title", "Nuova Pubblicazione");
                List<Editor> editors = getDataLayer().getEditors();
                List<Keyword> keywords = getDataLayer().getKeywords();
                List<Source> sources = getDataLayer().getSources();
                request.setAttribute("authors", "");
                request.setAttribute("editors", editors);
                request.setAttribute("keywords", keywords);
                request.setAttribute("sources", sources);
                
                Publication publication = null;
                if(session.getAttribute("publicationId") != null){
                    publication = getDataLayer().getPublication((int)session.getAttribute("publicationId"), true);
                }
                if(publication != null && publication.getIncomplete()){
                    List<Author> authors = getDataLayer().getPublicationAuthors((int)session.getAttribute("publicationId"));
                    request.setAttribute("authors", authors);
                    
                    request.setAttribute("publicationTitle", publication.getTitle());
                    request.setAttribute("publicationDescription", publication.getDescription());
                    request.setAttribute("publicationLanguage", publication.getLanguage());
                    DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                    String date = (publication.getPublicationDate() != null)? df.format(publication.getPublicationDate()) : "";
                    request.setAttribute("publicationDate", date);
                    //params.put("publicationIndex", Utils.checkString(request.getParameter("publicationIndex")));
                    request.setAttribute("publicationIsbn", publication.getIsbn());
                    request.setAttribute("publicationPages", publication.getPageNumber());
                    request.setAttribute("currentEditor", publication.getEditor().getKey());
                }
                if (request.getParameter("submitPublication") != null) {
                    action_composePublication(request, response);
                    session.removeAttribute("publicationId");
                }
                if (request.getParameter("addEditor") != null) {
                    action_savePartially(request, response, "/editor");
                }
                if (request.getParameter("addAuthor") != null) {
                    action_savePartially(request, response, "/author");
                }
                if (request.getParameter("addKeyword") != null) {
                    action_savePartially(request, response, "/keyword");
                }
                TemplateResult res = new TemplateResult(getServletContext());
                res.activate("publication.ftl.html", request, response);
            } else {
                action_default(request, response);
            }
        } catch (Exception ex) {
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
