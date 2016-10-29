/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.controller;

import it.univaq.iw.bibliomanager.data.model.History;
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
 * @author Vincenzo Lanzieri, Angelo Iezzi
 */
public class PublicationHistory extends BiblioManagerBaseController {

    /**
     * Pagine
     */
    private final Map<Integer, String> pages = new HashMap<>();
    /**
     * Opzioni di paginazione
     */
    private final Map<String, Integer> options = new HashMap<>();
    
    /**
     * Compila i template da restituire a video
     * @param request
     * @param response 
     */
    private void action_view(HttpServletRequest request, HttpServletResponse response){
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            int publicationKey = Integer.parseInt(request.getParameter("publicationId"));
            List<History> histories = getDataLayer().getHistoriesByPublication(publicationKey, options.get("limit"), options.get("offset"));
            
            request.setAttribute("totElements", getDataLayer().getHistoriesByPublication(publicationKey, 0, 0).size());
            request.setAttribute("paginationUrl", "reprint");
            pagination(request, response, pages, options);
            
            request.setAttribute("histories", histories);
            request.setAttribute("publicationId", publicationKey);
            res.activate("history.ftl.html", request, response);
        } catch (DataLayerException | ServletException | IOException ex) {
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
            request.setAttribute("page_title", "History");
            HttpSession session = SecurityLayer.checkSession(request);
            if (session != null) {
                currentUser(request, response, session);
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
            action_error(request, response, "Error" + ex.getMessage(), 501);
        }
    }
}
