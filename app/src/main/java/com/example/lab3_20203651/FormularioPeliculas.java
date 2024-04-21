package com.example.lab3_20203651;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lab3_20203651.interfaces_contador.OmdbApiService;
import com.example.lab3_20203651.objetos.peliculas;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FormularioPeliculas extends AppCompatActivity {

    private static final String BASE_URL = "https://www.omdbapi.com/";
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

        String idPelicula = getIntent().getStringExtra("ID_PELICULA");
        CheckBox checkbox = findViewById(R.id.Checkbox1);
        Button buttonRegresar = findViewById(R.id.buttonRegresar);

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


        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        OmdbApiService service = retrofit.create(OmdbApiService.class);


        Call<peliculas> call = service.getPeliculaDetails(API_KEY, idPelicula);
        call.enqueue(new Callback<peliculas>() {
            @Override
            public void onResponse(Call<peliculas> call, Response<peliculas> response) {
                if (response.isSuccessful()) {
                    peliculas pelicula = response.body();
                    TextView tituloPelicula = findViewById(R.id.TituloPelicula);
                    TextView directorEditText = findViewById(R.id.directorobtenido);
                    TextView escritoresEditText = findViewById(R.id.escritoresobtenido);
                    TextView actoresEditText = findViewById(R.id.actorobtenido);
                    TextView lenguajeEditText = findViewById(R.id.lenguajeobtenido);
                    TextView paisEditText = findViewById(R.id.paisobtenido);

                    tituloPelicula.setText(pelicula.getTitle());
                    directorEditText.setText(pelicula.getDirector());
                    escritoresEditText.setText(pelicula.getWriter());
                    actoresEditText.setText(pelicula.getActors());
                    lenguajeEditText.setText(pelicula.getLanguage());
                    paisEditText.setText(pelicula.getCountry());


                } else {
                    Toast.makeText(FormularioPeliculas.this, "Error al obtener la película", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<peliculas> call, Throwable t) {
                Toast.makeText(FormularioPeliculas.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}