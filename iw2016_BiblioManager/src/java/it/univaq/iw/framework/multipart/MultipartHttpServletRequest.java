/*
 * Questa classe, derivata da HttpServletRequestWrapper, estende la normale HttpServletRequest
 * per trattare in maniera trasparente le richieste di tipo multipart utilizzando
 * Apache FileUpload.
 * I parametri normali vengono presentati tramite la normale interfaccia getParameter,
 * come se provenisseto da una form con codifica url, mentre i metodi specifici
 * getStream/writeFile vengono utilizzati per leggere/copiare il file associato 
 * ai campi di tipo file. Inoltre, vengono aggiunti* i tre parametri X_name, 
 * X_type e X_size, dove X � il nome del campo di tipo file,
 * contenenti rispettivamente il nome, il tipo mime e la dimensione del file inviato.
 * 
 * This class, derived from HttpServletRequestWrapper, extends the standard HttpServletRequest
 * to transparently handle multipart encoded requests using Apache FileUpload.
 * The non-file parameters are exposed by this class throgh the standard getParameter method
 * (i.e., as they were passed with a url encoded submission), whereas the getStream/writeFile
 * methods are used to read/copy the files associated to the file form fields. Moreover,
 * we add three special parameters to the request: X_name, X_type e X_size, where X is
 * the name of a file form field, which allow to easily read the file name, its media type
 * and its size, respectively.
 *
 */
package it.univaq.iw.framework.multipart;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class MultipartHttpServletRequest extends HttpServletRequestWrapper {

    private Map<String, String[]> postparams;
    private Map<String, FileItem[]> postfiles;

    public MultipartHttpServletRequest(HttpServletRequest request) {
        super(request);
        postparams = new HashMap();
        postfiles = new HashMap();
        parsePOSTParameters(request);
    }

    private void parsePOSTParameters(HttpServletRequest request) {
        if (ServletFileUpload.isMultipartContent(request)) {
            try {
                FileItemFactory factory = new DiskFileItemFactory();
                ServletFileUpload upload = new ServletFileUpload(factory);
                List<FileItem> items;
                items = upload.parseRequest(request);
                for (FileItem item : items) {
                    if (item.isFormField()) {
                        addToParameterMap(item.getFieldName(), item.getString());
                    } else {
                        addToParameterMap(item.getFieldName(), "$STREAM$");
                        addToParameterMap(item.getFieldName() + "_type", item.getContentType());
                        addToParameterMap(item.getFieldName() + "_size", String.valueOf(item.getSize()));
                        addToParameterMap(item.getFieldName() + "_name", item.getName());
                        addToStreamMap(item.getFieldName(), item);
                    }
                }
            } catch (FileUploadException ex) {
                Logger.getLogger(MultipartHttpServletRequest.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            postparams = request.getParameterMap();
        }
    }

    private void addToParameterMap(String key, String value) {
        String[] arrvalue = {value};
        if (!postparams.containsKey(key)) {
            postparams.put(key, arrvalue);
        } else {
            String[] oldarrvalue = postparams.get(key);
            String[] newarrvalue = new String[oldarrvalue.length + 1];
            System.arraycopy(oldarrvalue, 0, newarrvalue, 0, oldarrvalue.length);
            System.arraycopy(arrvalue, 0, newarrvalue, oldarrvalue.length, arrvalue.length);
            postparams.put(key, newarrvalue);
        }
    }

    private void addToStreamMap(String key, FileItem value) {
        FileItem[] arrvalue = {value};
        if (!postfiles.containsKey(key)) {
            postfiles.put(key, arrvalue);
        } else {
            FileItem[] oldarrvalue = postfiles.get(key);
            FileItem[] newarrvalue = new FileItem[oldarrvalue.length + 1];
            System.arraycopy(oldarrvalue, 0, newarrvalue, 0, oldarrvalue.length);
            System.arraycopy(arrvalue, 0, newarrvalue, oldarrvalue.length, arrvalue.length);
            postfiles.put(key, newarrvalue);
        }
    }

    ///Metodi di HttpServletRequest
    //Li reimplementiamo basandoci sulla mappa dei parametri
    //costruita all'interno del nostro wrapper
    //TUTTI i metodi di HttpServletRequest non sovrascritti qui
    //verranno semplicemente ridiretti sugli omonimi metodi
    //della HttpServletRequest inserita nel wrapper (dal costruttore)
    
    //HttpServletRequest methods
    //These methods are implemented using the parameter map
    //contained in this wrapper
    //ALL the not-overridden methods are automatically redirected
    //to the corresponding methods of the enclosed HttpServletRequest
    @Override
    public String getParameter(String name) {
        if (postparams.containsKey(name)) {
            return postparams.get(name)[0];
        } else {
            return null;
        }
    }

    @Override
    public Map getParameterMap() {
        return postparams;
    }

    @Override
    public Enumeration getParameterNames() {
        return Collections.enumeration(postparams.keySet());
    }

    @Override
    public String[] getParameterValues(String name) {
        return postparams.get(name);
    }
    
    ///Metodi specifici della nostra MultipartHttpServletRequest
    //Per poterli utilizzare, la servlet dovr� necessariamente
    //fare un cast della HttpServletRequest riucevuta
    
    //MultipartHttpServletRequest specific methods
    //To use these methods, servlets must cast their
    //HttpServletRequest to a MultipartHttpServletRequest
    public InputStream[] getStreams(String name) {
        InputStream[] poststreams = new InputStream[postfiles.get(name).length];
        for (int i = 0; i < postfiles.get(name).length; ++i) {
            try {
                poststreams[i] = postfiles.get(name)[i].getInputStream();
            } catch (IOException ex) {
                Logger.getLogger(MultipartHttpServletRequest.class.getName()).log(Level.SEVERE, null, ex);
                poststreams[i] = null;
            }
        }
        return poststreams;
    }

    public InputStream getStream(String name) {
        if (postfiles.containsKey(name)) {
            try {
                return postfiles.get(name)[0].getInputStream();
            } catch (IOException ex) {
                Logger.getLogger(MultipartHttpServletRequest.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        } else {
            return null;
        }
    }

    public void writeFiles(String name, File f) throws Exception {
        String filename = f.getName();
        String extension;
        int extpos = filename.lastIndexOf('.');
        if (extpos > 0) {
            extension = filename.substring(extpos);
            filename = filename.substring(0, extpos);
        } else {
            extension = "";
        }
        for (int i = 0; i < postfiles.get(name).length; ++i) {
            File ff = new File(f.getParent() + File.separatorChar + filename + "_" + i + extension);
            postfiles.get(name)[i].write(ff);
        }
    }

    public void writeFile(String name, File f) throws Exception {
        postfiles.get(name)[0].write(f);
    }
}
