//https://api.le-systeme-solaire.net/en/

package com.example.solarsystem_webservice;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

@WebServlet(name = "SolarSystem", value = "/*")
public class SolarSystem extends HttpServlet {

    // TODO: 27/05/2022 fix unordered response xml tags

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        URL urlRequest;
        // save URL request
        String url = request.getRequestURL().toString().toLowerCase();

        //check the request type
        if (url.endsWith("body")) {
            try {

                //save body id contained in URL
                // Note : request.getParameter("id") may return 'null' value
                String idBody = request.getParameter("id");

                //checks if 'id' parameter is empty
                if (idBody == null || idBody.isEmpty()) {

                    // send error
                    response.sendError(400, "Missing parameter");
                    return;
                }

                // create the request URL towards open data web service encoding "idBody" value according UTF-8 standard
                urlRequest = new URL(RequestHandler.idbody_URL + URLEncoder.encode(idBody, "UTF-8"));

                //request data about body
                //check for some issue during comunication with open data web service
                String xmlResponse = RequestHandler.getInfo(urlRequest);
                if (xmlResponse==null){

                    //try another request then 1 second
                    TimeUnit.SECONDS.sleep(1);

                    if ((xmlResponse=RequestHandler.getInfo(urlRequest))==null){
                        // send error
                        response.sendError(400, "No connection towards to dataset");
                        return ;
                    }
                }
                response.setStatus(200);
                PrintWriter printResponse = response.getWriter();
                printResponse.write(xmlResponse);
                printResponse.flush();
                printResponse.close();


            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } else if (url.endsWith("list")) {
            try {

                //save body id contained in URL
                // Note : request.getParameter("id") may return 'null' value
                String bodyType = request.getParameter("type");

                //checks if 'id' parameter is empty
                if (bodyType == null || bodyType.isEmpty()) {

                    // send error
                    response.sendError(400, "Missing parameter");
                    return;
                }

                // create the request URL towards open data web service encoding "idBody" value according UTF-8 standard
                urlRequest = new URL(RequestHandler.bodyType_URL + URLEncoder.encode(bodyType, "UTF-8"));

                //request data about body
                //check for some issue during comunication with open data web service

                String xmlResponse = RequestHandler.getBodyType(urlRequest);
                if (xmlResponse==null){

                    //try another request then 1 second
                    TimeUnit.SECONDS.sleep(1);

                    if ((xmlResponse=RequestHandler.getBodyType(urlRequest))==null){
                        // send error
                        response.sendError(400, "No connection towards to dataset");
                        return ;
                    }
                }

                response.setStatus(200);
                PrintWriter printResponse = response.getWriter();
                printResponse.write(xmlResponse);
                printResponse.flush();
                printResponse.close();


            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            response.sendError(400, "Bad request");
        }
    }

}