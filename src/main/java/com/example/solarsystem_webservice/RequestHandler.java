package com.example.solarsystem_webservice;

import org.json.JSONObject;
import org.json.XML;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;

public class RequestHandler {

    // Open data powered by 'Le Système solaire à portée de votre souris' --> https://api.le-systeme-solaire.net/en/
    private static final String webService_URL = "https://api.le-systeme-solaire.net/rest/bodies?";

    public static String getInfo(String parameters) {
        URL server;
        HttpsURLConnection service;
        String xmlResponse = null;

        //stato della richiesta ( es 200, 404 ecc.)
        int status;

        try {

            // create the request URL towards open data web service encoding "idBody" value according UTF-8 standard
            server = new URL(webService_URL + URLEncoder.encode(parameters, "UTF-8"));

            //set up connection to web service
            service = (HttpsURLConnection) server.openConnection();

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
            StringBuilder jsonString = new StringBuilder();
            String line;
            while ((line = input.readLine()) != null) {
                jsonString.append(line);
            }

            JSONObject json = new JSONObject(jsonString);

            xmlResponse = XML.toString(json);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return xmlResponse;
    }
}