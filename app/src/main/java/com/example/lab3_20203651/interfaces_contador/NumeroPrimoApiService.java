package com.example.lab3_20203651.interfaces_contador;

import com.example.lab3_20203651.objetos.Primo;

import java.util.List;

import retrofit2.*;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NumeroPrimoApiService {

    @GET("/primeNumbers")
    Call<List<Primo>> getPrimos(@Query("len") int len1, @Query("order") int order1);
}
