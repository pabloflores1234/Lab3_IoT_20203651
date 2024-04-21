package com.example.lab3_20203651.interfaces_contador;

import com.example.lab3_20203651.objetos.primo;

import java.util.List;

import retrofit2.*;
import retrofit2.http.GET;

public interface numero_primo {

    @GET("primeNumbers?len=999&order=1")
    Call<List<primo>> getPrimeNumbers();

}
