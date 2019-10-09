package com.creative.share.apps.aamalnaa.models;

import java.io.Serializable;
import java.util.List;

public class PlaceMapDetailsData implements Serializable {
    private List<PlaceData> candidates;

    public List<PlaceData> getCandidates() {
        return candidates;
    }

    public class PlaceData implements Serializable
    {
        private String id;
        private String place_id;
        private Geometry geometry;
        private String formatted_address;

        public String getId() {
            return id;
        }

        public String getPlace_id() {
            return place_id;
        }

        public Geometry getGeometry() {
            return geometry;
        }

        public String getFormatted_address() {
            return formatted_address;
        }
    }
    public class Geometry implements Serializable
    {
       private Location location;

        public Location getLocation() {
            return location;
        }
    }

    public class Location implements Serializable {

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
