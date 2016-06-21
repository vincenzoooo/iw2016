package it.univaq.iw.bibliomanager.controller;

import it.univaq.iw.bibliomanager.data.impl.BiblioManagerDataLayerMysqlImpl;
import it.univaq.iw.bibliomanager.data.model.BiblioManagerDataLayer;
import it.univaq.iw.framework.data.DataLayerException;
import it.univaq.iw.framework.result.FailureResult;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 *
 * @author Vincenzo Lanzieri
 */
public abstract class BiblioManagerBaseController extends HttpServlet {

    public BiblioManagerDataLayer datalayer;    

    protected abstract void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException;

    private void processBaseRequest(HttpServletRequest request, HttpServletResponse response) {
        try {
            datalayer = new BiblioManagerDataLayerMysqlImpl((DataSource) getServletContext().getAttribute("datasource"));            
            datalayer.init();
            processRequest(request, response);
        } catch (Exception ex) {
            ex.printStackTrace(); //for debugging only
            (new FailureResult(getServletContext())).activate(
                    (ex.getMessage() != null || ex.getCause() == null) ? ex.getMessage() : ex.getCause().getMessage(), request, response);

        } finally {
            try {
                datalayer.destroy();
            } catch (DataLayerException ex) {
                Logger.getLogger(BiblioManagerBaseController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    protected void action_error(HttpServletRequest request, HttpServletResponse response, String message) {
        (new FailureResult(getServletContext())).activate(message, request, response);
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
