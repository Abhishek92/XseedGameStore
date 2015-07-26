package com.android.gamestore.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hp pc on 11-07-2015.
 */
public class GameApiHitModel {

    @SerializedName("api_hits")
    @Expose
    private String apiHits;

    /**
     *
     * @return
     * The apiHits
     */
    public String getApiHits() {
        return apiHits;
    }

    /**
     *
     * @param apiHits
     * The api_hits
     */
    public void setApiHits(String apiHits) {
        this.apiHits = apiHits;
    }

}
