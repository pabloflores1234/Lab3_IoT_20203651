package com.example.lab3_20203651;
import com.example.lab3_20203651.interfaces_contador.numero_primo;
import com.example.lab3_20203651.objetos.primo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class primos_total{

    public void getPrimeNumbers(Callback<List<primo>> callback) {
        numero_primo service = retro.getRetrofitInstance().create(numero_primo.class);
        Call<List<primo>> call = service.getPrimeNumbers();
        call.enqueue(callback);
    }

}
