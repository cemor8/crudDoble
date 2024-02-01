package org.example;

import com.google.gson.*;

import java.lang.reflect.Type;

public class SerializarLibro implements JsonSerializer<Libro>, JsonDeserializer<Libro> {
    @Override
    public JsonElement serialize(Libro libro, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("titulo", libro.getTitulo());
        jsonObject.addProperty("autor", libro.getAutor());
        jsonObject.addProperty("paginas", libro.getPaginas());
        return jsonObject;
    }

    @Override
    public Libro deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String titulo = jsonObject.get("titulo").getAsString();
        String autor = jsonObject.get("autor").getAsString();
        Integer paginas = jsonObject.get("paginas").getAsInt();
        return new Libro(titulo,autor,paginas);
    }
}
