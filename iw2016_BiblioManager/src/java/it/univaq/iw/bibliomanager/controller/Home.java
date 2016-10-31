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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import it.univaq.iw.bibliomanager.data.model.User;
import java.util.List;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Vincenzo Lanzieri, Angelo Iezzi
 */
public class Home extends BiblioManagerBaseController {

    /**
     * Compile the template for display it
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     * @throws DataLayerException
     */
    private void action_view(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, DataLayerException {
        request.setAttribute("page_title", "Homepage");
        TemplateResult res = new TemplateResult(getServletContext());
        request.setAttribute("script", "script/ramdombox.js");
        List<Publication> publicationsInsert = getDataLayer().getLastInsertedPublication();
        List<Publication> publicationsUpdate = getDataLayer().getLastModifiedPublication();
        List<User> activeUsers = getDataLayer().getMoreActiveUsers();
        request.setAttribute("lastPublicationsInsert", publicationsInsert);
        request.setAttribute("lastPublicationsUpdate", publicationsUpdate);
        request.setAttribute("activeUsers", activeUsers);
        res.activate("home.ftl.html", request, response);
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
                getDataLayer().deleteFilters();
                action_view(request, response);
            } else {
                action_view(request, response);
            }
        } catch (DataLayerException | IOException | ServletException ex) {
            action_error(request, response, "Error: " + ex.getMessage(), 501);
        }
    }
}
