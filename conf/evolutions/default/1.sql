# --- !Ups

CREATE TABLE usuarios
(
  id SERIAL,
  nombre VARCHAR(250) NOT NULL,
  apellido VARCHAR(250) NOT NULL,
  email VARCHAR(250) NOT NULL,
  CONSTRAINT usuarios_pk PRIMARY KEY (id)
);

# --- !Downs

DROP TABLE usuarios;