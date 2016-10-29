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

    private final String updateMessage = "Modifica avvenuta con successo.";
    private final String addMessage = "Inserimento avvenuto con successo.";
    private final String linkMessage = "Collegamento con la pubblicazione avvenuto con successo.";
    private final String deleteMessage = "Autore eliminato con successo.";
    private final String errorDeleteMessage = "Non è possibile eliminare l'autore. È collegato ad almeno una pubblicazione.";
    /**
     * ID of publication request
     */
    private int publicationId;
    /**
     * Url from which the request came
     */
    private String url;
    /**
     * ID of current Author if any
     */
    private int authorId;
    /**
     * Current date of the current Author
     */
    private String currentNameAuthor;
    private String currentSurnameAuthor;
    /**
     * Pages
     */
    private final Map<Integer, String> pages = new HashMap<>();
    /**
     * Configuration for pagination purpose
     */
    private final Map<String, Integer> options = new HashMap<>();

    /**
     * Verify and save a new Author
     *
     * @param request
     * @param response
     */
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
                action_createNotifyMessage(request, response, SUCCESS, addMessage, true);
            }
        } catch (DataLayerException ex) {
            action_error(request, response, "Errore nel salvare l'autore: " + ex.getMessage(), 510);
        }
    }

    /**
     * Verify and update an Author
     *
     * @param request
     * @param response
     */
    private void action_updateAuthor(HttpServletRequest request, HttpServletResponse response) {
        try {
            Author author = getDataLayer().getAuthor(authorId);
            Map<String, String> params = new HashMap<String, String>();
            params.put("updateAuthorName", Utils.checkString(request.getParameter("authorName")));
            params.put("updateAuthorSurname", Utils.checkString(request.getParameter("authorSurname")));
            if (!validator(params, request, response)) {
                if (!author.getName().equals(params.get("updateAuthorName"))) {
                    author.setName(params.get("updateAuthorName"));
                    author.setDirty(true);
                }
                if (!author.getSurname().equals(params.get("updateAuthorSurname"))) {
                    author.setSurname(params.get("updateAuthorSurname"));
                    author.setDirty(true);
                }
                getDataLayer().storeAuthor(author);
                action_createNotifyMessage(request, response, SUCCESS, updateMessage, true);
                request.setAttribute("saveResult", "Salvataggio effettuato con successo");
                authorId = 0;
            }
        } catch (DataLayerException ex) {
            action_error(request, response, "Errore nel salvare l'autore: " + ex.getMessage(), 510);
        }
    }

    /**
     * Delete the specified Author
     *
     * @param request
     * @param response
     * @throws DataLayerException
     */
    private void action_deleteAuthor(HttpServletRequest request, HttpServletResponse response)
            throws DataLayerException {
        Author author = getDataLayer().getAuthor((int) request.getAttribute("authorToDelete"));
        int deleted = getDataLayer().deleteAuthor(author);
        if(deleted == 0){
            action_createNotifyMessage(request, response, ERROR, errorDeleteMessage, true);
        }
        else{
            action_createNotifyMessage(request, response, SUCCESS, deleteMessage, true);
        }
    }

    /**
     * Link up one or more Author with request Publication
     *
     * @param request
     * @param response
     * @throws DataLayerException
     */
    private void action_LinkAuthor(HttpServletRequest request, HttpServletResponse response)
            throws DataLayerException {
        if (request.getParameterValues("authorSelected") != null) {
            List<String> values = new ArrayList<String>(Arrays.asList(request.getParameterValues("authorSelected")));
            getDataLayer().deletePublicationHasAuthor(publicationId);
            for (String value : values) {
                getDataLayer().storePublicationHasAuthor(Integer.parseInt(value), publicationId);
                action_createNotifyMessage(request, response, SUCCESS, linkMessage, true);
            }
        } else {
            getDataLayer().deletePublicationHasAuthor(publicationId);
        }
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
        request.setAttribute("page_title", "Gestione Autore");
        TemplateResult res = new TemplateResult(getServletContext());
        List<Author> authors = getDataLayer().getAuthors(options.get("limit"), options.get("offset"));
        List<Author> publicationAuthors = getDataLayer().getPublicationAuthors(publicationId);

        request.setAttribute("totElements", getDataLayer().getAuthors(0, 0).size());
        request.setAttribute("paginationUrl", "author");
        pagination(request, response, pages, options);

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
                if (request.getAttribute("publicationId") != null) {
                    publicationId = (int) request.getAttribute("publicationId");
                }
                if (request.getAttribute("url") != null) {
                    url = (String) request.getAttribute("url");
                }
                currentUser(request, response, session);
                if (authorId == 0 && request.getParameter("authorId") != null) {
                    currentNameAuthor = request.getParameter("currentNameAuthor");
                    currentSurnameAuthor = request.getParameter("currentSurnameAuthor");
                    authorId = Integer.parseInt(request.getParameter("authorId"));
                }
                if (request.getParameter("submitAuthor") != null) {
                    if (authorId == 0) {
                        action_composeAuthor(request, response);
                    } else {
                        action_updateAuthor(request, response);
                    }
                }
                if (request.getParameter("authorToDelete") != null) {
                    request.setAttribute("authorToDelete", Integer.parseInt(request.getParameter("authorToDelete")));
                    action_deleteAuthor(request, response);
                }
                if (request.getParameter("linkAuthor") != null) {
                    action_LinkAuthor(request, response);
                }
                if (request.getParameter("offset") != null) {
                    options.put("offset", Integer.parseInt(request.getParameter("offset")));
                } else {
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
}
