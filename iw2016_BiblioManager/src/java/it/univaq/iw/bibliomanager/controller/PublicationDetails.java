/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.controller;

import it.univaq.iw.bibliomanager.data.model.History;
import it.univaq.iw.bibliomanager.data.model.Publication;
import it.univaq.iw.bibliomanager.data.model.Review;
import it.univaq.iw.framework.data.DataLayerException;
import it.univaq.iw.framework.result.TemplateResult;
import it.univaq.iw.framework.security.SecurityLayer;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Vincenzo Lanzieri, Angelo Iezzi
 */
public class PublicationDetails extends BiblioManagerBaseController {

    private void action_publication(HttpServletRequest request, HttpServletResponse response)
            throws DataLayerException, IOException, NumberFormatException, ServletException {
        TemplateResult res = new TemplateResult(getServletContext());
        int publicationKey = Integer.parseInt(request.getParameter("publicationId"));
        List<History> histories = getDataLayer().getHistoriesByPublication(publicationKey);
        for (History entry : histories) {
            if (entry.getType() == 0) {
                request.setAttribute("publisher", entry.getUser());
            }
        }
        request.setAttribute("histories", histories);
        Publication publication = getDataLayer().getPublication(publicationKey);
        this.action_viewReviews(request, response);
        request.setAttribute("publication", publication);
        List<Review> lastReviews = getDataLayer().getLastReviews(publicationKey, 1);
        request.setAttribute("lastReviews", lastReviews);
        res.activate("details.ftl.html", request, response);
    }

    private void action_viewReviews(HttpServletRequest request, HttpServletResponse response) throws DataLayerException {
        List<Review> reviews = getDataLayer().getReviews(Integer.parseInt(request.getParameter("publicationId")));
        request.setAttribute("reviews", reviews);
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
            request.setAttribute("page_title", "Details");
            HttpSession session = SecurityLayer.checkSession(request);
            if (session != null) {
                currentUser(request, response, session);
                action_publication(request, response);
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
