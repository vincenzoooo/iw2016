/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.controller;

import it.univaq.iw.framework.data.DataLayerException;
import it.univaq.iw.framework.result.TemplateResult;
import it.univaq.iw.framework.utils.Utils;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import it.univaq.iw.bibliomanager.data.model.User;

/**
 *
 * @author Vincenzo Lanzieri, Angelo Iezzi
 */
public class Reset extends BiblioManagerBaseController {

    private User user = null;
    private void action_reset(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, DataLayerException, NoSuchAlgorithmException {
            TemplateResult res = new TemplateResult(getServletContext());
            String newPassword = Utils.checkString(request.getParameter("password"));
            String newRePassword = Utils.checkString(request.getParameter("re-password"));
            if(newPassword.equals(newRePassword) && user != null){
                user.setPassword(Utils.SHA1(newPassword));
                getDataLayer().storeUser(user);
                action_redirect(request, response, "/login");
            }
            else{
                request.setAttribute("errorReset", "Le password non corrispondono");
                action_default(request, response);
            }
    }

    private void action_isUser(HttpServletRequest request, HttpServletResponse response)
            throws DataLayerException, MessagingException, ServletException, IOException {
        String email = Utils.checkString(request.getParameter("email"));
        User user = getDataLayer().getUser(email);
        if (user != null) {
            this.user = user;
        }
        else{
            request.setAttribute("errorReset", "L'email non è stata registrata");
        }
        action_default(request, response);
    }

    @Override
    protected void action_default(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DataLayerException {
        request.setAttribute("page_title", "Reset");
        TemplateResult res = new TemplateResult(getServletContext());
        if(user != null){
            request.setAttribute("showPass", "1");
        }
        res.activate("reset.ftl.html", request, response);
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
            if (request.getParameter("submitReset") != null){
                if (request.getParameter("email") != null) {
                    action_isUser(request, response);
                } 
                if (this.user != null) {
                    action_reset(request, response);
                }
            }
            else {
                user = null;
                action_default(request, response);
            }
        } catch (DataLayerException | IOException | MessagingException | NoSuchAlgorithmException | ServletException ex) {
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
