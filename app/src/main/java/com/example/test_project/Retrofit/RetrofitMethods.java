package com.example.test_project.Retrofit;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.example.test_project.MainActivity;
import com.example.test_project.Reminder.ReminderMethods;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitMethods {

    Context context;
    ReminderMethods reminderMethods = new ReminderMethods();


    String url = "https://my-json-server.typicode.com/Nomemmurrakh/reminderApp/";

    public MyApi build(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MyApi myApi =retrofit.create(MyApi.class);
        return myApi;
    }


    public void add(final Context con, Modal modal, final String title, final long timestamp, final int id, final ProgressDialog progressDialog){

        context = con;

        MyApi myApi = build();


        Call<Modal> createCall = myApi.createEvent(modal);


        createCall.enqueue(new Callback<Modal>() {
            @Override
            public void onResponse(Call<Modal> call, Response<Modal> response) {
                if (response.isSuccessful()){

                    if (progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    Toast.makeText(context, "Succes", Toast.LENGTH_SHORT).show();
                    reminderMethods.scheduleReminder(title,timestamp*1000,id+1,con);

                }else {

                    if (progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    Toast.makeText(context, "Response failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Modal> call, Throwable t) {

                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }


    List<Modal> data;
    Location result;
    public void getAllCourses(List<Modal> dataa, Location resultt, final GoogleMap mMap,Context con) {

        context = con;
        data = dataa;
        result = resultt;


        MyApi myApi = build();

        Call<List<Modal>> call = myApi.getmodal();

        call.enqueue(new Callback<List<Modal>>() {
            @Override
            public void onResponse(Call<List<Modal>> call, Response<List<Modal>> response) {

                data = response.body();
                for (int i =0;i<data.size();i++){

                    result = data.get(i).getLocation();

                    MainActivity.data = data;
                    MainActivity.result = result;

                    mMap.addMarker(new MarkerOptions().position(
                            new LatLng(result.getLat(),result.getLng())).title(String.valueOf(data.get(i).getTitle())).snippet(
                            String.valueOf(data.get(i).getId())
                    ));


                }

            }

            @Override
            public void onFailure(Call<List<Modal>> call, Throwable t) {

                Toast.makeText(context, "Failde", Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void delete(Context con,int id){
        context = con;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MyApi myApi = retrofit.create(MyApi.class);

        Call<Void> deleteCall = myApi.deletReminder(id);

        deleteCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()){

                    Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
