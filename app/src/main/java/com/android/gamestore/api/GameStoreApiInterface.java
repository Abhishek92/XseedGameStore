package com.android.gamestore.api;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by hp pc on 11-07-2015.
 */
public interface GameStoreApiInterface {

    @GET("/game_data?type=json")
    void getGamesDetail(Callback<List<GamesDetailModel>> gamesDetailModelCallback);
    @GET("/game_hits?type=json")
    void getApiCounter(Callback<GameApiHitModel> gameApiHitModelCallback);
}
