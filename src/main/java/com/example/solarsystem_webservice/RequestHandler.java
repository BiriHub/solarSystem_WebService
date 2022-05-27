
//https://api.le-systeme-solaire.net/en/

package com.example.solarsystem_webservice;

import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;

public class RequestHandler {

    // Open data powered by 'Le Système solaire à portée de votre souris' --> https://api.le-systeme-solaire.net/en/


    //first get
    public static final String idbody_URL = "https://api.le-systeme-solaire.net/rest/bodies/"; //+ idbody

    //second get
    public static final String bodyType_URL = "https://api.le-systeme-solaire.net/rest.php/bodies?filter[]=bodyType,eq,";

    //create a document from an 'InputStream'
    private static Element getDocument(InputStream request) {
        // parsing dell'xml ricevuto con il metodo POST
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        Document document = null;
        try {
            builder = factory.newDocumentBuilder();
            //Salvo l'xml inviato dal client
            document = builder.parse(request);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return document.getDocumentElement();

    }

    public static String getInfo(URL urlRequest) {

        HttpsURLConnection service;
        String xmlResponse = null;

        //stato della richiesta ( es 200, 404 ecc.)
        int status;

        try {
            //set up connection to web service
            service = (HttpsURLConnection) urlRequest.openConnection();

            //set property of the request
            //require header setting related to interrogated host
            service.setRequestProperty("Host", "le-systeme-solaire.net");

            //require header setting related to response format (JSON)
            service.setRequestProperty("Accept", "application/json");

            //required header setting related to response encoding (UTF-8)
            service.setRequestProperty("Accept-Charset", "UTF-8");

            //set request method to 'GET'
            service.setRequestMethod("GET");

            //receive activation
            service.setDoInput(true);

            //establish the connection
            service.connect();

            status = service.getResponseCode();

            //check the status code
            if (status != 200) {
                //error
                return null;
            }

            //create xml string from json response
            BufferedReader input = new BufferedReader(new InputStreamReader(service.getInputStream(), "UTF-8"));
            StringBuffer jsonString = new StringBuffer();
            String line;

            if ((line = input.readLine()) != null) {
                JSONObject json = new JSONObject(line);
                xmlResponse = XML.toString(json,"body");
            }



            // XML parsing
            Element root = getDocument(service.getInputStream());


            // TODO: 27/05/2022 end xml parsing in order to follow Web API documentatio (It's also needed for 'getBodyType' method)
            // TODO: 27/05/2022 check if 'toXMLString' method od 'Body' class works 
            // TODO: 27/05/2022 start to build up the android application 
            


        } catch (IOException e) {
            e.printStackTrace();
        }



        return xmlResponse;
    }

    public static String getBodyType(URL urlRequest) {

        HttpsURLConnection service;
        String xmlResponse = null;

        //stato della richiesta ( es 200, 404 ecc.)
        int status;

        try {
            //set up connection to web service
            service = (HttpsURLConnection) urlRequest.openConnection();

            //set property of the request
            //require header setting related to interrogated host
            service.setRequestProperty("Host", "le-systeme-solaire.net");

            //require header setting related to response format (JSON)
            service.setRequestProperty("Accept", "application/json");

            //required header setting related to response encoding (UTF-8)
            service.setRequestProperty("Accept-Charset", "UTF-8");

            //set request method to 'GET'
            service.setRequestMethod("GET");

            //receive activation
            service.setDoInput(true);

            //establish the connection
            service.connect();

            status = service.getResponseCode();

            //check the status code
            if (status != 200) {
                //error
                return null;
            }

            String line;
            BufferedReader input = new BufferedReader(new InputStreamReader(service.getInputStream(), "UTF-8"));
            if ((line = input.readLine()) != null) {
                JSONObject json = new JSONObject(line);
                xmlResponse = XML.toString(json,"bodyType"); //establish the root
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return xmlResponse;
    }
}