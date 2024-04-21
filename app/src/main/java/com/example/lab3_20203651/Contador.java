package com.example.lab3_20203651;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.example.lab3_20203651.workers.primos_worker;

public class Contador extends AppCompatActivity {

    private TextView textViewNumeroPrimo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contador);

        textViewNumeroPrimo = findViewById(R.id.textViewNumeroPrimo);


        OneTimeWorkRequest trabajo = new OneTimeWorkRequest.Builder(primos_worker.class).build();
        WorkManager.getInstance(this).enqueue(trabajo);


        WorkManager.getInstance(this).getWorkInfoByIdLiveData(trabajo.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if (workInfo != null && workInfo.getState() == WorkInfo.State.SUCCEEDED) {

                            Data outputData = workInfo.getOutputData();
                            int[] numerosPrimos = outputData.getIntArray("numeros_primos");
                            if (numerosPrimos != null && numerosPrimos.length > 0) {

                                StringBuilder builder = new StringBuilder();
                                for (int i = 0; i < numerosPrimos.length; i++) {
                                    builder.append(numerosPrimos[i]);
                                    if (i < numerosPrimos.length - 1) {
                                        builder.append(", ");
                                    }
                                }
                                //
                                textViewNumeroPrimo.setText(builder.toString());
                            } else {
                                Log.e("Contador", "Error al obtener los números primos");
                            }
                        }
                    }
                });
    }
}


