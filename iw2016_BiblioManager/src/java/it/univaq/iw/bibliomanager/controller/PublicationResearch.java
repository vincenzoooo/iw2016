/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.controller;

import it.univaq.iw.bibliomanager.data.model.User;
import it.univaq.iw.framework.data.DataLayerException;
import it.univaq.iw.framework.result.TemplateResult;
import it.univaq.iw.framework.security.SecurityLayer;
import it.univaq.iw.framework.utils.Utils;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Vincenzo Lanzieri, Angelo Iezzi
 */
public class PublicationResearch extends BiblioManagerBaseController {

    /**
     * Verify and save Publication's research filters
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    private void action_research(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            Map<String, String> filters = new HashMap<String, String>();
            String isbn = request.getParameter("publicationIsbn");
            if (!Utils.isNullOrEmpty(isbn)) {
                noAction(isbn, request, response);
                filters.put("isbn", isbn);
            }
            else{
                filters.put("isbn", "");
            }
            String title = request.getParameter("publicationTitle");
            if (!Utils.isNullOrEmpty(title)) {
                noAction(title, request, response);
                filters.put("titolo", title);
            }
            else{
                filters.put("titolo", "");
            }
            String authorName = request.getParameter("publicationAuthor");
            if (!Utils.isNullOrEmpty(authorName)) {
                noAction(authorName, request, response);
                filters.put("autore", authorName);
            }
            else{
                filters.put("autore", "");
            }
            String editorName = request.getParameter("publicationEditor");
            if (!Utils.isNullOrEmpty(editorName)) {
                noAction(editorName, request, response);
                filters.put("editore", editorName);
            }
            else{
                filters.put("editore", "");
            }
            String date = request.getParameter("publicationYear");
            if (!Utils.isNullOrEmpty(date)) {
                noAction(date, request, response);
                filters.put("anno_inizio", date);
                String end = String.valueOf(Integer.parseInt(Utils.getArrayParameter(filters, "anno_inizio")) + 1);
                filters.put("anno_fine", end);
            } else {
                filters.put("anno_inizio", String.valueOf(0));
                int year = Calendar.getInstance().get(Calendar.YEAR);
                filters.put("anno_fine", String.valueOf(year + 1));
            }
            String keyword = request.getParameter("publicationKeyword");
            if (!Utils.isNullOrEmpty(keyword)) {
                noAction(keyword, request, response);
                filters.put("keyword", keyword);
            }
            else{
                filters.put("keyword", "");
            }
            String language = request.getParameter("publicationLanguage");
            if (!Utils.isNullOrEmpty(language)) {
                noAction(language, request, response);
                filters.put("lingua", language);
            }
            else{
                filters.put("lingua", "");
            }
            String download = request.getParameter("download");
            if (!Utils.isNullOrEmpty(download)) {
                noAction(download, request, response);
                filters.put("download", "download");
            }
            else{
                filters.put("download", "");
            }
            if (request.getAttribute("publicationUser") != null) {
                filters.put("utente", request.getAttribute("publicationUser").toString());
            }
            else{
                filters.put("utente", "");
            }
            filters.put("order_by", "titolo");
            filters.put("order_mode", "ASC");
            int filters_key = getDataLayer().storeFilters(filters);
            request.setAttribute("filter", filters_key);
            request.setAttribute("isResearch", true);
            getServletContext().getRequestDispatcher("/catalog").forward(request, response);
        } catch (DataLayerException ex) {
            action_error(request, response, "Unable to do the research: " + ex.getMessage(), 502);
        }
    }

    /**
     * Compile the template for display it
     *
     * @param request
     * @param response
     */
    private void action_view(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setAttribute("page_title", "Ricerca avanzata");
            TemplateResult res = new TemplateResult(getServletContext());
            res.activate("research.ftl.html", request, response);
        } catch (ServletException | IOException ex) {
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
            HttpSession session = SecurityLayer.checkSession(request);
            if (session != null) {
                currentUser(request, response, session);
                if (request.getParameter("submitResearch") != null) {
                    action_research(request, response);
                }
                if (request.getParameter("userId") != null) {
                    User user = getDataLayer().getUser(Integer.parseInt(request.getParameter("userId")));
                    if (user != null) {
                        request.setAttribute("publicationUser", user.getName() + " " + user.getSurname());
                        action_research(request, response);
                    }
                }
                action_view(request, response);
            } else {
                action_default(request, response);
            }
        } catch (DataLayerException | IOException | NumberFormatException | ServletException ex) {
            action_error(request, response, "Error: " + ex.getMessage(), 501);
        }
    }
}
