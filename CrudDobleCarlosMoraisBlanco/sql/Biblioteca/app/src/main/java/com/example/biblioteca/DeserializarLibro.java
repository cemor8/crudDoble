package com.example.biblioteca;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class DeserializarLibro implements JsonDeserializer<Libro> {
    /**
     * Método para deserializar los objetos json en objetos Libro,
     * modifico la fecha para obeter solo el dia - mes - año.
     * */
    @Override
    public Libro deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject contenidoJson = json.getAsJsonObject();
        System.out.println(contenidoJson);
        String titulo = contenidoJson.get("titulo").getAsString();
        String autor = contenidoJson.get("autor").getAsString();
        Integer paginas = contenidoJson.get("paginas").getAsInt();
        return new Libro(titulo,autor,paginas);
    }
}
