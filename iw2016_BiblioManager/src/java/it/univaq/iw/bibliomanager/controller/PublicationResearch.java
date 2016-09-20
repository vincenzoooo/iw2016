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

    private void action_research(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            Map<String, String> filters = new HashMap<String, String>();
            String isbn = request.getParameter("publicationIsbn");
            if (!Utils.isNullOrEmpty(isbn)) {
                filters.put("isbn", isbn);
            }
            String title = request.getParameter("publicationTitle");
            if (!Utils.isNullOrEmpty(title)) {
                filters.put("titolo", title);
            }
            String authorName = request.getParameter("publicationAuthor");
            if (!Utils.isNullOrEmpty(authorName)) {
                filters.put("autore", authorName);
            }
            String editorName = request.getParameter("publicationEditor");
            if (!Utils.isNullOrEmpty(authorName)) {
                filters.put("editore", editorName);
            }
            String date = request.getParameter("publicationYear");
            if (!Utils.isNullOrEmpty(date)) {
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
                filters.put("keyword", keyword);
            }
            String language = request.getParameter("publicationLanguage");
            if (!Utils.isNullOrEmpty(language)) {
                filters.put("lingua", language);
            }
            String download = request.getParameter("download");
            if (!Utils.isNullOrEmpty(download)) {
                filters.put("download", "download");
            }
            if (request.getAttribute("publicationUser") != null) {
                filters.put("utente", request.getAttribute("publicationUser").toString());
            }
            filters.put("order_by", "titolo");
            filters.put("order_mode", "ASC");
            int filters_key = getDataLayer().storeFilters(filters);
            request.setAttribute("filter", filters_key);
            request.setAttribute("isResearch", true);
            getServletContext().getRequestDispatcher("/catalog").forward(request, response);
        } catch (DataLayerException ex) {
            action_error(request, response, "Unable to do the research: " + ex.getMessage());
        }
    }

    private void action_view(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setAttribute("page_title", "Ricerca avanzata");
            TemplateResult res = new TemplateResult(getServletContext());
            res.activate("research.ftl.html", request, response);
        } catch (ServletException | IOException ex) {
            action_error(request, response, "Error build the template: " + ex.getMessage());
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
