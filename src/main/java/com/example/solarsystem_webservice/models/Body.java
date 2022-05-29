package com.example.solarsystem_webservice.models;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Body {
    private final String name;
    private final String mass;          // mass kg * 10^n
    private final String volume;         // volume kg^3 * 10^n
    private final float density;        // density in g/cm^3.
    private final float gravity;        // surface gravity in m/s^2.
    private final String bodyType;      // body type
    private final float orbitPeriod;
    private HashMap<String, String> moons;     // moons -> List of all moons : moonUrl -> url used to request data about the moon


    public Body(String name, String mass, String volume, float density, float gravity, String bodyType, float orbitPeriod, HashMap<String, String> moons) {
        this.name = name;
        this.mass = mass;
        this.volume = volume;
        this.density = density;
        this.gravity = gravity;
        this.bodyType = bodyType;
        this.orbitPeriod = orbitPeriod;
        this.moons = moons;
    }

    public Body(String name, String mass, String volume, float density, float gravity, String bodyType, float orbitPeriod) {
        this.name = name;
        this.mass = mass;
        this.volume = volume;
        this.density = density;
        this.gravity = gravity;
        this.bodyType = bodyType;
        this.orbitPeriod = orbitPeriod;
        this.moons = null;
    }

    public String getName() {
        return name;
    }

    public String getMass() {
        return mass;
    }

    public String getVolume() {
        return volume;
    }

    public float getDensity() {
        return density;
    }

    public float getGravity() {
        return gravity;
    }

    public String getBodyType() {
        return bodyType;
    }

    public float getOrbitPeriod() {
        return orbitPeriod;
    }

    public HashMap<String, String> getMoons() {
        return moons;
    }

    @Override
    public String toString() {
        return "Body{" +
                "name='" + name + '\'' +
                ", mass='" + mass + '\'' +
                ", volume='" + volume + '\'' +
                ", density=" + density +
                ", gravity=" + gravity +
                ", bodyType='" + bodyType + '\'' +
                ", orbitPeriod=" + orbitPeriod +
                ", moons=" + moons +
                '}';
    }

    public String toXml() {
        String result = "<body>" +
                "<name>" + name + "</name>\n" +
                "<mass>" + mass + "</mass>\n" +
                "<vol>" + volume + "</vol>\n" +
                "<density>" + density + "</density>\n" +
                "<gravity>" + gravity + "</gravity>\n" +
                "<bodyType>" + bodyType + "</bodyType>\n" +
                "<orbitPeriod>" + orbitPeriod + "</orbitPeriod>\n";

        if (moons == null) {

            return result + "<moons>null</moons>\n</body>";

        } else {

            StringBuilder moonsUrlList = new StringBuilder("<moons>\n");
            for (Map.Entry<String, String> entry : moons.entrySet()) {
                moonsUrlList.append("<moonUrl name=\"").append(entry.getKey()).append("\">").append(entry.getValue()).append("</moonUrl>\n");
            }

            moonsUrlList.append("</moons>\n</body>");

            return result + moonsUrlList.toString();

        }
    }
}
/*

/
 */