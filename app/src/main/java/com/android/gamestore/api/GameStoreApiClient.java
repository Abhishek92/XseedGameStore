package com.android.gamestore.api;

import retrofit.RestAdapter;

/**
 * Created by hp pc on 11-07-2015.
 */
public class GameStoreApiClient {

    private final static String API_URL = "http://xseed.0x10.info/api/";
    private static GameStoreApiInterface storeApiInterface;

    private static RestAdapter getRestAdapter()
    {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(API_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        return restAdapter;
    }

    public static GameStoreApiInterface getGameStoreApi() {
        if (storeApiInterface == null)
            storeApiInterface = getRestAdapter().create(GameStoreApiInterface.class);

        return storeApiInterface;
    }
}
