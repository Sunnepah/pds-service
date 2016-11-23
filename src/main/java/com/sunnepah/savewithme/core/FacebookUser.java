package com.sunnepah.savewithme.core;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by sunnepah on 22/11/2016.
 * Sunday Ayandokun @sundayayandokun
 */
public class FacebookUser extends BaseUser {

    @JsonProperty("id")
    public String facebookId;

    @JsonProperty("first_name")
    public String first_name;

    @JsonProperty("last_name")
    public String last_name;

    @JsonProperty("link")
    public String link;

    @JsonProperty("locale")
    public String locale;

    @JsonProperty("work")
    public Work[] work;

    @JsonProperty("picture")
    public Picture picture;

    public static class Work {
        @JsonProperty("id")
        public String id;
        @JsonProperty("start_date")
        public String start_date;
        @JsonProperty("end_date")
        public String end_date;

        @JsonProperty("employer")
        public Employer employer;
    }

    public static class Employer {
        @JsonProperty("id")
        public String id;
        @JsonProperty("name")
        public String name;
    }

    public static class Picture {
        @JsonProperty("data")
        private PictureData data;

        public PictureData getData() {
            return data;
        }

        public void setData(PictureData data) {
            this.data = data;
        }
    }

    public static class PictureData {
        @JsonProperty("url")
        public String url;
        @JsonProperty("is_silhouette")
        public boolean is_silhouette;
    }
}

