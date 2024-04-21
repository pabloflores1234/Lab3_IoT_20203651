package com.example.lab3_20203651.workers;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.lab3_20203651.interfaces_contador.NumeroPrimoApiService;
import com.example.lab3_20203651.objetos.Primo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WorkerPrimo extends Worker {

    public WorkerPrimo(Context context, WorkerParameters params){
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork(){
        int len = 999;
        int order = 1;

        NumeroPrimoApiService numeroPrimoApiService = new Retrofit.Builder()
                .baseUrl("https://prime-number-api.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(NumeroPrimoApiService.class);
        numeroPrimoApiService.getPrimos(len,order).enqueue(new Callback<List<Primo>>() {
            @Override
            public void onResponse(Call<List<Primo>> call, Response<List<Primo>> response) {
                if(response.isSuccessful()){
                    List<Primo> primoLista = response.body();





                }else{
                    Log.d("error","no se recibió bien la lista");
                }
            }

            @Override
            public void onFailure(Call<List<Primo>> call, Throwable t) {
                t.printStackTrace();
            }
        });
        return Result.success();
    }
}
