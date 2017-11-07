package com.rudanic.earthquaketracker.model;

public class EarthquakeNewsFeatures {
    private String type;
    private EarthquakeNewsFeaturesProperties properties;
    private EarthquakeNewsFeaturesGeometry geometry;
    private String id;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public EarthquakeNewsFeaturesProperties getProperties() {
        return properties;
    }

    public void setProperties(EarthquakeNewsFeaturesProperties properties) {
        this.properties = properties;
    }

    public EarthquakeNewsFeaturesGeometry getGeometry() {
        return geometry;
    }

    public void setGeometry(EarthquakeNewsFeaturesGeometry geometry) {
        this.geometry = geometry;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
