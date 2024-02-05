package com.example.biblioteca;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ControllerCrearLibro extends AppCompatActivity {
    EditText introducirTitulo;
    EditText introducirAutor;
    EditText introducirPaginas;
    private ArrayList<Libro> libros = new ArrayList<>();
    Map<String, String> columnasExpresiones = new HashMap<String, String>() {
        {
            put("Paginas", "^\\d{1,5}$");
            put("Titulo", "^[\\w\\s.,!?-]{1,100}$");
            put("Autor", "^(?=.*[a-z])(?=.*[A-Z]).{4,30}$");
            put("Fecha", "^\\d{4}-\\d{2}-\\d{2}$");
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear);
        Intent intent = getIntent();
        if (intent.hasExtra("libros")) {
            this.libros =(ArrayList<Libro>) intent.getSerializableExtra("libros");
        }
        this.introducirTitulo = findViewById(R.id.introducirTitulo);
        this.introducirAutor = findViewById(R.id.introducirAutor);
        this.introducirPaginas = findViewById(R.id.introducirPaginas);
    }
    /**
     * Método que se encarga de crear un libro, comprueba que los datos sean correctos antes de
     * enviarlos al servidor, tambien comprueba que no exista un libro con el mismo titulo, si esto
     * es satisfactorio, hace una peticion al servidor express para crear el libro, una vez creado el libro en la
     * base de datos, lo añade a la lista de libros
     * */
    public void crearLibro(View view){
        System.out.println("creando el libro");
        boolean error = false;
        if(!validarDatos(this.columnasExpresiones.get("Titulo"),this.introducirTitulo.getText().toString())){
            error = true;
            System.out.println("tiutulo mal");
        }
        if(!validarDatos(this.columnasExpresiones.get("Autor"),this.introducirAutor.getText().toString())){
            error = true;
            System.out.println("autor mal");
        }
        if(!validarDatos(this.columnasExpresiones.get("Paginas"),this.introducirPaginas.getText().toString())){
            error = true;
            System.out.println("paginas mal");
        }

        if (error){
            System.out.println("error");
            Toast.makeText(this,"Error al crear los libros",Toast.LENGTH_LONG).show();
            return;
        }
        System.out.println(this.libros);
        Optional<Libro> libroRepetidoOptional = this.libros.stream().filter(libro -> libro.getTitulo().equalsIgnoreCase(this.introducirTitulo.getText().toString())).findAny();
        if(libroRepetidoOptional.isPresent()){
            Toast.makeText(this,"Libro con Id Existente",Toast.LENGTH_LONG).show();
            return;
        }
        Libro libroCreado = new Libro(this.introducirTitulo.getText().toString(),this.introducirAutor.getText().toString(),
                Integer.parseInt(this.introducirPaginas.getText().toString()));
        this.introducirAutor.setText("");
        this.introducirPaginas.setText("");
        this.introducirTitulo.setText("");
        Toast.makeText(this,"Libro Creado Con Éxito",Toast.LENGTH_LONG).show();
        Api api = ConexionRetrofit.getConexion().create(Api.class);

        Call<ResponseBody> call = api.meterLibro(libroCreado);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                System.out.println("respuesta");
                if (response.isSuccessful()) {
                    libros.add(libroCreado);

                }else {
                    System.out.println("Error en server");
                    int statusCode = response.code();
                    System.out.println(statusCode);

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable err) {
                System.out.println("Error al intentar enviar datos");
                System.out.println(err.getMessage());
            }
        });

    }
    /**
     * Método que permite volver a la anterior actividad
     * */
    public void volver(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Método que se encarga de validar los datos para que se cumpla la
     * expresion regular.
     *
     * @param patronCumplir patron a cumplir
     * @param textoBuscar   string donde buscar el patron
     */
    public boolean validarDatos(String patronCumplir, String textoBuscar) {
        Pattern patron = Pattern.compile(patronCumplir);
        Matcher matcher = patron.matcher(textoBuscar);
        return matcher.matches();
    }
}
