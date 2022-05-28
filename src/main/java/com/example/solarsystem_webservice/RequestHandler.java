
//https://api.le-systeme-solaire.net/en/

package com.example.solarsystem_webservice;

import com.example.solarsystem_webservice.models.Body;
import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

public class RequestHandler {

    // Open data powered by 'Le Système solaire à portée de votre souris' --> https://api.le-systeme-solaire.net/en/


    //first get
    public static final String idbody_URL = "https://api.le-systeme-solaire.net/rest/bodies/"; //+ idbody

    //second get
    public static final String bodyType_URL = "https://api.le-systeme-solaire.net/rest.php/bodies?filter[]=bodyType,eq,";

    //create a document from an 'InputStream'
    private static Element getDocument(String request) {
        // parsing dell'xml ricevuto con il metodo POST
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        Document document = null;
        try {
            builder = factory.newDocumentBuilder();

            InputSource inputRequest = new InputSource(new StringReader(request));
            //Salvo l'xml inviato dal client
            document = builder.parse(inputRequest);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return document.getDocumentElement();

    }
    // TODO: 28/05/2022 it works
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
                xmlResponse = XML.toString(json, "body");
            }

            // XML parsing
            Element root = getDocument(xmlResponse);


            String name = root.getElementsByTagName("name").item(0).getFirstChild().getNodeValue();

            String mass = "" + root.getElementsByTagName("massValue").item(0).getFirstChild().getNodeValue() + "10^" + root.getElementsByTagName("massExponent").item(0).getFirstChild().getNodeValue();
            String vol = "" + root.getElementsByTagName("volValue").item(0).getFirstChild().getNodeValue() + "10^" + root.getElementsByTagName("volExponent").item(0).getFirstChild().getNodeValue();
            float density = Float.parseFloat(root.getElementsByTagName("density").item(0).getFirstChild().getNodeValue());
            float gravity = Float.parseFloat(root.getElementsByTagName("gravity").item(0).getFirstChild().getNodeValue());
            String bodyType = root.getElementsByTagName("bodyType").item(0).getFirstChild().getNodeValue();
            float orbitPeriod = Float.parseFloat(root.getElementsByTagName("sideralOrbit").item(0).getFirstChild().getNodeValue());
            String moon = root.getElementsByTagName("moon").item(0).getFirstChild().getNodeValue();

            HashMap<String, String> moons;
            Body newBody;
            if (!(moon == null)) {

                moons = new HashMap<>();
                NodeList moonList = root.getElementsByTagName("moon");
                NodeList urlList = root.getElementsByTagName("rel");

                for (int i = 0; i < moonList.getLength(); i++) {
                    String moonName = moonList.item(i).getFirstChild().getNodeValue();
                    String rel = urlList.item(i).getFirstChild().getNodeValue();
                    moons.put(moonName, rel);
                }
                newBody = new Body(name, mass, vol, density, gravity, bodyType, orbitPeriod, moons);
            } else {
                newBody = new Body(name, mass, vol, density, gravity, bodyType, orbitPeriod);
            }

            xmlResponse = newBody.toXml();
            System.out.println(newBody.toXml());

        } catch (IOException e) {
            e.printStackTrace();
        }


        return xmlResponse;
    }

    public static String getBodyType(URL urlRequest) {

        HttpsURLConnection service;
        StringBuilder xmlResponse = null;

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
                xmlResponse = new StringBuilder(XML.toString(json, "body")); //establish the root
            }

            // XML parsing
            Element root = getDocument(xmlResponse.toString());


            // TODO: 28/05/2022  Cannot invoke "org.w3c.dom.Node.getFirstChild()" because the return value of "org.w3c.dom.NodeList.item(int)" is null
            // TODO: 28/05/2022  //risolvi il problema
            // TODO: 27/05/2022 start to build up the android application


            String bodyType = root.getElementsByTagName("bodyType").item(0).getFirstChild().getNodeValue();
            xmlResponse = new StringBuilder("<bodies>\n<bodyType>" + bodyType + "</bodyType>\n");


            NodeList bodies = root.getElementsByTagName("bodies");


            HashMap<String, String> moons;
            Body newBody;

            for (int i = 0; i < bodies.getLength(); i++) {

                String name = root.getElementsByTagName("name").item(i).getFirstChild().getNodeValue();
                String mass = "" + root.getElementsByTagName("massValue").item(i).getFirstChild().getNodeValue() + "10^" + root.getElementsByTagName("massExponent").item(i).getFirstChild().getNodeValue();
                String vol = "" + root.getElementsByTagName("volValue").item(i).getFirstChild().getNodeValue() + "10^" + root.getElementsByTagName("volExponent").item(i).getFirstChild().getNodeValue();
                float density = Float.parseFloat(root.getElementsByTagName("density").item(i).getFirstChild().getNodeValue());
                float gravity = Float.parseFloat(root.getElementsByTagName("gravity").item(i).getFirstChild().getNodeValue());
                float orbitPeriod = Float.parseFloat(root.getElementsByTagName("sideralOrbit").item(i).getFirstChild().getNodeValue());
                String moon = root.getElementsByTagName("moon").item(i).getFirstChild().getNodeValue();

                if (!(moon == null)) {
                    moons = new HashMap<>();
                    NodeList moonList = root.getElementsByTagName("moon");
                    NodeList urlList = root.getElementsByTagName("rel");

                    for (int j = 0; j < moonList.getLength(); j++) {
                        String moonName = moonList.item(j).getFirstChild().getNodeValue();
                        String rel = urlList.item(j).getFirstChild().getNodeValue();
                        moons.put(moonName, rel);
                    }
                    newBody = new Body(name, mass, vol, density, gravity, bodyType, orbitPeriod, moons);
                } else {
                    newBody = new Body(name, mass, vol, density, gravity, bodyType, orbitPeriod);
                }

                xmlResponse.append(newBody.toXml()).append("\n");
            }
            //System.out.println(newBody.toXml());
            xmlResponse.append("</bodies>");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return xmlResponse.toString();
    }
}