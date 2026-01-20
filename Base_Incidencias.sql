-- MySQL dump 10.13  Distrib 8.0.44, for Win64 (x86_64)
--
-- Host: localhost    Database: gestion_incidencias
-- ------------------------------------------------------
-- Server version	8.0.44

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `historico_resoluciones`
--

DROP TABLE IF EXISTS `historico_resoluciones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `historico_resoluciones` (
  `id_historico` int NOT NULL AUTO_INCREMENT,
  `id_incidencia` int NOT NULL,
  `fecha_cambio` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `estado_anterior` enum('PENDIENTE','EN_PROCESO','ESPERA','CERRADA') DEFAULT NULL,
  `estado_nuevo` enum('PENDIENTE','EN_PROCESO','ESPERA','CERRADA') DEFAULT NULL,
  `prioridad_anterior` enum('BAJA','MEDIA','ALTA') DEFAULT NULL,
  `prioridad_nueva` enum('BAJA','MEDIA','ALTA') DEFAULT NULL,
  `comentario` text,
  `id_tecnico` int NOT NULL,
  PRIMARY KEY (`id_historico`),
  KEY `id_incidencia` (`id_incidencia`),
  KEY `id_tecnico` (`id_tecnico`),
  CONSTRAINT `historico_resoluciones_ibfk_1` FOREIGN KEY (`id_incidencia`) REFERENCES `incidencias` (`id_incidencia`),
  CONSTRAINT `historico_resoluciones_ibfk_2` FOREIGN KEY (`id_tecnico`) REFERENCES `usuarios` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `historico_resoluciones`
--

LOCK TABLES `historico_resoluciones` WRITE;
/*!40000 ALTER TABLE `historico_resoluciones` DISABLE KEYS */;
INSERT INTO `historico_resoluciones` VALUES (1,1,'2024-02-10 13:30:00','PENDIENTE','EN_PROCESO','ALTA','ALTA','Revisado el proyector. Necesita cambio de lámpara. Se solicita repuesto.',2),(2,1,'2024-02-10 15:00:00','EN_PROCESO','ESPERA','ALTA','ALTA','Pendiente de recibir lámpara de repuesto. Estimado 2 días.',2),(3,1,'2024-02-12 09:15:00','ESPERA','EN_PROCESO','ALTA','ALTA','Recibida lámpara. Procediendo al reemplazo.',2),(4,1,'2024-02-12 15:45:00','EN_PROCESO','CERRADA','ALTA','ALTA','Lámpara reemplazada y probada. Funcionando correctamente.',2),(5,2,'2024-02-15 09:00:00','PENDIENTE','EN_PROCESO','ALTA','ALTA','Revisando router principal del edificio B.',3),(6,2,'2024-02-15 14:30:00','EN_PROCESO','ESPERA','ALTA','ALTA','Router necesita reinicio completo. Programado para horario no lectivo.',3),(7,2,'2024-02-16 07:00:00','ESPERA','EN_PROCESO','ALTA','ALTA','Reiniciando router y configurando nuevamente.',3),(8,2,'2024-02-16 11:30:00','EN_PROCESO','CERRADA','ALTA','ALTA','Router operativo. Wifi funcionando en todo el edificio.',3),(9,3,'2024-02-20 12:15:00','PENDIENTE','EN_PROCESO','MEDIA','MEDIA','Diagnosticando error en AutoCAD.',2),(10,3,'2024-02-20 15:45:00','EN_PROCESO','ESPERA','MEDIA','MEDIA','Necesaria reinstalación. Esperando backups.',2),(11,3,'2024-02-21 08:00:00','ESPERA','EN_PROCESO','MEDIA','MEDIA','Reinstalando software en los equipos afectados.',2),(12,3,'2024-02-21 09:20:00','EN_PROCESO','CERRADA','MEDIA','MEDIA','Software reinstalado y funcionando. Usuarios notificados.',2),(13,4,'2024-01-25 14:30:00','PENDIENTE','EN_PROCESO','MEDIA','MEDIA','Revisando atascamiento en impresora 3D.',3),(14,4,'2024-01-25 15:45:00','EN_PROCESO','ESPERA','MEDIA','MEDIA','Esperando piezas de repuesto para el extrusor.',3),(15,4,'2024-01-26 13:00:00','ESPERA','EN_PROCESO','MEDIA','MEDIA','Reparando extrusor con piezas nuevas.',3),(16,4,'2024-01-26 16:00:00','EN_PROCESO','CERRADA','MEDIA','MEDIA','Impresora reparada y calibrada. Prueba exitosa.',3),(17,5,'2024-02-05 10:30:00','PENDIENTE','EN_PROCESO','MEDIA','ALTA','Mismo problema recurrente. Prioridad elevada.',3),(18,5,'2024-02-05 14:20:00','EN_PROCESO','ESPERA','ALTA','ALTA','Problema más grave de lo esperado. Contactando con soporte técnico fabricante.',3),(19,5,'2024-02-07 07:30:00','ESPERA','EN_PROCESO','ALTA','ALTA','Reemplazando componente completo siguiendo instrucciones fabricante.',3),(20,5,'2024-02-07 08:15:00','EN_PROCESO','CERRADA','ALTA','ALTA','Componente reemplazado. Impresora funcionando correctamente.',3),(21,6,'2024-02-23 14:15:00','PENDIENTE','EN_PROCESO','MEDIA','MEDIA','Revisando unidad de aire acondicionado.',3),(22,6,'2024-02-23 16:30:00','EN_PROCESO','ESPERA','MEDIA','MEDIA','Necesita recarga de gas. Esperando técnico especializado.',3),(23,6,'2024-02-24 08:00:00','ESPERA','EN_PROCESO','MEDIA','MEDIA','Técnico especializado realizando recarga.',3),(24,6,'2024-02-24 09:30:00','EN_PROCESO','CERRADA','MEDIA','MEDIA','Recarga completada. Aula a temperatura correcta.',3),(25,7,'2024-02-19 08:00:00','PENDIENTE','EN_PROCESO','MEDIA','MEDIA','Limpiando rodillos de la fotocopiadora.',3),(26,7,'2024-02-19 13:30:00','EN_PROCESO','ESPERA','MEDIA','MEDIA','Esperando entrega de rodillos de repuesto.',3),(27,7,'2024-02-20 09:00:00','ESPERA','EN_PROCESO','MEDIA','MEDIA','Reemplazando rodillos desgastados.',3),(28,7,'2024-02-20 10:45:00','EN_PROCESO','CERRADA','MEDIA','MEDIA','Rodillos reemplazados. Fotocopiadora funcionando sin atascos.',3),(29,8,'2024-02-16 15:30:00','PENDIENTE','EN_PROCESO','MEDIA','MEDIA','Probando monitor con diferentes cables y puertos.',3),(30,8,'2024-02-16 16:45:00','EN_PROCESO','ESPERA','MEDIA','MEDIA','Monitor defectuoso. Solicitud de reemplazo.',3),(31,8,'2024-02-17 07:30:00','ESPERA','EN_PROCESO','ALTA','MEDIA','Instalando monitor de reemplazo.',3),(32,8,'2024-02-17 08:15:00','EN_PROCESO','CERRADA','MEDIA','MEDIA','Monitor instalado y configurado. Usuario satisfecho.',3),(33,9,'2024-02-25 09:30:00','PENDIENTE','EN_PROCESO','MEDIA','ALTA','Revisando calibración de pantalla táctil. Prioridad elevada por ser equipo crítico.',2),(34,9,'2024-02-26 10:45:00','EN_PROCESO','EN_PROCESO','ALTA','ALTA','Problema en controlador táctil. Buscando solución alternativa.',2),(35,10,'2024-02-28 16:20:00','PENDIENTE','EN_PROCESO','ALTA','BAJA','Investigando fuente del zumbido en altavoces.',3),(36,11,'2024-02-21 15:45:00','PENDIENTE','EN_PROCESO','ALTA','ALTA','Revisando configuración de Moodle.',2),(37,11,'2024-02-22 09:15:00','EN_PROCESO','ESPERA','ALTA','ALTA','Aumentando límite de tamaño de archivos subidos.',2),(38,12,'2024-02-29 08:00:00','PENDIENTE','EN_PROCESO','MEDIA','BAJA','Prioridad elevada por ser equipo del profesor. Diagnosticando problema.',2),(39,13,'2024-02-22 13:00:00','PENDIENTE','EN_PROCESO','BAJA','BAJA','Verificando inventario de ratones.',2),(40,13,'2024-02-23 08:30:00','EN_PROCESO','ESPERA','BAJA','BAJA','Esperando entrega de ratones del almacén central.',2),(41,14,'2024-02-24 10:45:00','PENDIENTE','EN_PROCESO','ALTA','MEDIA','Preparando actualización de software de idiomas.',3),(42,14,'2024-02-24 15:00:00','EN_PROCESO','ESPERA','MEDIA','ALTA','Esperando aprobación de dirección para licencias nuevas.',3),(43,15,'2024-02-26 10:00:00','PENDIENTE','EN_PROCESO','MEDIA','MEDIA','Diagnosticando lector de tarjetas.',2),(44,15,'2024-02-26 13:30:00','EN_PROCESO','ESPERA','BAJA','MEDIA','Esperando repuesto del sensor óptico.',2),(45,16,'2024-02-18 13:10:00','PENDIENTE','EN_PROCESO','ALTA','ALTA','Diagnosticando router sala profesores.',2),(46,16,'2024-02-18 15:45:00','EN_PROCESO','ESPERA','ALTA','ALTA','Router defectuoso. Esperando envío de equipo de reemplazo.',2),(47,17,'2024-02-29 09:30:00','PENDIENTE','EN_PROCESO','ALTA','MEDIA','Revisando sistema de videoconferencia.',3),(48,17,'2024-02-29 10:45:00','EN_PROCESO','ESPERA','ALTA','ALTA','Necesita actualización de firmware. Esperando autorización.',3),(49,21,'2025-12-19 21:45:34','PENDIENTE','EN_PROCESO','ALTA','ALTA','Solución iniciada',2),(50,21,'2025-12-19 21:45:50','EN_PROCESO','EN_PROCESO','ALTA','MEDIA','sjhsfkjhsdf',2),(51,21,'2025-12-19 21:46:30','EN_PROCESO','CERRADA','MEDIA','MEDIA','ya se ha solucioando',2);
/*!40000 ALTER TABLE `historico_resoluciones` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `incidencias`
--

DROP TABLE IF EXISTS `incidencias`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `incidencias` (
  `id_incidencia` int NOT NULL AUTO_INCREMENT,
  `titulo` varchar(200) NOT NULL,
  `descripcion` text NOT NULL,
  `fecha_alta` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_inicio` timestamp NULL DEFAULT NULL,
  `fecha_fin` timestamp NULL DEFAULT NULL,
  `estado` enum('PENDIENTE','EN_PROCESO','ESPERA','CERRADA') DEFAULT 'PENDIENTE',
  `prioridad` enum('BAJA','MEDIA','ALTA') DEFAULT 'MEDIA',
  `id_autor` int NOT NULL,
  `id_tecnico_asignado` int DEFAULT NULL,
  `id_incidencia_anterior` int DEFAULT NULL,
  `activa` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id_incidencia`),
  KEY `id_autor` (`id_autor`),
  KEY `id_tecnico_asignado` (`id_tecnico_asignado`),
  KEY `id_incidencia_anterior` (`id_incidencia_anterior`),
  CONSTRAINT `incidencias_ibfk_1` FOREIGN KEY (`id_autor`) REFERENCES `usuarios` (`id_usuario`),
  CONSTRAINT `incidencias_ibfk_2` FOREIGN KEY (`id_tecnico_asignado`) REFERENCES `usuarios` (`id_usuario`),
  CONSTRAINT `incidencias_ibfk_3` FOREIGN KEY (`id_incidencia_anterior`) REFERENCES `incidencias` (`id_incidencia`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `incidencias`
--

LOCK TABLES `incidencias` WRITE;
/*!40000 ALTER TABLE `incidencias` DISABLE KEYS */;
INSERT INTO `incidencias` VALUES (1,'Proyector aula 101 no enciende','El proyector del aula 101 no responde al mando ni al botón de encendido. Se necesita revisión técnica.','2024-02-10 08:15:00','2024-02-10 13:30:00','2024-02-12 15:45:00','CERRADA','ALTA',4,2,NULL,1),(2,'Fallo en red wifi edificio B','Los dispositivos no se conectan a la red WiFi en las aulas del edificio B desde esta mañana.','2024-02-15 07:45:00','2024-02-15 09:00:00','2024-02-16 11:30:00','CERRADA','ALTA',5,3,NULL,1),(3,'Software CAD no funciona','El programa AutoCAD da error al iniciar en los ordenadores del aula de informática 3.','2024-02-20 10:30:00','2024-02-20 12:15:00','2024-02-21 09:20:00','CERRADA','MEDIA',6,2,NULL,1),(4,'Impresora 3D atascada','La impresora 3D del laboratorio se ha atascado durante una impresión.','2024-01-25 13:20:00','2024-01-25 14:30:00','2024-01-26 16:00:00','CERRADA','MEDIA',7,3,NULL,1),(5,'Impresora 3D vuelve a dar problemas','La impresora 3D presenta el mismo problema de atascamiento después de la reparación.','2024-02-05 09:45:00','2024-02-05 10:30:00','2024-02-07 08:15:00','CERRADA','ALTA',7,3,4,1),(6,'Aire acondicionado aula 102 no enfría','El aire acondicionado del aula 102 no baja de 25ºC aunque esté al máximo.','2024-02-23 13:30:00','2024-02-23 14:15:00','2024-02-24 09:30:00','CERRADA','MEDIA',5,3,NULL,1),(7,'Fotocopiadora atascada papel','La fotocopiadora de la planta baja se atasca constantemente con papel.','2024-02-19 07:15:00','2024-02-19 08:00:00','2024-02-20 10:45:00','CERRADA','MEDIA',7,3,NULL,1),(8,'Monitor auxiliar no funciona','El segundo monitor del puesto de administración no enciende.','2024-02-16 14:45:00','2024-02-16 15:30:00','2024-02-17 08:15:00','CERRADA','MEDIA',6,3,NULL,1),(9,'Pantalla interactiva aula 203 con fallos táctiles','La pantalla responde mal al tacto en ciertas zonas, dificultando su uso.','2024-02-25 08:00:00','2024-02-25 09:30:00',NULL,'EN_PROCESO','ALTA',4,2,NULL,1),(10,'Equipo de sonido auditorio con ruido','Se escucha un zumbido constante en los altavoces del auditorio durante las presentaciones.','2024-02-28 15:45:00','2024-02-28 16:20:00',NULL,'EN_PROCESO','ALTA',5,3,NULL,1),(11,'Problema con plataforma e-learning','Los estudiantes no pueden subir tareas en la plataforma Moodle.','2024-02-21 15:00:00','2024-02-21 15:45:00',NULL,'EN_PROCESO','ALTA',6,2,NULL,1),(12,'Teclado aula 202 con teclas pegadas','Las teclas F1-F12 no responden correctamente en el ordenador del profesor.','2024-02-29 07:30:00','2024-02-29 08:00:00',NULL,'EN_PROCESO','ALTA',4,2,NULL,1),(13,'Reposición ratones aula 104','Faltan 5 ratones inalámbricos en el aula 104, se solicita reposición.','2024-02-22 12:15:00','2024-02-22 13:00:00',NULL,'ESPERA','BAJA',6,2,NULL,1),(14,'Actualización software laboratorio idiomas','Necesaria actualización del software de los ordenadores del laboratorio de idiomas.','2024-02-24 10:00:00','2024-02-24 10:45:00',NULL,'ESPERA','MEDIA',7,3,NULL,1),(15,'Lector de tarjetas no funciona','El lector de tarjetas de la biblioteca no reconoce las credenciales.','2024-02-26 08:45:00','2024-02-26 10:00:00',NULL,'ESPERA','MEDIA',4,2,NULL,1),(16,'Router sala profesores sin conexión','El router de la sala de profesores no proporciona internet desde hoy.','2024-02-18 12:20:00','2024-02-18 13:10:00',NULL,'ESPERA','ALTA',4,2,NULL,1),(17,'Fallo en sistema de videoconferencia','El sistema de videoconferencia de la sala de reuniones no conecta audio.','2024-02-29 09:15:00','2024-02-29 09:30:00',NULL,'ESPERA','ALTA',5,3,NULL,1),(18,'Cámara seguridad pasillo 1º no transmite','La cámara del pasillo de la primera planta no envía señal desde ayer.','2024-02-29 11:45:00',NULL,NULL,'PENDIENTE','ALTA',6,NULL,NULL,1),(19,'Silla aula 301 rota','Una silla del aula 301 tiene una pata rota, riesgo de accidente.','2024-02-27 14:20:00',NULL,NULL,'PENDIENTE','BAJA',7,NULL,NULL,1),(20,'Software estadístico desactualizado','El software SPSS necesita actualización a la versión 29.','2024-02-17 09:30:00',NULL,NULL,'PENDIENTE','BAJA',5,NULL,NULL,1),(21,'brodi se me ha cascado el pene','ayuda','2025-12-19 21:45:10','2025-12-19 21:45:34','2025-12-19 21:46:30','CERRADA','MEDIA',5,2,NULL,1),(22,'brodi se me ha cascado el pene','ayuda','2025-12-19 21:47:51',NULL,NULL,'PENDIENTE','MEDIA',5,NULL,21,1);
/*!40000 ALTER TABLE `incidencias` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `logins`
--

DROP TABLE IF EXISTS `logins`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `logins` (
  `id_login` int NOT NULL AUTO_INCREMENT,
  `id_usuario` int NOT NULL,
  `fecha_login` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_logout` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id_login`),
  KEY `id_usuario` (`id_usuario`),
  CONSTRAINT `logins_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `logins`
--

LOCK TABLES `logins` WRITE;
/*!40000 ALTER TABLE `logins` DISABLE KEYS */;
INSERT INTO `logins` VALUES (1,1,'2025-12-14 14:13:37','2025-12-19 21:47:30'),(2,2,'2025-12-14 14:14:04','2026-01-20 11:33:23'),(3,2,'2025-12-14 14:14:48','2026-01-20 11:33:23'),(4,5,'2025-12-14 14:14:55','2026-01-20 11:32:38'),(5,2,'2025-12-14 14:15:03','2026-01-20 11:33:23'),(6,1,'2025-12-14 14:15:22','2025-12-19 21:47:30'),(7,5,'2025-12-19 21:44:00','2026-01-20 11:32:38'),(8,2,'2025-12-19 21:44:26','2026-01-20 11:33:23'),(9,1,'2025-12-19 21:44:41','2025-12-19 21:47:30'),(10,5,'2025-12-19 21:45:01','2026-01-20 11:32:38'),(11,2,'2025-12-19 21:45:23','2026-01-20 11:33:23'),(12,5,'2025-12-19 21:46:01','2026-01-20 11:32:38'),(13,2,'2025-12-19 21:46:15','2026-01-20 11:33:23'),(14,1,'2025-12-19 21:47:11','2025-12-19 21:47:30'),(15,5,'2025-12-19 21:47:36','2026-01-20 11:32:38'),(16,5,'2026-01-08 00:54:03','2026-01-20 11:32:38'),(17,5,'2026-01-20 11:30:12','2026-01-20 11:32:38'),(18,2,'2026-01-20 11:32:44','2026-01-20 11:33:23'),(19,1,'2026-01-20 11:33:27',NULL);
/*!40000 ALTER TABLE `logins` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id_usuario` int NOT NULL AUTO_INCREMENT,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `apellidos` varchar(100) NOT NULL,
  `perfil` enum('USUARIO','TECNICO','ADMINISTRADOR') NOT NULL,
  `activo` tinyint(1) DEFAULT '1',
  `fecha_creacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'admin@centro.edu','123','Admin','Principal','ADMINISTRADOR',1,'2025-12-14 14:13:22'),(2,'gorka@centro.edu','123','Gorka','Ajangiz','TECNICO',1,'2025-12-14 14:13:22'),(3,'edrian@centro.edu','123','Edrian','Mendoza','TECNICO',1,'2025-12-14 14:13:22'),(4,'andoni@centro.edu','123','Andoni','Lamas','USUARIO',1,'2025-12-14 14:13:22'),(5,'julen@centro.edu','123','Julen','García','USUARIO',1,'2025-12-14 14:13:22'),(6,'victor@centro.edu','123','Victor','Sanchez','USUARIO',1,'2025-12-14 14:13:22'),(7,'arkhyp@centro.edu','123','Arkhyp','Lebedenko','USUARIO',1,'2025-12-14 14:13:22'),(8,'sergio@centro.edu','123','Sergio','Godoy','USUARIO',1,'2025-12-14 14:13:22');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-01-20 13:05:48
