package com.imgu.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.imgu.data.MyImage;
import com.imgu.data.PMF;
import java.io.PrintWriter;

public class ImageServlet extends HttpServlet {

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        // Get the image representation
        PrintWriter out = res.getWriter();
        ServletFileUpload upload = new ServletFileUpload();
        FileItemIterator iter;
        FileItemStream imageItem = null;
        try {
            iter = upload.getItemIterator(req);
            imageItem = iter.next();
        } catch (Exception e) {
        }

        InputStream imgStream = imageItem.openStream();

        // construct our entity objects
        Blob imageBlob = new Blob(IOUtils.toByteArray(imgStream));
        MyImage myImage = new MyImage(imageItem.getName(), imageBlob);

        // persist image
        PersistenceManager pm = PMF.get().getPersistenceManager();
        pm.makePersistent(myImage);
        pm.close();

        // respond to query
        res.setContentType("text/plain");
        out.write(req.getServerName()+"/imgu?id=" + KeyFactory.keyToString(myImage.getId()));

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        String skey = req.getParameter("id");
        Key key = KeyFactory.stringToKey(skey);
        imageFor(key, resp);
    }

    void imageFor(Key key, HttpServletResponse res) throws IOException {
        // find desired image
        PersistenceManager pm = PMF.get().getPersistenceManager();
//	    Query query = pm.newQuery("select from MyImage " +
//	        "where name = nameParam " +
//	        "parameters String nameParam");
//	    List<MyImage> results = (List<MyImage>)query.execute(name);
//	    Blob image = results.iterator().next().getImage();
        Blob image = ((MyImage) pm.getObjectById(MyImage.class, key)).getImage();

        // serve the first image
        res.setContentType("image/jpeg");
        res.getOutputStream().write(image.getBytes());
    }
}
