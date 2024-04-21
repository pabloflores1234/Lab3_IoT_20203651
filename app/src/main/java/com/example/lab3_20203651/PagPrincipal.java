package com.example.lab3_20203651;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PagPrincipal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.pag_principal);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        Button buttonVisualizar = findViewById(R.id.buttonVisualizar);
        Button buttonBuscar = findViewById(R.id.buttonBuscar);
        EditText editTextIdPelicula = findViewById(R.id.editTextIdPelicula);

        buttonVisualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PagPrincipal.this, Contador.class);

                startActivity(intent);
            }
        });

        buttonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idPelicula = editTextIdPelicula.getText().toString().trim();


                if (!idPelicula.isEmpty()) {

                    Intent intent = new Intent(PagPrincipal.this, FormularioPeliculas.class);


                    intent.putExtra("ID_PELICULA", idPelicula);


                    startActivity(intent);
                } else {

                    Toast.makeText(PagPrincipal.this, "Ingrese el ID de la película", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}