package com.example.lab3_20203651.interfaces_contador;

import com.example.lab3_20203651.objetos.Peliculas;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OmdbApiService {

    @GET("/")
    Call<Peliculas> getPeliculaDetails(@Query("apikey") String apikey, @Query("i") String id);
}
