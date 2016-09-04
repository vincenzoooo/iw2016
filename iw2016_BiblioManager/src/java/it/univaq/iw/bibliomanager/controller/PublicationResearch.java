/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.controller;

import it.univaq.iw.framework.data.DataLayerException;
import it.univaq.iw.framework.result.TemplateResult;
import it.univaq.iw.framework.security.SecurityLayer;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Vincenzo Lanzieri
 */
public class PublicationResearch extends BiblioManagerBaseController {

    private void action_research(HttpServletRequest request, HttpServletResponse response){
        try {
            Map<String, Object> filters = new HashMap<String, Object>();
            String isbn = request.getParameter("isbn");
            if (isbn != null) {
                filters.put("isbn", isbn);
            }
            String title = request.getParameter("title");
            if (title != null) {
                filters.put("titolo", title);
            }
            String authorName = request.getParameter("authorName");
            if (authorName != null) {
                filters.put("autore_nome", authorName);
            }
            String authorSurname = request.getParameter("authorSurname");
            if (authorSurname != null) {
                filters.put("autore_cognome", authorSurname);
            }
            String date = request.getParameter("date");
            if (date != null) {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date formatDate = format.parse(date);
                filters.put("data", new java.sql.Date(formatDate.getTime()));
            }
            String keyword = request.getParameter("keyword");
            if (keyword != null) {
                filters.put("keyword_nome", keyword);
            }
            String language = request.getParameter("language");
            if (language != null) {
                filters.put("lingua", language);
            }
            filters.put("order_by", "titolo");
            request.setAttribute("publications", getDataLayer().getPublicationsByFilters(filters));
        }catch(ParseException ex){
            action_error(request, response, "Unable to parse the date: " + ex.getMessage());
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
            if (SecurityLayer.checkSession(request) != null) {
                if (request.getParameter("submitResearch") != null) {
                    action_research(request, response);
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
