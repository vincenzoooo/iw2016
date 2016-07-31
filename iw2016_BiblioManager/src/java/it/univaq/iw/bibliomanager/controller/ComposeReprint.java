/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.controller;

import it.univaq.iw.bibliomanager.data.model.Reprint;
import it.univaq.iw.framework.data.DataLayerException;
import it.univaq.iw.framework.security.SecurityLayer;
import it.univaq.iw.framework.utils.Utils;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Vincenzo Lanzieri
 */
public class ComposeReprint extends BiblioManagerBaseController {

    private Reprint action_composeReprint(HttpServletRequest request, HttpServletResponse response) throws NumberFormatException, ParseException {
        Reprint reprint = null;
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("number", Utils.checkString(request.getParameter("reprintNumber")));
            params.put("date", Utils.checkString(request.getParameter("reprintDate")));
            if (!validator(params, request, response)) {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date date = format.parse(params.get("date"));
                reprint = getDataLayer().createReprint();
                reprint.setNumber(Integer.parseInt(params.get("number")));
                reprint.setDate(new java.sql.Date(date.getTime()));
                getDataLayer().storeReprint(reprint);
            }
        } catch (NumberFormatException | ParseException | DataLayerException ex) {
            action_error(request, response, "Errore nell'elaborare i dati: " + ex.getMessage());
        }
        return reprint;
    }

    private Reprint action_updateReprint(HttpServletRequest request, HttpServletResponse response) throws NumberFormatException, ParseException {
        Reprint reprint = null;
        try {
            reprint = getDataLayer().getReprint(Integer.parseInt("idreprint"));
            Map<String, String> params = new HashMap<String, String>();
            params.put("number", Utils.checkString(request.getParameter("reprintNumber")));
            params.put("date", Utils.checkString(request.getParameter("reprintDate")));
            if (!validator(params, request, response)) {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date date = format.parse(params.get("date"));
                reprint.setNumber(Integer.parseInt(params.get("number")));
                reprint.setDate(new java.sql.Date(date.getTime()));
                getDataLayer().storeReprint(reprint);
            }
        } catch (NumberFormatException | ParseException | DataLayerException ex) {
            action_error(request, response, "Errore nell'elaborare i dati: " + ex.getMessage());
        }
        return reprint;
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
            request.setAttribute("page_title", "Gestione Ristampa");
            if (SecurityLayer.checkSession(request) != null) {
                if (request.getParameter("idreprint") != null) {
                    Reprint reprint = getDataLayer().getReprint(Integer.parseInt(request.getParameter("idreprint")));
                    request.setAttribute("reprint", reprint);
                }
                if (request.getParameter("submitReprint") != null && request.getParameter("idreprint") != null) {
                    action_updateReprint(request, response);
                }
                //TODO: Verificarne la correttezza
                if (request.getParameter("submitReprint") != null && request.getParameter("idreprint") == null) {
                    action_composeReprint(request, response);
                }
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
