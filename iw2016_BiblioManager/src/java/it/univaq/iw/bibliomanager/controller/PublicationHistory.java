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

    private final Map<Integer, String> pages = new HashMap<>();
    private final Map<String, Integer> options = new HashMap<>();
    
    private void action_view(HttpServletRequest request, HttpServletResponse response){
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            int publicationKey = Integer.parseInt(request.getParameter("publicationId"));
            List<History> histories = getDataLayer().getHistoriesByPublication(publicationKey, options.get("limit"), options.get("offset"));
            int historiesNumber = getDataLayer().getHistoriesByPublication(publicationKey, 0, 0).size();
            if(historiesNumber < options.get("end")){
                options.put("end", historiesNumber);
            }
            int pageNumber = historiesNumber / options.get("limit");
            if(pageNumber < options.get("slice")){
                options.put("slice", pageNumber+1);
            }
            if (pageNumber != 0 && historiesNumber % options.get("limit") > 0) {
                pageNumber++;
            }
            int totOffset = (pageNumber - 1) * options.get("limit");
            for (int i = pageNumber-1; i >= 0; --i) {
                String url = "history?publicationId=" + publicationKey + "&offset=" + totOffset;
                pages.put(i, url);
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
