package com.example.biblioteca;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ControllerListaLibros extends AppCompatActivity {
    private ArrayList<Libro> libros = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_libros);
        Intent intent = getIntent();
        if (intent.hasExtra("libros")) {
            this.libros =(ArrayList<Libro>) intent.getSerializableExtra("libros");
        }

        RecyclerView recyclerView = findViewById(R.id.contenedor);

        int columnas = 2;
        recyclerView.setLayoutManager(new GridLayoutManager(this, columnas));
        LibroAdapter adapter = new LibroAdapter(this.libros);
        adapter.setOnItemClickListener(new LibroAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                abrirDetalleLibro(position);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setAdapter(adapter);

    }
    /**
     * Método que se encarga de volver a la anterior actividad
     * */
    public void volver(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    /**
     * Método que abre la actividad donde se muestra cada libro, con los datos
     * del libro seleccionado
     * */
    private void abrirDetalleLibro(int posicionLibro) {
        Intent intent = new Intent(this, ControllerCadaLibro.class);
        intent.putExtra("posicionLibro", posicionLibro);
        intent.putExtra("libros",this.libros);
        startActivity(intent);
    }
}
