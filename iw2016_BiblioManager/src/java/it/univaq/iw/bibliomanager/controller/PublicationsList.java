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
 * @author Vincenzo Lanzieri, Angelo Iezzi
 */
public class PublicationsList extends BiblioManagerBaseController {

    private final String[] orderField = new String[]{"titolo", "e.nome", "a.cognome", "data_pubblicazione", "n_consigli"};
    private final String[] orderType = new String[]{"ASC", "DESC"};
    private Map<String, String> filters = new HashMap<>();
    private boolean isResearch = false;
    private final int limit = 5;

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
            int offset = request.getParameter("offset") != null ? Integer.parseInt(request.getParameter("offset")) : 0;
            request.setAttribute("orderBy", orderBy);
            request.setAttribute("orderMode", orderMode);
            filters.put("offset", Integer.toString(offset));
            filters.put("limit", "1");

            List<Publication> publications = getDataLayer().getPublicationsByFilters(filters);
            request.setAttribute("publications", publications);
            filters.remove("limit");
            filters.remove("offset");
            int publicationsNumber = getDataLayer().getPublicationsByFilters(filters).size();
            int pages = publicationsNumber / limit;
            if (pages != 0 && publicationsNumber % limit > 0) {
                pages++;
            }
            String[] urlPages = new String[pages];
            offset = (pages - 1) * limit;
            while (pages > 0) {
                String url = "catalog?orderBy=" + orderBy + "&offset=" + offset;
                if (request.getAttribute("isResearch") != null && request.getAttribute("filter") != null) {
                    url += "&isResearch=" + request.getAttribute("isResearch").toString()+"&filter="+request.getAttribute("filter").toString();
                }
                urlPages[--pages] = url;
                offset -= limit;
            }
            request.setAttribute("isResearch", isResearch);
            request.setAttribute("pages", urlPages);
            action_view(request, response);
        } catch (DataLayerException ex) {
            action_error(request, response, "Unable to get the publications: " + ex.getMessage());
        }
    }

    private void action_view(HttpServletRequest request, HttpServletResponse response) {
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            res.activate("catalog.ftl.html", request, response);
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
            request.setAttribute("page_title", "Catalogo");
            HttpSession session = SecurityLayer.checkSession(request);
            if (session != null) {
                currentUser(request, response, session);
                if (request.getParameter("isResearch") != null && request.getParameter("filter") != null) {
                    request.setAttribute("isResearch", request.getParameter("isResearch"));
                    request.setAttribute("filter", request.getParameter("filter"));
                }
                action_list(request, response);
            } else {
                action_default(request, response);
            }
        } catch (DataLayerException | IOException | ServletException ex) {
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
