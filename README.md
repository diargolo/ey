# Intrucciones de Ejecucion de API:

* Para iniciar la DB (ejecutar en consola, en el directorio /hsqldb-2.6.1/hsqldb/data):
  java -cp ../lib/hsqldb.jar org.hsqldb.server.Server --database.0 file:userdb --dbname.0 userdb

* Para conectarse la DB (ejecutar en consola, en el directorio /hsqldb-2.6.1/hsqldb/data):
  java -cp ../lib/hsqldb.jar org.hsqldb.util.DatabaseManagerSwing

* Collecion de todas las operaciones para POSTMAN ( * Recomendado Importarla )
  * /docs/EY.postman_collection.json
  * (createTables, listUsers, createUser, createUser - Error Email, createUser - Error PW, decodeJWT, getUserById, deleteUserById, updateUser)

* Para inicar desde el API la base de datos, no es necesario ejecutar scrips de SQL, en su lugar, usar de la collecion de postman:
  POST http://localhost:8080/v1/createTables

* Script creacion Tabla User
  CREATE TABLE user (
  id VARCHAR (45) NOT NULL,
  name VARCHAR (45),
  email VARCHAR (45),
  password VARCHAR (45),
  created  VARCHAR (45),
  modified VARCHAR (45),
  lastLogin VARCHAR (45),
  token VARCHAR (200),
  isActive VARCHAR (5),
  PRIMARY KEY (ID)
  );

* Script creacion Tabla phone
  CREATE TABLE phone (
  id VARCHAR (45) NOT NULL,
  idUser VARCHAR (45) NOT NULL,
  number VARCHAR (10),
  citycode VARCHAR (2),
  contrycode VARCHAR (2),
  PRIMARY KEY (ID)
  );