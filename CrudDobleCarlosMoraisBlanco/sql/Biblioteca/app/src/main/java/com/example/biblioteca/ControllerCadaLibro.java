package com.example.biblioteca;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ControllerCadaLibro extends AppCompatActivity {
    private ArrayList<Libro> libros;
    private TextView mostrarTitulo;
    private TextView mostrarAutor;
    private TextView mostrarPag;
    private TextView mostrarFecha;
    private Libro libroSeleccionado;
    private Context referencia;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cada_libro);
        Intent intent = getIntent();
        referencia = getApplicationContext();
        if (intent.hasExtra("libros")) {
            this.libros = (ArrayList<Libro>) intent.getSerializableExtra("libros");
        }

        if(intent.hasExtra("posicionLibro")){
            this.libroSeleccionado = this.libros.get(intent.getIntExtra("posicionLibro",0));
        }
        this.mostrarTitulo = findViewById(R.id.mostrarTitulo);
        this.mostrarAutor = findViewById(R.id.mostrarAutor);
        this.mostrarPag = findViewById(R.id.mostrarPag);

        this.mostrarTitulo.setText(this.libroSeleccionado.getTitulo());
        this.mostrarAutor.setText(this.libroSeleccionado.getAutor());
        String pagina = getString(R.string.paginas,this.libroSeleccionado.getPaginas().toString());
        this.mostrarPag.setText(pagina);

    }
    /**
     * Método que permite volver a la anterior actividad
     * */
    public void volver(View view){
        Intent intent = new Intent(this, ControllerListaLibros.class);
        intent.putExtra("libros",this.libros);
        startActivity(intent);
    }
    /**
     * Método que se encarga de cargar la actividad de modificacion de libros
     * */
    public void modificar(View view){
        Intent intent = new Intent(this, ControllerModificarLibro.class);
        intent.putExtra("posicionLibro", this.libros.indexOf(this.libroSeleccionado));
        intent.putExtra("libros",this.libros);
        startActivity(intent);
    }
    /**
     * Método que se encarga de eliminar un libro haciendo una peticion al servidor express, si la peticion
     * es satisfactoria y es eliminado el contenido de la base de datos, luego se elimina de la lista de libros
     * */
    public void eliminar(View view){
        Api api = ConexionRetrofit.getConexion().create(Api.class);
        Call<Void> call = api.eliminarLibro(this.libroSeleccionado.getTitulo());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                System.out.println("respuesta");
                if (response.isSuccessful()) {
                    libros.remove(libroSeleccionado);
                    Intent intent = new Intent(referencia, ControllerListaLibros.class);
                    intent.putExtra("libros",libros);
                    startActivity(intent);

                }else {
                    System.out.println("Error en server");
                    int statusCode = response.code();
                    System.out.println(statusCode);

                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable err) {
                System.out.println("Error al intentar enviar datos");
                System.out.println(err.getMessage());
            }
        });
    }
}
