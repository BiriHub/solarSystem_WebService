
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

            //checks if body response is not null
            if ((line = input.readLine()) != null) {
                //creates a 'JSON' object from json response
                JSONObject json = new JSONObject(line);
                //creates the corrisponding xml response from the json one
                xmlResponse = XML.toString(json, "body");
            }

            // XML parsing
            //creats DOM tree from string
            Element root = getDocument(xmlResponse);
            String mass,vol;

            //check if the body owns a mass
            String checkMass= root.getElementsByTagName("mass").item(0).getFirstChild().getNodeValue();
            if (!(checkMass==null)) {
                mass ="null";
            }else{
                mass= "" + root.getElementsByTagName("massValue").item(0).getFirstChild().getNodeValue() + "10^" + root.getElementsByTagName("massExponent").item(0).getFirstChild().getNodeValue();
            }

            //check if the body owns a volume
            String checkVol= root.getElementsByTagName("vol").item(0).getFirstChild().getNodeValue();
            if (!(checkVol==null)) {
                vol ="null";
            }else{
                vol = "" + root.getElementsByTagName("volValue").item(0).getFirstChild().getNodeValue() + "10^" + root.getElementsByTagName("volExponent").item(0).getFirstChild().getNodeValue();
            }

            String name = root.getElementsByTagName("name").item(0).getFirstChild().getNodeValue();
            float density = Float.parseFloat(root.getElementsByTagName("density").item(0).getFirstChild().getNodeValue());
            float gravity = Float.parseFloat(root.getElementsByTagName("gravity").item(0).getFirstChild().getNodeValue());
            String bodyType = root.getElementsByTagName("bodyType").item(0).getFirstChild().getNodeValue();
            float orbitPeriod = Float.parseFloat(root.getElementsByTagName("sideralOrbit").item(0).getFirstChild().getNodeValue());

            HashMap<String, String> moons;
            Body newBody;

            String checkMoon = root.getElementsByTagName("moons").item(0).getFirstChild().getNodeValue();
            //checks if there are other bodies that turn around the current body
            if (checkMoon == null) {
                moons = new HashMap<>();
                NodeList moonList = root.getElementsByTagName("moon");
                NodeList urlList = root.getElementsByTagName("rel");


                //saves all bodies in the hashmap
                for (int j = 0; j < moonList.getLength(); j++) {
                    String moonName = moonList.item(j).getFirstChild().getNodeValue();
                    String rel = urlList.item(j).getFirstChild().getNodeValue();
                    moons.put(moonName, rel);
                }
                newBody = new Body(name, mass, vol, density, gravity, bodyType, orbitPeriod, moons);
            } else {
                newBody = new Body(name, mass, vol, density, gravity, bodyType, orbitPeriod);
            }

            //prints XML string of body in the response
            xmlResponse = newBody.toXml();

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
            // check if body response is not null
            if ((line = input.readLine()) != null) {

                //creates a 'JSON' object from json response
                JSONObject json = new JSONObject(line);
                // creates the corresponding xml response from the json one
                xmlResponse = new StringBuilder(XML.toString(json, "body"));
            }

            // XML parsing
            //creates DOM tree from string
            Element root = getDocument(xmlResponse.toString());

            // saves body type
            String bodyType = root.getElementsByTagName("bodyType").item(0).getFirstChild().getNodeValue();

            //formatting xml response string
            xmlResponse = new StringBuilder("<bodies>\n<bodyType>" + bodyType + "</bodyType>\n");

            //creates a node list of all bodies contained in DOM tree
            NodeList bodies = root.getElementsByTagName("bodies");
            HashMap<String, String> moons;
            Body newBody;

            for (int i = 0; i < bodies.getLength(); i++) {
                String mass,vol;
                Element body = (Element) bodies.item(i);

                //saves body name
                String name = body.getElementsByTagName("name").item(0).getFirstChild().getNodeValue();


                //check if the body owns a mass
                String checkMass= body.getElementsByTagName("mass").item(0).getFirstChild().getNodeValue();
                if (!(checkMass==null)) {
                    mass ="null";
                }else{
                     mass= "" + body.getElementsByTagName("massValue").item(0).getFirstChild().getNodeValue() + "10^" + root.getElementsByTagName("massExponent").item(0).getFirstChild().getNodeValue();
                }

                //check if the body owns a volume
                String checkVol= body.getElementsByTagName("vol").item(0).getFirstChild().getNodeValue();
                if (!(checkVol==null)) {
                    vol ="null";
                }else{
                    vol = "" + body.getElementsByTagName("volValue").item(0).getFirstChild().getNodeValue() + "10^" + root.getElementsByTagName("volExponent").item(0).getFirstChild().getNodeValue();
                }

                float density = Float.parseFloat(body.getElementsByTagName("density").item(0).getFirstChild().getNodeValue());
                float gravity = Float.parseFloat(body.getElementsByTagName("gravity").item(0).getFirstChild().getNodeValue());
                float orbitPeriod = Float.parseFloat(body.getElementsByTagName("sideralOrbit").item(0).getFirstChild().getNodeValue());

                String checkMoon = body.getElementsByTagName("moons").item(0).getFirstChild().getNodeValue();
                //checks if there are other bodies that turn around the current body
                if (checkMoon == null) {
                    moons = new HashMap<>();
                    NodeList moonList = body.getElementsByTagName("moon");
                    NodeList urlList = body.getElementsByTagName("rel");


                    //saves all bodies in the hashmap
                    for (int j = 0; j < moonList.getLength(); j++) {
                        String moonName = moonList.item(j).getFirstChild().getNodeValue();
                        String rel = urlList.item(j).getFirstChild().getNodeValue();
                        moons.put(moonName, rel);
                    }
                    newBody = new Body(name, mass, vol, density, gravity, bodyType, orbitPeriod, moons);
                } else {
                    newBody = new Body(name, mass, vol, density, gravity, bodyType, orbitPeriod);
                }

                // prints XML string of body in the response
                xmlResponse.append(newBody.toXml()).append("\n");
            }
            xmlResponse.append("</bodies>");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return xmlResponse.toString();
    }
}