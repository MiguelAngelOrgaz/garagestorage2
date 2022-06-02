# garagestorage2

En primer lugar habría que crear la base de datos, las tablas y sus relaciones. Y después, hay un par de inserts para crear los roles y 
otro par de inserts para crear un usuario administrador.

MUY IMPORTANTE crear los inserts antes de hacer uso de la aplicación.
El administrador tiene por defecto la contraseña 12345678


CREATE DATABASE `garaje` /*!40100 DEFAULT CHARACTER SET utf8mb4 */ /*!80016 DEFAULT ENCRYPTION='N' */;

CREATE TABLE garaje.`usuario2` (
  `idUsuario` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) NOT NULL COMMENT 'Nombre con el que se loga el usuario',
  `password` varchar(150) NOT NULL,
  `email` varchar(45) NOT NULL,
  `esActivo` char(1) NOT NULL,
  `intento` int NOT NULL,
  PRIMARY KEY (`idUsuario`),
  UNIQUE KEY `nombre_UNIQUE` (`nombre`)
);

CREATE TABLE garaje.`rol` (
  `idRol` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) NOT NULL,
  PRIMARY KEY (`idRol`)
);

CREATE TABLE garaje.`rol` .`usuario_roles` (
  `usuarios_idusuario` int NOT NULL,
  `roles_idrol` int NOT NULL,
  PRIMARY KEY (`usuarios_idusuario`,`roles_idrol`),
  KEY `FK_Rol_idx` (`roles_idrol`),
  CONSTRAINT `FK_Rol` FOREIGN KEY (`roles_idrol`) REFERENCES `rol` (`idRol`),
  CONSTRAINT `FK_Usuario` FOREIGN KEY (`usuarios_idusuario`) REFERENCES `usuario` (`idUsuario`)
);

CREATE TABLE garaje.`estancia` (
  `idEstancia` int NOT NULL AUTO_INCREMENT,
  `idUsuario` int NOT NULL,
  `nombre` varchar(45) NOT NULL,
  `descripcion` varchar(250) NOT NULL,
  PRIMARY KEY (`idEstancia`),
  KEY `FK_EstanciaUsuario_idx` (`idUsuario`),
  CONSTRAINT `FK_EstanciaUsuario` FOREIGN KEY (`idUsuario`) REFERENCES `usuario` (`idUsuario`)
);

CREATE TABLE garaje.`estanteria` (
  `idEstanteria` int NOT NULL AUTO_INCREMENT,
  `idEstancia` int NOT NULL,
  `nombre` varchar(45) NOT NULL,
  `descripcion` varchar(250) NOT NULL,
  PRIMARY KEY (`idEstanteria`),
  KEY `FK_EstanteriaEstancia_idx` (`idEstancia`),
  CONSTRAINT `FK_EstanteriaEstancia` FOREIGN KEY (`idEstancia`) REFERENCES `estancia` (`idEstancia`)
); 

CREATE TABLE garaje.`categoria` (
  `idCategoria` int NOT NULL AUTO_INCREMENT,
  `categoriaPadre` int DEFAULT NULL,
  `nombre` varchar(45) NOT NULL,
  `esFija` char(1) NOT NULL DEFAULT 'N',
  `usuario` int NOT NULL,
  PRIMARY KEY (`idCategoria`),
  KEY `FK_CategoriaPadre_idx` (`categoriaPadre`),
  KEY `FK_CategoriaUsuario_idx` (`usuario`),
  CONSTRAINT `FK_CategoriaPadre` FOREIGN KEY (`categoriaPadre`) REFERENCES `categoria` (`idCategoria`),
  CONSTRAINT `FK_CategoriaUsuario` FOREIGN KEY (`usuario`) REFERENCES `usuario` (`idUsuario`)
);

CREATE TABLE garaje.`balda` (
  `idBalda` int NOT NULL AUTO_INCREMENT,
  `idEstanteria` int NOT NULL,
  `descripcion` varchar(45) DEFAULT NULL,
  `llena` char(1) NOT NULL DEFAULT 'N',
  `posicion` int NOT NULL DEFAULT '1',
  PRIMARY KEY (`idBalda`),
  KEY `FK_BaldaEstanteria_idx` (`idEstanteria`),
  CONSTRAINT `FK_BaldaEstanteria` FOREIGN KEY (`idEstanteria`) REFERENCES `estanteria` (`idEstanteria`)
);

CREATE TABLE garaje.`caja` (
  `idCaja` int NOT NULL AUTO_INCREMENT,
  `idBalda` int DEFAULT NULL,
  `descripcion` varchar(45) NOT NULL,
  `foto` longblob,
  `posicion` int NOT NULL DEFAULT '0',
  `idUsuario` int NOT NULL,
  PRIMARY KEY (`idCaja`),
  KEY `FK_CajaBalda_idx` (`idBalda`),
  KEY `FK_UsuarioCaja_idx` (`idUsuario`),
  CONSTRAINT `FK_CajaBalda` FOREIGN KEY (`idBalda`) REFERENCES `balda` (`idBalda`),
  CONSTRAINT `FK_UsuarioCaja` FOREIGN KEY (`idUsuario`) REFERENCES `usuario` (`idUsuario`)
);

CREATE TABLE garaje.`objeto` (
  `idObjeto` int NOT NULL AUTO_INCREMENT,
  `idCaja` int DEFAULT NULL,
  `nombre` varchar(45) NOT NULL,
  `marca` varchar(45) DEFAULT NULL,
  `descripcion` varchar(250) DEFAULT NULL,
  `foto` longblob,
  `posicion` int NOT NULL DEFAULT '0',
  `idCategoria` int NOT NULL,
  `idusuario` int NOT NULL,
  PRIMARY KEY (`idObjeto`),
  KEY `FK_ObjetoCaja_idx` (`idCaja`),
  KEY `FK_CategoriaObjeto_idx` (`idCategoria`),
  KEY `FK_ObjetoUsuario_idx` (`idusuario`),
  CONSTRAINT `FK_CategoriaObjeto` FOREIGN KEY (`idCategoria`) REFERENCES `categoria` (`idCategoria`),
  CONSTRAINT `FK_ObjetoCaja` FOREIGN KEY (`idCaja`) REFERENCES `caja` (`idCaja`),
  CONSTRAINT `FK_ObjetoUsuario` FOREIGN KEY (`idusuario`) REFERENCES `usuario` (`idUsuario`)
);

INSERT INTO `garaje`.`rol` (`idRol`, `nombre`) VALUES (1,'ROLE_USUARIO');
INSERT INTO `garaje`.`rol` (`idRol`, `nombre`) VALUES (2,'ROLE_ADMIN');

--Insertamos el usuario administrador
INSERT INTO `garaje`.`usuario` (`nombre`, `password`, `email`, `esActivo`, `intento`)
VALUES ('admin', '$2a$10$SuXGIzRXiGru5Fp//7rarOoSbhnGOWiFHM.Fx7N3or3PxDLtbD05q',
		'admin@uoc.edu', 'S', 0);

INSERT INTO `garaje`.`usuario_roles` (`usuarios_idusuario`,`roles_idrol`)
VALUES (1,2);
