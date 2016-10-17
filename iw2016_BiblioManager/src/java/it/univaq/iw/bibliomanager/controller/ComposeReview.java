/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.controller;

import it.univaq.iw.bibliomanager.data.model.History;
import it.univaq.iw.bibliomanager.data.model.Review;
import it.univaq.iw.bibliomanager.data.model.User;
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
public class ComposeReview extends BiblioManagerBaseController {

    private final Map<Integer, String> pages = new HashMap<>();
    private final Map<String, Integer> options = new HashMap<>();
    
    private void action_view(HttpServletRequest request, HttpServletResponse response){
        try {
            request.setAttribute("page_title", "Gestione Recensioni");
            TemplateResult res = new TemplateResult(getServletContext());
            int publicationKey = Integer.parseInt(request.getParameter("publicationId"));
            List<Review> reviews = getDataLayer().getReviews(publicationKey, options.get("limit"), options.get("offset"));
            
            request.setAttribute("totElements", getDataLayer().getReviews(publicationKey, 0, 0).size());
            request.setAttribute("paginationUrl", "reprint");
            pagination(request, response, pages, options);
        
            request.setAttribute("reviews", reviews);
            request.setAttribute("publicationId", publicationKey);
            res.activate("review.ftl.html", request, response);
        } catch (DataLayerException | ServletException | IOException ex) {
            action_error(request, response, "Error build the template: " + ex.getMessage(), 511);
        }
    }
    
    private void action_composeReview(HttpServletRequest request, HttpServletResponse response) throws DataLayerException {
        try {
            Review review = getDataLayer().createReview();
            HttpSession session = SecurityLayer.checkSession(request);
            Map<String, String> params = new HashMap<String, String>();
            params.put("textReview", Utils.checkString(request.getParameter("textReview")));
            params.put("publicationId", request.getParameter("publicationId"));
            if (!validator(params, request, response)) {
                review.setText(params.get("textReview"));
                review.setAuthor(getDataLayer().getUser((int) session.getAttribute("userId")));
                review.setPublicationKey(Integer.parseInt(params.get("publicationId")));
                review.setStatus(false);
                review.setReviewDate(new java.sql.Timestamp(System.currentTimeMillis()));
                getDataLayer().storeReview(review);
            }
        } catch (DataLayerException ex) {
            action_error(request, response, "Errore nel salvare la recensione: " + ex.getMessage(), 510);
        }
    }

    private void action_confirmReview(HttpServletRequest request, HttpServletResponse response) throws DataLayerException {
        try {
            if (request.getParameter("reviewId") != null && request.getParameter("publicationId") != null) {
                HttpSession session = SecurityLayer.checkSession(request);
                Review review = getDataLayer().getReview(Integer.parseInt(request.getParameter("reviewId")));
                if (review.getAuthor().getKey() != (int) session.getAttribute("userId")) {
                    History history = this.action_composeHistory(request, response, "Recensione approvata");
                    review.setHistory(history);
                    review.setStatus(true);
                    request.setAttribute("reviewApproved", 1);
                    getDataLayer().storeReview(review);
                }
            } else {
                //TODO
                request.setAttribute("error", "Recensione non valida");
            }
        } catch (DataLayerException ex) {
            action_error(request, response, "Errore nel salvare la recensione: " + ex.getMessage(), 510);
        }
    }

    private void action_rejectReview(HttpServletRequest request, HttpServletResponse response) throws DataLayerException {
        try {
            if (request.getParameter("reviewId") != null && request.getParameter("publicationId") != null) {
                Review review = getDataLayer().getReview(Integer.parseInt(request.getParameter("reviewId")));
                HttpSession session = SecurityLayer.checkSession(request);
                if (review.getAuthor().getKey() != (int) session.getAttribute("userId")) {
                    request.setAttribute("reviewRejected", 1);
                    getDataLayer().deleteReview(review);
                }
            } else {
                //TODO
                request.setAttribute("error", "Recensione non valida");
            }
        } catch (DataLayerException ex) {
            action_error(request, response, "Errore nel salvare la recensione: " + ex.getMessage(), 510);
        }
    }

    private History action_composeHistory(HttpServletRequest request, HttpServletResponse response, String result) throws DataLayerException {
        HttpSession session = SecurityLayer.checkSession(request);
        User user = getDataLayer().getUser((int) session.getAttribute("userId"));
        History history = getDataLayer().createHistory();
        history.setEntry(result);
        history.setType(3);
        history.setUser(user);
        history.setPublicationKey(Integer.parseInt(request.getParameter("publicationId")));
        history.setDate(new java.sql.Timestamp(System.currentTimeMillis()));
        getDataLayer().storeHistory(history);
        return history;
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
                if (request.getParameter("addReview") != null) {
                    action_composeReview(request, response);
                }
                if (request.getParameter("r") != null) {
                    if (request.getParameter("r").equals("1")) {
                        action_confirmReview(request, response);
                    } else {
                        action_rejectReview(request, response);
                    }
                }
                if (request.getParameter("offset") != null){
                   options.put("offset", Integer.parseInt(request.getParameter("offset")));
                }
                else{
                   pages.clear();
                   options.put("limit", 7);
                   options.put("offset", 0);
                   options.put("slice", 10);
                   options.put("start", 0);
                   options.put("end", 10);
                }
                action_view(request, response);
            } else {
                action_default(request, response);
            }
        } catch (DataLayerException | IOException ex) {
            action_error(request, response, "Errore: " + ex.getMessage(), 501);
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
