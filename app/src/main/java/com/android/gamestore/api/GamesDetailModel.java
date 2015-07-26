package com.android.gamestore.api;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp pc on 11-07-2015.
 */
public class GamesDetailModel {

    @Expose
    private String name;
    @Expose
    private String image;
    @Expose
    private String url;
    @Expose
    private String price;
    @Expose
    private String rating;
    @Expose
    private String description;
    @Expose
    private List<Demographic> demographic = new ArrayList<Demographic>();

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The image
     */
    public String getImage() {
        return image;
    }

    /**
     *
     * @param image
     * The image
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     *
     * @return
     * The url
     */
    public String getUrl() {
        return url;
    }

    /**
     *
     * @param url
     * The url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     *
     * @return
     * The price
     */
    public String getPrice() {
        return price;
    }

    /**
     *
     * @param price
     * The price
     */
    public void setPrice(String price) {
        this.price = price;
    }

    /**
     *
     * @return
     * The rating
     */
    public String getRating() {
        return rating;
    }

    /**
     *
     * @param rating
     * The rating
     */
    public void setRating(String rating) {
        this.rating = rating;
    }

    /**
     *
     * @return
     * The description
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     * The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return
     * The demographic
     */
    public List<Demographic> getDemographic() {
        return demographic;
    }

    /**
     *
     * @param demographic
     * The demographic
     */
    public void setDemographic(List<Demographic> demographic) {
        this.demographic = demographic;
    }

    public class Demographic
    {
        @Expose
        private String country;
        @Expose
        private String percentage;

        /**
         *
         * @return
         * The country
         */
        public String getCountry() {
            return country;
        }

        /**
         *
         * @param country
         * The country
         */
        public void setCountry(String country) {
            this.country = country;
        }

        /**
         *
         * @return
         * The percentage
         */
        public String getPercentage() {
            return percentage;
        }

        /**
         *
         * @param percentage
         * The percentage
         */
        public void setPercentage(String percentage) {
            this.percentage = percentage;
        }

    }

    @Override
    public String toString() {
        return name;
    }
}
