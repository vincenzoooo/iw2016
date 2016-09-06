package it.univaq.iw.bibliomanager.controller;

import it.univaq.iw.bibliomanager.data.impl.BiblioManagerDataLayerMysqlImpl;
import it.univaq.iw.bibliomanager.data.model.BiblioManagerDataLayer;
import it.univaq.iw.bibliomanager.data.model.User;
import it.univaq.iw.framework.data.DataLayerException;
import it.univaq.iw.framework.result.FailureResult;
import it.univaq.iw.framework.result.SplitSlashesFmkExt;
import it.univaq.iw.framework.result.TemplateResult;
import it.univaq.iw.framework.security.SecurityLayer;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

/**
 *
 * @author Vincenzo Lanzieri, Angelo Iezzi
 */
public abstract class BiblioManagerBaseController extends HttpServlet {

    private BiblioManagerDataLayer datalayer;

    protected abstract void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException;

    private void processBaseRequest(HttpServletRequest request, HttpServletResponse response) {
        try {
            datalayer = new BiblioManagerDataLayerMysqlImpl((DataSource) getServletContext().getAttribute("datasource"));
            datalayer.init();
            HttpSession s = SecurityLayer.checkSession(request);
            if (s!=null){
               currentUser(request, response, s);      
           }
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            //SecurityLayer.redirectToHttps(request, response);
            
            //WARNING: never declare DB-related objects including references to Connection and Statement (as our data layer)
            //as class variables of a servlet. Since servlet instances are reused, concurrent requests may conflict on such
            //variables leading to unexpected results. To always have different connections and statements on a per-request
            //(i.e., per-thread) basis, declare them in the doGet, doPost etc. (or in methods called by them) and 
            //(possibly) pass such variables through the request.
            request.setAttribute("datalayer", datalayer);
            processRequest(request, response);
        } catch (Exception ex) {
            ex.printStackTrace(); //for debugging only
            (new FailureResult(getServletContext())).activate(
                    (ex.getMessage() != null || ex.getCause() == null) ? ex.getMessage() : ex.getCause().getMessage(), request, response);
        } finally {
            if (datalayer != null) {
                try {
                    datalayer.destroy();
                    datalayer = null;
                } catch (DataLayerException ex) {
                    Logger.getLogger(BiblioManagerBaseController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            /*
            try {
                datalayer.destroy();
            } catch (DataLayerException ex) {
                Logger.getLogger(BiblioManagerBaseController.class.getName()).log(Level.SEVERE, null, ex);
            }
            */
        }
    }
    
    protected void currentUser(HttpServletRequest request, HttpServletResponse response, HttpSession session){
        try {
            int userId = (int) session.getAttribute("userId");
            int status = (int) session.getAttribute("userStatus");
            User user = getDataLayer().getUser(userId);
            request.setAttribute("me", user);
        } catch (DataLayerException ex) {
          
        }
    }
    
    protected void action_error(HttpServletRequest request, HttpServletResponse response, String message) {
        (new FailureResult(getServletContext())).activate(message, request, response);
    }

    protected void action_default(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("page_title", "Login to Biblio");
        TemplateResult res = new TemplateResult(getServletContext());
        res.activate("login.ftl.html", request, response);
    }
    
    protected void action_redirect(HttpServletRequest request, HttpServletResponse response, String url) throws ServletException, IOException {
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(url);
        dispatcher.forward(request, response);
    }
    protected boolean validator(Map<String, String> params, HttpServletRequest request, HttpServletResponse response) {
        boolean error = false;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (entry.getValue() == null || entry.getValue().equals("")) {
                String fieldName = Character.toUpperCase(entry.getKey().charAt(0)) + entry.getKey().substring(1);
                request.setAttribute("error" + fieldName, "Non valorizzato");
                error = true;
            }
        }
        return error;
    }

    public BiblioManagerDataLayer getDataLayer() {
        return datalayer;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processBaseRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processBaseRequest(request, response);
    }

}
