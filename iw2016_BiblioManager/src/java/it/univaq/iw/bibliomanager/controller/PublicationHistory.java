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
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Vincenzo Lanzieri
 */
public class PublicationHistory extends BiblioManagerBaseController {

    private void action_history(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, DataLayerException {
        TemplateResult res = new TemplateResult(getServletContext());
        int publicationKey = Integer.parseInt(request.getParameter("publicationId"));
        List<History> histories = getDataLayer().getHistoriesByPublication(publicationKey);
        request.setAttribute("histories", histories);
        request.setAttribute("publicationId", request.getParameter("publicationId"));
        res.activate("history.ftl.html", request, response);//TODO: Definire pagina di storico
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
            if (SecurityLayer.checkSession(request) != null) {
                action_history(request, response);
            } else {
                action_default(request, response);
            }
        } catch (Exception ex) {
            action_error(request, response, "Error" + ex.getMessage());
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
