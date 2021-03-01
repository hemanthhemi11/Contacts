package com.example.contacts.Utils;


import com.example.contacts.constants.ProjectNameConstants;
import com.example.contacts.services.ApiService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProjectNameUtils {

    public static ApiService initializeRetro() {
        return new Retrofit.Builder()
                .baseUrl(ProjectNameConstants.Url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService.class);
    }
}
