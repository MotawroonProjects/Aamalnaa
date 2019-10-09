package com.creative.share.apps.aamalnaa.models;

import java.io.Serializable;
import java.util.List;

public class PlaceGeocodeData implements Serializable {

    private List<Geocode> results;

    public List<Geocode> getResults() {
        return results;
    }
    public class Geocode implements Serializable
    {
        private String formatted_address;
        private String place_id;
        private Geometry geometry;

        public String getFormatted_address() {
            return formatted_address;
        }

        public Geometry getGeometry() {
            return geometry;
        }

        public String getPlace_id() {
            return place_id;
        }
    }

    public class Geometry implements Serializable
    {
        private Location location;

        public Location getLocation() {
            return location;
        }
    }
    public class Location implements Serializable
    {
        private double lat;
        private double lng;

        public double getLat() {
            return lat;
        }

        public double getLng() {
            return lng;
        }
    }
}
