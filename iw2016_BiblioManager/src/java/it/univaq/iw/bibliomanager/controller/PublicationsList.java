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
 * @author Vincenzo Lanzieri
 */
public class PublicationsList extends BiblioManagerBaseController {
    private final String[] orderField = new String[]{"titolo", "e.nome", "a.cognome", "data_pubblicazione", "n_consigli"};
    private final String[] orderType = new String[]{"ASC", "DESC"};
    private void action_list(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, String> filters = new HashMap<String, String>();
        int orderBy = request.getParameter("orderBy") != null ? Integer.parseInt(request.getParameter("orderBy")) : 0;
        filters.put("order_by", orderField[orderBy]);
        int orderMode = request.getParameter("orderMode") != null ? Integer.parseInt(request.getParameter("orderMode")) : 0;
        filters.put("order_mode", orderType[orderMode]);       
        request.setAttribute("orderBy", orderBy);
        request.setAttribute("orderMode", orderMode);
        TemplateResult res = new TemplateResult(getServletContext());
        try {
            if(request.getAttribute("publications") == null){
                List<Publication> publications = getDataLayer().getPublicationsByFilters(filters);
                request.setAttribute("publications", publications);
            }
            res.activate("catalog.ftl.html", request, response);
        } catch (ServletException | DataLayerException ex) {
            action_error(request, response, "Unable to get the publications: " + ex.getMessage());
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
                action_list(request, response);
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
