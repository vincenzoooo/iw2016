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
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Vincenzo Lanzieri
 */
public class PublicationResearch extends BiblioManagerBaseController {

    private void action_research(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        try {
            Map<String, String> filters = new HashMap<String, String>();
            String isbn = request.getParameter("publicationIsbn");
            if (isbn != null) {
                filters.put("publicationIsbn", isbn);
            }
            String title = request.getParameter("publicationTitle");
            if (title != null) {
                filters.put("publicationTitle", title);
            }
            String authorName = request.getParameter("publicationAuthor");
            if (authorName != null) {
                filters.put("publicationAuthor", authorName);
            }
            String editorName = request.getParameter("publicationEditor");
            if (authorName != null) {
                filters.put("publicationEditor", editorName);
            }
            String date = request.getParameter("publicationYear");
            if (date != null && !date.isEmpty()) {
                filters.put("publicationYear", date);
                String end = String.valueOf(Integer.parseInt(Utils.getArrayParameter(filters, "publicationYear")) + 1);
                filters.put("publicationYearEnd", end);
            }
            else{
                filters.put("publicationYear", String.valueOf(0));
                int year = Calendar.getInstance().get(Calendar.YEAR);
                filters.put("publicationYearEnd", String.valueOf(year+1));
            }
            String keyword = request.getParameter("publicationKeyword");
            if (keyword != null) {
                filters.put("publicationKeyword", keyword);
            }
            String language = request.getParameter("publicationLanguage");
            if (language != null) {
                filters.put("publicationLanguage", language);
            }
            String user = request.getAttribute("publicationUser").toString();
            if(user != null){
                filters.put("publicationUser", user);
            }
            filters.put("order_by", "titolo");
            request.setAttribute("publications", getDataLayer().getPublicationsByFilters(filters));
            getServletContext().getRequestDispatcher("/catalog").forward(request, response);
        }
        catch (DataLayerException ex) {
            action_error(request, response, "Unable to do the research: " + ex.getMessage());
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
            request.setAttribute("page_title", "Ricerca avanzata");
            TemplateResult res = new TemplateResult(getServletContext());
            HttpSession session = SecurityLayer.checkSession(request);
            if (session != null) {
                currentUser(request, response, session);
                if (request.getParameter("submitResearch") != null) {
                    action_research(request, response);
                }
                if(request.getParameter("userId") != null) {
                    User user = getDataLayer().getUser(Integer.parseInt(request.getParameter("userId")));
                    if(user != null){
                        request.setAttribute("publicationUser", user.getName() + " " + user.getSurname());
                        action_research(request, response);
                    }
                    
                }
                res.activate("research.ftl.html", request, response);
            } else {
                action_default(request, response);
            }
        } catch (Exception ex) {
            action_error(request, response, "OPS");
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
