create database biblioteca;
use biblioteca;
create table libro(
	titulo varchar(30) not null,
    autor varchar(30),
    paginas int,
    fecha date,
    primary key (titulo)
);
insert into libro values("Dune","Frank Herbert",412,"1965-05-02");