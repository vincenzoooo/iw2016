/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.controller;

import it.univaq.iw.bibliomanager.data.model.Reprint;
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
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Vincenzo Lanzieri, Angelo Iezzi;
 */
public class ComposeReprint extends BiblioManagerBaseController {

    private int publicationId;
    private String url;
    private int reprintId;
    private Reprint currentReprint;
    private final Map<Integer, String> pages = new HashMap<>();
    private final Map<String, Integer> options = new HashMap<>();
    
    private void action_composeReprint(HttpServletRequest request, HttpServletResponse response) throws NumberFormatException, ParseException {
        try {
            Reprint reprint = getDataLayer().createReprint();
            Map<String, String> params = new HashMap<String, String>();
            params.put("reprintNumber", Utils.checkString(request.getParameter("reprintNumber")));
            params.put("reprintDate", Utils.checkString(request.getParameter("reprintDate")));
            if (!validator(params, request, response)) {
                reprint.setNumber(Integer.parseInt(params.get("reprintNumber")));
                DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                java.util.Date date = format.parse(params.get("reprintDate"));
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                reprint.setDate(sqlDate);
                reprint.setPublicationKey(publicationId);
                getDataLayer().storeReprint(reprint);
                request.setAttribute("reprintAdded",1);
            }
        } catch (NumberFormatException | ParseException | DataLayerException ex) {
            action_error(request, response, "Errore nel salvare la ristampa: " + ex.getMessage(), 510);
        }
    }

    private void action_updateReprint(HttpServletRequest request, HttpServletResponse response) throws NumberFormatException, ParseException {
        try {
            Reprint reprint = getDataLayer().getReprint(reprintId);
            Map<String, String> params = new HashMap<String, String>();
            params.put("reprintNumber", Utils.checkString(request.getParameter("reprintNumber")));
            params.put("reprintDate", Utils.checkString(request.getParameter("reprintDate")));
            if (!validator(params, request, response)) {
                reprint.setNumber(Integer.parseInt(params.get("reprintNumber")));
                DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                java.util.Date date = format.parse(params.get("reprintDate"));
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                reprint.setDate(sqlDate);
                getDataLayer().storeReprint(reprint);
                request.setAttribute("reprintUpdated",1);
                reprintId = 0;
            }
        } catch (NumberFormatException | ParseException | DataLayerException ex) {
            action_error(request, response, "Errore nel salvare la ristampa:: " + ex.getMessage(), 510);
        }
    }

    @Override
    protected boolean validator(Map<String, String> params, HttpServletRequest request, HttpServletResponse response) {
        boolean error = super.validator(params, request, response);
        if (!error) {
                if (!Utils.isNumeric(params.get("reprintNumber"))) {
                    request.setAttribute("errorReprintNumber", "Non è un numero valido");
                    error = true;
                }
                if (!Utils.isDate(params.get("reprintDate"))) {
                    request.setAttribute("errorReprintDate", "Non è una data valida, si aspetta il formato dd-mm-yyyy");
                    error = true;
                }
        }
        return error;
    }
    
    private void action_view(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DataLayerException {
        request.setAttribute("page_title", "Gestione Ristampa");
        TemplateResult res = new TemplateResult(getServletContext());
        List<Reprint> reprints = getDataLayer().getReprints(publicationId, options.get("limit"), options.get("offset"));
        
        request.setAttribute("totElements", getDataLayer().getReprints(publicationId, 0, 0).size());
        request.setAttribute("paginationUrl", "reprint");
        pagination(request, response, pages, options);
        
        request.setAttribute("reprints", reprints);
        request.setAttribute("publicationId", publicationId);
        request.setAttribute("url", url);
        request.setAttribute("reprintId", reprintId);
        request.setAttribute("reprint", currentReprint);
        res.activate("reprint.ftl.html", request, response);
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
                if(request.getAttribute("publicationId") != null){
                    publicationId = (int) request.getAttribute("publicationId");
                }
                if(request.getAttribute("url") != null){
                    url = (String) request.getAttribute("url");
                }
                if (request.getParameter("reprintId") != null) {
                    reprintId = Integer.parseInt(request.getParameter("reprintId"));
                    currentReprint = getDataLayer().getReprint(reprintId);
                }
                if (request.getParameter("submitReprint") != null){
                    if(reprintId == 0) {
                        action_composeReprint(request, response);
                    }
                    else{
                        action_updateReprint(request, response);
                    }
                }
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
        } catch (DataLayerException | IOException | NumberFormatException | ParseException | ServletException ex) {
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
