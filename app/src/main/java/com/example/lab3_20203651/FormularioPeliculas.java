package com.example.lab3_20203651;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lab3_20203651.interfaces_contador.OmdbApiService;
import com.example.lab3_20203651.objetos.Peliculas;
import com.example.lab3_20203651.objetos.Rating;

import org.w3c.dom.Text;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FormularioPeliculas extends AppCompatActivity {


    private static final String API_KEY = "bf81d461";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_formulario_peliculas);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
        boolean tieneInternet = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        Log.d("msg-test","Internet: " + tieneInternet);



        String idPelicula = getIntent().getStringExtra("ID_PELICULA");
        CheckBox checkbox = findViewById(R.id.Checkbox1);
        Button buttonRegresar = findViewById(R.id.buttonRegresar);


        OmdbApiService omdbApiService = new Retrofit.Builder()
                .baseUrl("https://www.omdbapi.com/")
                        .addConverterFactory(GsonConverterFactory.create())
                                .build().create(OmdbApiService.class);


        omdbApiService.getPeliculaDetails(API_KEY,idPelicula).enqueue(new Callback<Peliculas>() {
            @Override
            public void onResponse(@NonNull Call<Peliculas> call, @NonNull Response<Peliculas> response) {
                if(response.isSuccessful()){
                    Log.d("msg_yeih","Recibimos bien");
                    Peliculas peliculas = response.body();
                    TextView titulo_pelicula = findViewById(R.id.TituloPelicula);
                    TextView director = findViewById(R.id.directorobtenido);
                    TextView escritores = findViewById(R.id.escritoresobtenido);
                    TextView actor = findViewById(R.id.actorobtenido);
                    TextView lenguaje = findViewById(R.id.lenguajebtenido);
                    TextView pais = findViewById(R.id.paisobtenido);
                    TextView trama = findViewById(R.id.plot);

                    TextView rating1 = findViewById(R.id.rating1);
                    TextView rating2 = findViewById(R.id.rating2);
                    TextView puntuacion1 = findViewById(R.id.punt1);
                    TextView puntuacion2 = findViewById(R.id.punt2);

                    List<Rating> ratings =  peliculas.getRatings();
                    if (ratings != null && !ratings.isEmpty()) {
                        for (int i = 0; i < ratings.size(); i++) {
                            Rating rating = ratings.get(i);
                            switch (i) {
                                case 0:
                                    rating1.setText(rating.getSource());
                                    puntuacion1.setText(rating.getValue());
                                    break;
                                case 1:
                                    rating2.setText(rating.getSource());
                                    puntuacion2.setText(rating.getValue());
                                    break;
                                default:
                                    break;
                            }
                        }
                    }



                    titulo_pelicula.setText(peliculas.getTitle());
                    director.setText(peliculas.getDirector());
                    escritores.setText(peliculas.getWriter());
                    actor.setText(peliculas.getActors());
                    lenguaje.setText(peliculas.getLanguage());
                    pais.setText(peliculas.getCountry());
                    trama.setText(peliculas.getPlot());


                }else{
                    Log.d("msg-test","Error al recibir");
                }

            }

            @Override
            public void onFailure(Call<Peliculas> call, Throwable t) {
                t.printStackTrace();
            }
        });

        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                buttonRegresar.setEnabled(isChecked);
                if (!isChecked) {
                    buttonRegresar.setClickable(false);
                    buttonRegresar.setFocusable(false);
                } else {
                    buttonRegresar.setClickable(true);
                    buttonRegresar.setFocusable(true);
                }
            }
        });

        buttonRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkbox.isChecked()) {
                    Intent intent = new Intent(FormularioPeliculas.this, PagPrincipal.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(FormularioPeliculas.this, "Confirma la información antes de regresar.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}