const express = require('express');
const fs = require("fs");
const mongoose = require('mongoose');
const app = express();
const port = 3000;
app.use(express.json());
app.use(express.urlencoded({
    extended: true
}));
const direccion = "mongodb://localhost:27017/Biblioteca";

mongoose.connect(direccion);

mongoose.connection.on('connected', () => {
    console.log('Conectado a MongoDB');
});

mongoose.connection.on('error', (err) => {
    console.log('Error al conectar a MongoDB', err);
});

const Schema = mongoose.Schema;
const modelo = mongoose.model;

//Defino la estructura que van a tener los documentos enviados a la aplicación
const esquema = new Schema({
    titulo: String,
    autor: String,
    paginas: Number
},{
    versionKey: false
});

// modelo vincula esquema con una coleccion para buscar documentos de ese formato en 
//esa coleccion
const libro = modelo('libro', esquema);

/**
 * Obtiene la lista de libros de la coleccion libro y 
 * la devuelve como una lista de objetos json.
 * @returns
 */
app.get('/api/libros', async (req, res) => {
    try {
        let listaLibros = await libro.find();
        res.status(200).json(listaLibros);
    } catch (err) {
        res.status(500).json({ message: err.message });
    }
});

/**
 * Actualiza un libro dependiendo del titulo que le 
 * le pase con el objeto que recibe. Devuelve un estado 200
 * si todo es correcto
 * @returns
 */
app.put('/api/libros/:titulo', async (req, res) => {
    try {
        console.log(req.body);
        await libro.findOneAndUpdate({ titulo: req.params.titulo }, req.body);
        res.sendStatus(200);
    } catch (err) {
        console.log(err.message);
        res.status(500).json({ message: err.message });
    }
});
/**
 * Introduce un nuevo libro en la base de datos con los
 * datos que obtiene en el body, devuelve un estado 201
 * si todo es correcto
 * @returns
 */
app.post('/api/libros', async (req, res) => {
    try {
        await new libro(req.body).save();
        res.sendStatus(201);
    } catch (err) {
        console.log(err.message)
        res.status(500).json({ message: err.message });
    }
});

/**
 * Elimina un libro que contenga el indice "titulo" igual
 * al especificado en la peticion, devuelve un estado 200
 * si todo es correcto.
 */
app.delete('/api/libros/:titulo', async (req, res) => {
    try {
        await libro.findOneAndDelete({ titulo: req.params.titulo });
        console.log("bien");
        res.sendStatus(200);
    } catch (err) {
        console.log(err.message)
        res.status(500).json({ message: err.message });
    }
});



app.listen(port, () => {
    console.log("Servidor levantado correctamente en el puerto", port);
});