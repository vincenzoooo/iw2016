/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.controller;

import it.univaq.iw.bibliomanager.data.model.Reprint;
import it.univaq.iw.bibliomanager.data.model.Source;
import it.univaq.iw.framework.data.DataLayerException;
import it.univaq.iw.framework.result.TemplateResult;
import it.univaq.iw.framework.security.SecurityLayer;
import it.univaq.iw.framework.utils.Utils;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Vincenzo Lanzieri, Angelo Iezzi;
 */
public class ComposeReprint extends BiblioManagerBaseController {

    private void action_composeReprint(HttpServletRequest request, HttpServletResponse response) throws NumberFormatException, ParseException {
        try {
            Reprint reprint = getDataLayer().createReprint();
            Map<String, String> params = new HashMap<String, String>();
            params.put("reprintNumber", Utils.checkString(request.getParameter("reprintNumber")));
            params.put("reprintDate", Utils.checkString(request.getParameter("reprintDate")));
            if (!validator(params, request, response)) {
                reprint.setNumber(Integer.parseInt(params.get("reprintNumber")));
                DateFormat format = new SimpleDateFormat("dd-mm-yyyy", Locale.ITALIAN);
                java.util.Date date = format.parse(params.get("reprintDate"));
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                reprint = getDataLayer().createReprint();
                reprint.setNumber(Integer.parseInt(params.get("reprintNumber")));
                reprint.setDate(sqlDate);
                getDataLayer().storeReprint(reprint);
            }
        } catch (NumberFormatException | ParseException | DataLayerException ex) {
            action_error(request, response, "Errore nel salvare la ristampa: " + ex.getMessage());
        }
    }

    private void action_updateReprint(HttpServletRequest request, HttpServletResponse response) throws NumberFormatException, ParseException {
        Reprint reprint = null;
        try {
            reprint = getDataLayer().getReprint(Integer.parseInt("reprintId"));
            Map<String, String> params = new HashMap<String, String>();
            params.put("reprintNumber", Utils.checkString(request.getParameter("reprintNumber")));
            params.put("reprintDate", Utils.checkString(request.getParameter("reprintDate")));
            if (!validator(params, request, response)) {
                reprint.setNumber(Integer.parseInt(params.get("reprintNumber")));
                DateFormat format = new SimpleDateFormat("dd-mm-yyyy", Locale.ITALIAN);
                java.util.Date date = format.parse(params.get("reprintDate"));
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                reprint.setDate(sqlDate);
                getDataLayer().storeReprint(reprint);
            }
        } catch (NumberFormatException | ParseException | DataLayerException ex) {
            action_error(request, response, "Errore nel salvare la ristampa:: " + ex.getMessage());
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
            request.setAttribute("page_title", "Gestione Ristampa");
            TemplateResult res = new TemplateResult(getServletContext());
            if (SecurityLayer.checkSession(request) != null) {
                if (request.getParameter("reprintId") != null) {
                    Reprint reprint = getDataLayer().getReprint(Integer.parseInt(request.getParameter("reprintId")));
                    request.setAttribute("reprint", reprint);
                    res.activate("reprint.ftl.html", request, response);//DA impostare il nome effettivamente usato
                }
                if (request.getParameter("submitReprint") != null && request.getParameter("reprintId") != null) {
                    action_updateReprint(request, response);
                    request.removeAttribute("reprintId");
                }
                if (request.getParameter("submitReprint") != null && request.getParameter("reprintId") == null) {
                    action_composeReprint(request, response);
                }
                if (request.getParameter("publicationId") !=null){
                    List<Reprint> reprints = getDataLayer().getReprints(Integer.parseInt(request.getParameter("publicationId")));
                    request.setAttribute("reprints", reprints);
                }
                res.activate("reprint.ftl.html", request, response);
            } else {
                action_default(request, response);
            }
        } catch (Exception ex) {
            action_error(request, response, "Errore: " + ex.getMessage());
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
