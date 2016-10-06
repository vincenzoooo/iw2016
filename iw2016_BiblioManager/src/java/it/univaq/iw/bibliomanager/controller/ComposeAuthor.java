/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.controller;

import it.univaq.iw.bibliomanager.data.model.Author;
import it.univaq.iw.framework.data.DataLayerException;
import it.univaq.iw.framework.result.TemplateResult;
import it.univaq.iw.framework.security.SecurityLayer;
import it.univaq.iw.framework.utils.Utils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
public class ComposeAuthor extends BiblioManagerBaseController {

    private int publicationId;
    private String url;
    private int authorId;
    private String currentNameAuthor;
    private String currentSurnameAuthor;
    private final Map<Integer, String> pages = new HashMap<>();
    private final Map<String, Integer> options = new HashMap<>();
    
    private void action_composeAuthor(HttpServletRequest request, HttpServletResponse response) {
        try {
            Author author = getDataLayer().createAuthor();
            Map<String, String> params = new HashMap<String, String>();
            params.put("authorName", Utils.checkString(request.getParameter("authorName")));
            params.put("authorSurname", Utils.checkString(request.getParameter("authorSurname")));
            if (!validator(params, request, response)) {
                author.setName(params.get("authorName"));
                author.setSurname(params.get("authorSurname"));
                getDataLayer().storeAuthor(author);
                request.setAttribute("authorAdded", 1);
            }
        } catch (DataLayerException ex) {
            action_error(request, response, "Errore nel salvare l'autore: " + ex.getMessage(), 510);
        }
    }

    private void action_updateAuthor(HttpServletRequest request, HttpServletResponse response) {
        try {
            Author author = getDataLayer().getAuthor(authorId);
            Map<String, String> params = new HashMap<String, String>();
            params.put("authorName", Utils.checkString(request.getParameter("authorName")));
            params.put("authorSurname", Utils.checkString(request.getParameter("authorSurname")));
            if (!validator(params, request, response)) {
                author.setName(params.get("authorName"));
                author.setSurname(params.get("authorSurname"));
                getDataLayer().storeAuthor(author);
                request.setAttribute("authorUpdated", 1);
                request.setAttribute("saveResult", "Salvataggio effettuato con successo");
                authorId = 0;
            }
        } catch (DataLayerException ex) {
            action_error(request, response, "Errore nel salvare l'autore: " + ex.getMessage(), 510);
        }
    }

    private void action_deleteAuthor(HttpServletRequest request, HttpServletResponse response)
            throws DataLayerException {
        Author author = getDataLayer().getAuthor((int)request.getAttribute("authorToDelete"));
        getDataLayer().deleteAuthor(author);
    }
    
    private void action_LinkAuthor(HttpServletRequest request, HttpServletResponse response)
            throws DataLayerException {
        List<String> values = new ArrayList<String>(Arrays.asList(request.getParameterValues("authorSelected")));
        getDataLayer().deletePublicationHasAuthor(publicationId);
        for (String value : values) {
            getDataLayer().storePublicationHasAuthor(Integer.parseInt(value), publicationId);
            request.setAttribute("authorConnected", 1);
        }
    }

    private void action_view(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DataLayerException {
        request.setAttribute("page_title", "Gestione Autore");
        TemplateResult res = new TemplateResult(getServletContext());
        List<Author> authors = getDataLayer().getAuthors(options.get("limit"), options.get("offset"));
        List<Author> publicationAuthors = getDataLayer().getPublicationAuthors(publicationId);
        
        int authorsNumber = getDataLayer().getAuthors(0, 0).size();
        if(authorsNumber < options.get("end")){
            options.put("end", authorsNumber);
        }
        int pageNumber = authorsNumber / options.get("limit");
        if(pageNumber < options.get("slice")){
            options.put("slice", pageNumber+1);
        }
        if (pageNumber != 0 && authorsNumber % options.get("limit") > 0) {
            pageNumber++;
        }
        int totOffset = (pageNumber - 1) * options.get("limit");
        for (int i = pageNumber-1; i >= 0; --i) {
            String editorUrl = "author?offset=" + totOffset;
            pages.put(i, editorUrl);
            totOffset -= options.get("limit");
        }
        action_pagination_next(options, pageNumber);
        action_pagination_previous(options, pageNumber);
        action_pagination_first(options);
        action_pagination_last(options, pageNumber);
        request.setAttribute("pages", getSlice(pages, options.get("start"), options.get("end")).entrySet());
        request.setAttribute("first", pages.get(0));
        request.setAttribute("last", pages.get(pages.size()-1));
        int page = options.get("offset")/options.get("limit");
        if(page > 0){
            request.setAttribute("previous", pages.get(page-1));
        }
        if(page < pageNumber){
            request.setAttribute("next", pages.get(page+1));
        }
        request.setAttribute("curr", page);
        
        request.setAttribute("authors", authors);
        request.setAttribute("publicationAuthors", publicationAuthors);
        request.setAttribute("publicationId", publicationId);
        request.setAttribute("url", url);
        request.setAttribute("authorId", authorId);
        request.setAttribute("currentNameAuthor", currentNameAuthor);
        request.setAttribute("currentSurnameAuthor", currentSurnameAuthor);
        res.activate("author.ftl.html", request, response);
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
                if(request.getAttribute("publicationId") != null){
                    publicationId = (int) request.getAttribute("publicationId");
                }
                if(request.getAttribute("url") != null){
                    url = (String) request.getAttribute("url");
                }
                currentUser(request, response, session);
                if (request.getParameter("authorId") != null) {
                    currentNameAuthor = request.getParameter("currentNameAuthor");
                    currentSurnameAuthor = request.getParameter("currentSurnameAuthor");
                    authorId = Integer.parseInt(request.getParameter("authorId"));
                }
                if (request.getParameter("submitAuthor") != null){
                    if(authorId == 0) {
                        action_composeAuthor(request, response);
                    }
                    else{
                        action_updateAuthor(request, response);
                    }
                }                
                if(request.getAttribute("authorToDelete") != null){
                    request.setAttribute("authorToDelete", request.getParameter("authorToDelete"));
                    action_deleteAuthor(request, response);
                }
                if (request.getParameter("linkAuthor") != null) {
                    action_LinkAuthor(request, response);
                }
                if (request.getParameter("offset") != null){
                   options.put("offset", Integer.parseInt(request.getParameter("offset")));
                }
                else{
                   pages.clear();
                   options.put("limit", 10);
                   options.put("offset", 0);
                   options.put("slice", 10);
                   options.put("start", 0);
                   options.put("end", 10);
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
