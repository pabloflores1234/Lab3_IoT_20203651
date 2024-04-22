package com.example.lab3_20203651.workers;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.lab3_20203651.interfaces_contador.NumeroPrimoApiService;
import com.example.lab3_20203651.objetos.Primo;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WorkerPrimo extends Worker {

    public WorkerPrimo(Context context, WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        int[] listaprimo = getInputData().getIntArray("listaenterosprimos");

        if (listaprimo != null) {

            setProgressAsync(new Data.Builder().putString("state", "RUNNING").build());


            for (int i = 0; i < listaprimo.length; i++) {
                int primo = listaprimo[i];
                enviarPrimoConRetraso(primo, i * 1000);
            }


            try {
                Thread.sleep(listaprimo.length * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            setProgressAsync(new Data.Builder().putString("state", "SUCCESS").build());
        } else {
            Log.d("msgerror", "La lista de primos es nula");
        }


        Data data = new Data.Builder()
                .putString("info","Worker finalizado")
                .build();

        return Result.success(data);
    }

    private void enviarPrimoConRetraso(int primo, long retraso) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                setProgressAsync(new Data.Builder().putInt("primo", primo).build());
            }
        }, retraso);
    }

}

