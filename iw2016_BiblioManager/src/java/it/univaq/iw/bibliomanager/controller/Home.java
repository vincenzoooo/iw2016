/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.controller;

import it.univaq.iw.bibliomanager.data.model.BiblioManagerDataLayer;
import it.univaq.iw.framework.data.DataLayerException;
import it.univaq.iw.framework.result.TemplateResult;
import it.univaq.iw.framework.security.SecurityLayer;
import it.univaq.iw.framework.utils.Utils;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import it.univaq.iw.bibliomanager.data.model.User;
import it.univaq.iw.framework.result.TemplateManagerException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Angelo Iezzi
 */
public class Home extends BiblioManagerBaseController {
    
     private void action_default(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setAttribute("page_title", "Home");
        TemplateResult res = new TemplateResult(getServletContext());
        res.activate("home.ftl.html", request, response);
    }
     
    private void action_logged(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, DataLayerException {
        
        HttpSession s = SecurityLayer.checkSession(request);
        if(s!=null){
            request.setAttribute("page_title", "Homepage");
            // recupero lo userId e il nome
            int userId = (int) s.getAttribute("userid");
            boolean role = (boolean) s.getAttribute("userRole");
            System.out.println("userId: "+userId);
            User us = ((BiblioManagerDataLayer) request.getAttribute("datalayer")).getUser(userId);
            String nameLogged = SecurityLayer.stripSlashes(us.getName());
            request.setAttribute("nameLogged", nameLogged);
            request.setAttribute("usernameLogged", "Homepage");
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("role", role);
            
            res.activate("home.ftl.html", request, response);
        }
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
         try {
             action_default(request, response);
         } catch (IOException ex) {
             Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
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
    }// </editor-fold>

}
