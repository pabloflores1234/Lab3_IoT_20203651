package com.example.lab3_20203651;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.example.lab3_20203651.interfaces_contador.NumeroPrimoApiService;
import com.example.lab3_20203651.interfaces_contador.OmdbApiService;
import com.example.lab3_20203651.objetos.Primo;
import com.example.lab3_20203651.workers.WorkerPrimo;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Contador extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contador);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;



        });

        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
        boolean tieneInternet = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        Log.d("msg-test","Internet: " + tieneInternet);

        metodocontador();


    }

    public void metodocontador(){

        TextView primotexto = findViewById(R.id.contadorPrimo);
        NumeroPrimoApiService numeroPrimoApiService = new Retrofit.Builder()
                .baseUrl("https://prime-number-api.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(NumeroPrimoApiService.class);

        int len = 999;
        int order = 1;
        numeroPrimoApiService.getPrimos(len,order).enqueue(new Callback<List<Primo>>() {
            @Override
            public void onResponse(@NonNull Call<List<Primo>> call,@NonNull Response<List<Primo>> response) {
                if(response.isSuccessful()){
                    Log.d("msg_yeih","Recibimos bien");
                    List<Primo> listaprimo = response.body();

                    int[] arrayPrimos = new int[listaprimo.size()];

                    // Llenar el arreglo con los números primos
                    for (int i = 0; i < listaprimo.size(); i++) {
                        arrayPrimos[i] = listaprimo.get(i).getNumber();
                    }

                    UUID uuid = UUID.randomUUID();
                    Data dataBuilder = new Data.Builder()
                            .putIntArray("listaenterosprimos", arrayPrimos)
                            .build();

                    WorkRequest workRequest = new OneTimeWorkRequest.Builder(WorkerPrimo.class)
                            .setId(uuid)
                            .setInputData(dataBuilder)
                            .build();

                    WorkManager.getInstance().enqueue(workRequest);


                    WorkManager.getInstance()
                            .getWorkInfoByIdLiveData(uuid)
                            .observe(Contador.this, workInfo -> {
                                if (workInfo != null) {
                                    Data progress = workInfo.getProgress();
                                    int primo = progress.getInt("primo", 0);
                                    Log.d("primo_esperado",String.valueOf(primo));
                                    primotexto.setText(String.valueOf(primo));
                                } else {
                                    Log.d("msgestado", "work info == null ");
                                }
                            });


                }else{
                    Log.d("msg-test","Todo bien");
                }
            }

            @Override
            public void onFailure(Call<List<Primo>> call, Throwable t) {
                t.printStackTrace();
            }
        });


    }

}


