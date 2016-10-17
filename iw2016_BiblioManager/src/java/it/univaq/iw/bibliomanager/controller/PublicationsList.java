/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.controller;

import it.univaq.iw.bibliomanager.data.model.Publication;
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

/**
 *
 * @author Vincenzo Lanzieri, Angelo Iezzi
 */
public class PublicationsList extends BiblioManagerBaseController {

    private final String[] orderField = new String[]{"titolo", "e.nome", "a.cognome", "data_pubblicazione", "n_consigli"};
    private final String[] orderType = new String[]{"ASC", "DESC"};
    private Map<String, String> filters = new HashMap<>();
    private boolean isResearch = false;
    private final Map<Integer, String> pages = new HashMap<>();
    private final Map<String, Integer> options = new HashMap<>();
    
    private void action_list(HttpServletRequest request, HttpServletResponse response) {
        try {
            if (request.getAttribute("isResearch") != null && request.getAttribute("filter") != null) {
                isResearch = Boolean.parseBoolean(request.getAttribute("isResearch").toString());
                request.setAttribute("filter", request.getAttribute("filter"));
                filters = getDataLayer().getFilters(Integer.parseInt(request.getAttribute("filter").toString()));
            }
            else{
                isResearch = false;
                filters = new HashMap<>();
            }
            int orderBy = request.getParameter("orderBy") != null ? Integer.parseInt(request.getParameter("orderBy")) : 0;
            filters.put("order_by", orderField[orderBy]);
            int orderMode = request.getParameter("orderMode") != null ? Integer.parseInt(request.getParameter("orderMode")) : 0;
            filters.put("order_mode", orderType[orderMode]);
            request.setAttribute("orderBy", orderBy);
            request.setAttribute("orderMode", orderMode);
            filters.put("offset", Integer.toString(options.get("offset")));
            filters.put("limit", "1");

            List<Publication> publications = getDataLayer().getPublicationsByFilters(filters);
            request.setAttribute("publications", publications);
            filters.remove("limit");
            filters.remove("offset");
            request.setAttribute("isResearch", isResearch);
            action_view(request, response);
        } catch (DataLayerException ex) {
            action_error(request, response, "Unable to get the publications: " + ex.getMessage(), 502);
        }
    }
            
    private void action_view(HttpServletRequest request, HttpServletResponse response) throws DataLayerException {
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            
            request.setAttribute("totElements", getDataLayer().getPublicationsByFilters(filters).size());
            request.setAttribute("paginationUrl", "catalog");
            pagination(request, response, pages, options);
            
            res.activate("catalog.ftl.html", request, response);
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
            request.setAttribute("page_title", "Catalogo");
            HttpSession session = SecurityLayer.checkSession(request);
            if (session != null) {
                currentUser(request, response, session);
                if (request.getParameter("isResearch") != null && request.getParameter("filter") != null) {
                    request.setAttribute("isResearch", request.getParameter("isResearch"));
                    request.setAttribute("filter", request.getParameter("filter"));
                }
                if (request.getParameter("offset") != null){
                   options.put("offset", Integer.parseInt(request.getParameter("offset")));
                }
                else{
                   pages.clear();
                   options.put("limit", 5);
                   options.put("offset", 0);
                   options.put("slice", 10);
                   options.put("start", 0);
                   options.put("end", 10);
                }
                action_list(request, response);
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
