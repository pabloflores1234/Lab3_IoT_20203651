package com.example.lab3_20203651.workers;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.lab3_20203651.objetos.primo;
import com.example.lab3_20203651.retro;
import com.example.lab3_20203651.interfaces_contador.numero_primo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class primos_worker extends Worker {

    public primos_worker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        // Lógica para obtener los números primos de la API de forma asíncrona
        obtenerNumerosPrimosAsync();
        return Result.success(); // Devolver Result.success() para indicar que el trabajo se ha completado correctamente
    }

    // Método para obtener los números primos de la API de forma asíncrona
    private void obtenerNumerosPrimosAsync() {
        numero_primo service = retro.getRetrofitInstance().create(numero_primo.class);
        Call<List<primo>> call = service.getPrimeNumbers();
        call.enqueue(new Callback<List<primo>>() {
            @Override
            public void onResponse(Call<List<primo>> call, Response<List<primo>> response) {
                if (response.isSuccessful()) {
                    List<primo> primos = response.body();
                    if (primos != null) {
                        List<Integer> numerosPrimos = new ArrayList<>();
                        for (primo p : primos) {
                            numerosPrimos.add(p.getNumero());
                        }
                        // Construir la lista de números primos como resultado
                        Data outputData = new Data.Builder()
                                .putIntArray("numeros_primos", convertirListaEnteros(numerosPrimos))
                                .build();
                        // Entregar los datos de salida al resultado del trabajo
                        Result.success(outputData);
                    } else {
                        Log.e("primos_worker", "Respuesta nula al obtener los números primos");
                    }
                } else {
                    Log.e("primos_worker", "Error al obtener los números primos: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<primo>> call, Throwable t) {
                Log.e("primos_worker", "Error de red al obtener los números primos", t);
            }
        });
    }

    // Método para convertir una lista de enteros a un array de enteros
    private int[] convertirListaEnteros(List<Integer> lista) {
        int[] array = new int[lista.size()];
        for (int i = 0; i < lista.size(); i++) {
            array[i] = lista.get(i);
        }
        return array;
    }
}
