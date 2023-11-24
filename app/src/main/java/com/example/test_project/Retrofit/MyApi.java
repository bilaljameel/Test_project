package com.example.test_project.Retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MyApi {
    @GET("reminders")
    Call<List<Modal>> getmodal();

    @POST("reminders")
    Call<Modal> createEvent(@Body Modal modal);

    @DELETE("reminders")
    Call<Void> deletReminder(@Path("id") int reminderId);

}
