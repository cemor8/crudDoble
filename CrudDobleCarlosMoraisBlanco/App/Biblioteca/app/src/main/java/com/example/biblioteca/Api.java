package com.example.biblioteca;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface Api {
    @GET("/api/libros")
    Call<ArrayList<Libro>> getLibros();
    @POST("/api/libros")
    Call<ResponseBody> meterLibro(@Body Libro nuevoLibro);
    @PUT("/api/libros/{titulo}")
    Call<Void> actualizarLibro(@Path("titulo") String titulo, @Body Libro libro);
    @DELETE("/api/libros/{titulo}")
    Call<Void> eliminarLibro(@Path("titulo") String titulo);
}
