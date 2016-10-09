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
import java.util.List;

/**
 *
 * @author Vincenzo Lanzieri, Angelo Iezzi
 */
public class ComposePublication extends BiblioManagerBaseController {

    private int publicationId;
    private final String url = "publication";
    
    private void action_composePublication(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DataLayerException {
        try {
            Publication publication = getDataLayer().createPublication();
            HttpSession session = SecurityLayer.checkSession(request);
            if (publicationId > 0) {
                publication = getDataLayer().getPublication(publicationId);
            }
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
                request.setAttribute("publicationAdded",1);
                request.setAttribute("publication", publication);
                this.action_composeHistory(request, response);
                publicationId = publication.getKey();
            }
            else{
                request.setAttribute("redirect", false);
            }
        } catch (DataLayerException | ParseException ex) {
            action_error(request, response, "Errore nel salvare i dati: " + ex.getMessage(), 510);
        }
    }

    private void action_composeHistory(HttpServletRequest request, HttpServletResponse response) throws DataLayerException {
        HttpSession session = SecurityLayer.checkSession(request);
        User user = getDataLayer().getUser((int) session.getAttribute("userId"));
        History history = getDataLayer().createHistory();
        history.setEntry("Inserita una pubblicazione");
        history.setType(0);
        history.setUser(user);
        history.setPublicationKey(((Publication) request.getAttribute("publication")).getKey());
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
                if (getDataLayer().getPublicationByISBN(isbn) != null) {
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
                action_error(request, response, "Error query ISBN: " + ex.getMessage(), 504);
            }
        }
        return error;
    }

    private void action_savePartially(HttpServletRequest request, HttpServletResponse response, String url, Publication publication) throws DataLayerException, ServletException, IOException, ParseException {
        if (publication == null) {
            publication = getDataLayer().createPublication();
            if (publicationId > 0) {
                publication = getDataLayer().getPublication(publicationId);
            }
            if (!request.getParameter("publicationTitle").isEmpty()) {
                String title = Utils.checkString(request.getParameter("publicationTitle"));
                noAction(title, request, response);
                publication.setTitle(title);
            }
            if (!request.getParameter("publicationDescription").isEmpty()) {
                String description = Utils.checkString(request.getParameter("publicationDescription"));
                noAction(description, request, response);
                publication.setDescription(description);
            }
            if (!request.getParameter("publicationLanguage").isEmpty()) {
                String language = Utils.checkString(request.getParameter("publicationLanguage"));
                noAction(language, request, response);
                publication.setLanguage(language);
            }
            if (!request.getParameter("publicationDate").isEmpty() && Utils.isDate(request.getParameter("publicationDate"))) {
                String publicationDate = request.getParameter("publicationDate");
                DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                java.util.Date date = df.parse(publicationDate);
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                publication.setPublicationDate(sqlDate);
            }
            if (!request.getParameter("publicationIsbn").isEmpty() && getDataLayer().getPublicationByISBN(request.getParameter("publicationIsbn")) == null && Utils.isNumeric(request.getParameter("publicationIsbn")) && request.getParameter("publicationIsbn").length() >= 13 && request.getParameter("publicationIsbn").length() < 14) {
                publication.setIsbn(request.getParameter("publicationIsbn"));
            }
            if (!request.getParameter("publicationPages").isEmpty() && Utils.isNumeric(request.getParameter("publicationPages"))) {
                publication.setPageNumber(Integer.parseInt(request.getParameter("publicationPages")));
            }
            if (request.getParameter("editors") != null && !request.getParameter("editors").isEmpty()) {
                Editor editor = getDataLayer().getEditor(Integer.parseInt(request.getParameter("editors")));
                publication.setEditor(editor);
            }
            else{
                Editor editor = getDataLayer().createEditor();
                publication.setEditor(editor);
            }
            publication.setIncomplete(true);
            publication.setTimestamp(new java.sql.Timestamp(System.currentTimeMillis()));
            getDataLayer().storePublication(publication);
            publicationId = publication.getKey();
            request.setAttribute("publicationId", publicationId);
            request.setAttribute("url", this.url);
        }
        request.setAttribute("publicationPartial",1);
        action_redirect(request, response, url);
    }

    private void action_unlinkAuthor (HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws DataLayerException {
        getDataLayer().deleteAuthorFromPublication(publicationId, Integer.parseInt(request.getParameter("unlinkAuthor")));
    }

    private void action_unlinkKeyword (HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws DataLayerException {
        getDataLayer().deleteKeywordFromPublication(publicationId, Integer.parseInt(request.getParameter("unlinkAuthor")));
    }
    
    private void action_view (HttpServletRequest request, HttpServletResponse response) throws DataLayerException, ServletException, IOException{
        Publication publication = getDataLayer().getPublication(publicationId);
        if (publication != null && publication.getIncomplete()) {
            List<Author> authors = getDataLayer().getPublicationAuthors(publicationId);
            List<Keyword> keywords = getDataLayer().getPublicationKeywords(publicationId);
            List<Source> sources = getDataLayer().getPublicationSources(publicationId,0,0);
            request.setAttribute("authors", authors);
            request.setAttribute("keywords", keywords);
            request.setAttribute("sources", sources);
            request.setAttribute("publicationTitle", publication.getTitle());
            request.setAttribute("publicationDescription", publication.getDescription());
            request.setAttribute("publicationLanguage", publication.getLanguage());
            DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            String date = (publication.getPublicationDate() != null) ? df.format(publication.getPublicationDate()) : "";
            request.setAttribute("publicationDate", date);
            request.setAttribute("publicationIsbn", publication.getIsbn());
            request.setAttribute("publicationPages", publication.getPageNumber());
            request.setAttribute("currentEditor", publication.getEditor().getKey());
        }
        TemplateResult res = new TemplateResult(getServletContext());
        res.activate("publication.ftl.html", request, response);
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
                request.setAttribute("page_title", "Nuova Pubblicazione");
                List<Editor> editors = getDataLayer().getEditors(0,0);

                request.setAttribute("authors", "");
                request.setAttribute("editors", editors);
                request.setAttribute("keywords", "");
                request.setAttribute("sources", "");

                Publication publication = null;
                if (request.getParameter("publicationId") != null) {
                    publicationId = Integer.parseInt(request.getParameter("publicationId"));
                }
                if (request.getParameter("submitPublication") != null) {
                    action_composePublication(request, response);
                    boolean redirect = true;
                    if(request.getAttribute("redirect") != null){
                        redirect = (boolean)request.getAttribute("redirect");
                    }
                    if(redirect && publicationId > 0){
                        String detailUrl = "/details?publicationId=" + publicationId;
                        publicationId = 0;
                         request.setAttribute("publicationAdded", 1);
                        action_redirect(request, response, detailUrl);
                    }
                }
                if (request.getParameter("addEditor") != null) {
                    action_savePartially(request, response, "/editor", publication);
                }
                if (request.getParameter("addAuthor") != null) {
                    action_savePartially(request, response, "/author", publication);
                }
                if (request.getParameter("unlinkAuthor") != null) {
                    action_unlinkAuthor(request, response, session);
                }
                if (request.getParameter("addKeyword") != null) {
                    action_savePartially(request, response, "/keyword", publication);
                }
                if (request.getParameter("unlinkKeyword") != null) {
                    action_unlinkKeyword(request, response, session);
                }
                if (request.getParameter("addIndex") != null) {
                    action_savePartially(request, response, "/index", publication);
                }
                if (request.getParameter("addSource") != null) {
                    action_savePartially(request, response, "/source", publication);
                }
                if (request.getParameter("addReprint") != null) {
                    action_savePartially(request, response, "/reprint", publication);
                }
                action_view(request, response);
            } else {
                action_default(request, response);
            }
        } catch (DataLayerException | IOException | ParseException | ServletException ex) {
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
