/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.controller;

import it.univaq.iw.bibliomanager.data.model.Keyword;
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
public class ComposeKeyword extends BiblioManagerBaseController {

    /**
     * Notify messages
     */
    private final String updateMessage = "Modifica avvenuta con successo.";
    private final String addMessage = "Inserimento avvenuto con successo.";
    private final String linkMessage = "Collegamento con la pubblicazione avvenuto con successo.";
    private final String deleteMessage = "Keyword eliminata con successo.";
    private final String errorDeleteMessage = "Non è possibile eliminare la keyword. È collegata ad almeno una pubblicazione.";

    /**
     * ID of publication request
     */
    private int publicationId;
    /**
     * Url from which the request came
     */
    private String url;
    /**
     * ID of current Keyword if any
     */
    private int keywordId;
    /**
     * Current date of the current Keyword
     */
    private String currentKeyword;
    /**
     * Pages
     */
    private final Map<Integer, String> pages = new HashMap<>();
    /**
     * Configuration for pagination purpose
     */
    private final Map<String, Integer> options = new HashMap<>();

    /**
     * Verify and save a new Keyword
     *
     * @param request
     * @param response
     * @throws DataLayerException
     */
    private void action_composeKeyword(HttpServletRequest request, HttpServletResponse response)
            throws DataLayerException {
        try {
            Keyword keyword = getDataLayer().createKeyword();
            Map<String, String> params = new HashMap<String, String>();
            params.put("keywordName", Utils.checkString(request.getParameter("keywordName")));
            if (!validator(params, request, response)) {
                keyword.setName(params.get("keywordName"));
                getDataLayer().storeKeyword(keyword);
                action_createNotifyMessage(request, response, SUCCESS, addMessage, true);
            }
        } catch (DataLayerException ex) {
            action_error(request, response, "Errore nel salvare la parola chiave: " + ex.getMessage(), 510);
        }
    }

    /**
     * Verify and update a Keyword
     *
     * @param request
     * @param response
     * @throws DataLayerException
     */
    private void action_updateKeyword(HttpServletRequest request, HttpServletResponse response)
            throws DataLayerException {
        try {
            Keyword keyword = getDataLayer().getKeyword(keywordId);
            Map<String, String> params = new HashMap<String, String>();
            params.put("updateKeywordName", Utils.checkString(request.getParameter("keywordName")));
            if (!validator(params, request, response)) {
                if (!keyword.getName().equals(params.get("updateKeywordName"))) {
                    keyword.setName(params.get("updateKeywordName"));
                    keyword.setDirty(true);
                }
                getDataLayer().storeKeyword(keyword);
                action_createNotifyMessage(request, response, SUCCESS, updateMessage, true);
                request.setAttribute("saveResult", "Salvataggio effettuato con successo");
                keywordId = 0;
            }
        } catch (DataLayerException ex) {
            action_error(request, response, "Errore nel salvare la risorsa: " + ex.getMessage(), 510);
        }
    }

    /**
     * Delete the specified Keyword
     *
     * @param request
     * @param response
     * @throws DataLayerException
     */
    private void action_deleteKeyword(HttpServletRequest request, HttpServletResponse response)
            throws DataLayerException {
        int deleted = 0;
        Keyword keyword = getDataLayer().getKeyword((int) request.getAttribute("keywordToDelete"));
        deleted = getDataLayer().deleteKeyword(keyword);
        if (deleted == 0) {
            action_createNotifyMessage(request, response, ERROR, errorDeleteMessage, true);
        } else {
            action_createNotifyMessage(request, response, SUCCESS, deleteMessage, true);
        }
    }

    /**
     * Link up one or more Keyword with request Publication
     *
     * @param request
     * @param response
     * @throws DataLayerException
     */
    private void action_LinkKeyword(HttpServletRequest request, HttpServletResponse response) throws DataLayerException {
        List<String> values = new ArrayList<String>(Arrays.asList(request.getParameterValues("keywordSelected")));
        getDataLayer().deletePublicationHasKeyword(publicationId);
        for (String value : values) {
            getDataLayer().storePublicationHasKeyword(Integer.parseInt(value), publicationId);
            action_createNotifyMessage(request, response, SUCCESS, linkMessage, true);
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
        request.setAttribute("page_title", "Gestione Parole Chiave");
        TemplateResult res = new TemplateResult(getServletContext());
        List<Keyword> keywords = getDataLayer().getKeywords(options.get("limit"), options.get("offset"));
        List<Keyword> publicationKeywords = getDataLayer().getPublicationKeywords(publicationId);

        request.setAttribute("totElements", getDataLayer().getKeywords(0, 0).size());
        request.setAttribute("paginationUrl", "keyword");
        pagination(request, response, pages, options);

        request.setAttribute("keywords", keywords);
        request.setAttribute("publicationKeywords", publicationKeywords);
        request.setAttribute("publicationId", publicationId);
        request.setAttribute("url", url);
        request.setAttribute("keywordId", keywordId);
        request.setAttribute("currentKeyword", currentKeyword);
        res.activate("keyword.ftl.html", request, response);
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
                if (request.getParameter("keywordId") != null && keywordId != Integer.parseInt(request.getParameter("keywordId"))) {
                    currentKeyword = request.getParameter("currentKeyword");
                    keywordId = Integer.parseInt(request.getParameter("keywordId"));
                }
                if (request.getParameter("keywordToDelete") != null) {
                    request.setAttribute("keywordToDelete", Integer.parseInt(request.getParameter("keywordToDelete")));
                    action_deleteKeyword(request, response);
                }
                if (request.getParameter("submitKeyword") != null) {
                    if (keywordId == 0) {
                        action_composeKeyword(request, response);
                    } else {
                        action_updateKeyword(request, response);
                    }
                }
                if (request.getParameter("linkKeyword") != null) {
                    action_LinkKeyword(request, response);
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
