/*
 * ATTENZIONE: questo � un context listener, e deve essere configurato come tale
 * nel file web.xml
 * 
 * ATTENZIONE: il codice di questa classe dipende dalla corretta definizione delle
 * risorse presente nei file context.xml (Resource) e web.xml (respurce-ref)
 * 
 * WARNING: this is a context listener, and must be registered in web.xml
 * 
 * WARNING: this class needs the definition of an external data source to work correctly.
 * See the Resource element in context.xml and the resource-ref element in web.xml
 * 
 */
package it.univaq.iw.bibliomanager;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

public class ContextInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            //preleviamo un riferimento al naming context
            //get a reference to the naming context
            InitialContext ctx = new InitialContext();
            //e da questo otteniamo un riferimento alla DataSource
            //che gestisce il pool di connessioni. 
            //ottenere il nome della sorgente dati 
            //ottenere il nome della sorgente dati 

            //and from the context we get a reference to the DataSource
            //that manages the connection pool
            //in a web application, we may  use a context parameter to obtain the data source name
            DataSource datasource = (DataSource) ctx.lookup(sce.getServletContext().getInitParameter("data.source"));
            sce.getServletContext().setAttribute("datasource", datasource);
        } catch (NamingException ex) {
            Logger.getLogger(ContextInitializer.class.getName()).log(Level.SEVERE, null, ex);       
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        //niente da fare
        //nothing to do
    }
}
