const express = require('express');
const mysql = require('mysql');
const app = express();
const port = 3000;
app.use(express.json());
app.use(express.urlencoded({
    extended: true
}));
//conexion a la base de datos con el usuario root
const base = mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: '',
    database: 'biblioteca'
});

base.connect((err) => {
    if (err) {
        throw err;
    }
    console.log('Conectado a la base de datos MySQL');
});
/**
 * Obtiene todos los libros y los devuelve
 * @returns
 */
app.get('/api/libros', (req, res) => {
    const sql = 'select * from libro';
    base.query(sql, (err, result) => {
        if (err) {
            throw err;
        }
        res.send(result);
    });
});
/**
 * Recibe un nuevo libro en el body y crea un nuevo registro
 * en la tabla libro con estos datos
 * @returns
 */
app.post('/api/libros', (req, res) => {
    let libro = req.body;
    let sql = 'INSERT INTO libro (titulo, autor, paginas) VALUES (?, ?, ?)';
    let valores = [libro.titulo, libro.autor, libro.paginas];

    base.query(sql, valores, (err, result) => {
        if (err) {
            throw err;
        }
        res.sendStatus(200);
    });
});
/**
 * Recibe un titulo de un libro en los parametros de la url
 * junto a un nuevo objeto libro y modifica el registro que contenga
 * el titulo establecido en los parametros con los nuevos datos
 * @returns
 */
app.put('/api/libros/:titulo', (req, res) => {
    let titulo = req.params.titulo;
    let libroModificado = {
        titulo: req.body.titulo,
        autor: req.body.autor,
        paginas: req.body.paginas
    };
    let sql = `update libro set ? where titulo = "${titulo}"`;
    base.query(sql, libroModificado, (err, result) => {
        if (err) {
            throw err;
        }
        //todo ok
        res.sendStatus(200);
    });
});
/**
 * Elimina el registro cuyo titulo sea igual al establecido en 
 * los parametros de la url
 * 
 */
app.delete('/api/libros/:titulo', (req, res) => {
    let titulo = req.params.titulo;

    let sql = `delete from libro where titulo = "${titulo}"`;
    base.query(sql, (err, result) => {
        if (err) {
            throw err;
        }
        res.sendStatus(200);
    });
});


app.listen(port, () => {
    console.log("Servidor levantado correctamente en el puerto", port);
});