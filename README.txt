Este fue un proyecto en el que nos encargaron crear una aplicación en Swing capaz de gestionar incidencias 
en una empresa real, con varios objetivos opcionales. Yo realicé todos los objetivos y la hice en JavaFX 
(que daba un punto extra). Además de esto, hice el javadoc correspondiente, muchas opciones adicionales
de filtrado y ajustes, depuré el código, hice testing de errores y escribí el modelo, DAO y gestor a mano 
para hacerlo a mi gusto. Se puede acceder como usuario, administrador y técnico; los usuarios pueden crear
incidencias, editarlas, eliminarlas (baja lógica, permanece en la base de datos) y reabrirlas.
Los técnicos pueden solucionar, poner en espera, cerrar, reabrir y editar (solo la prioridad). El administrador
puede hacer todo lo que los técnicos y usuarios pueden, y, adicionalmente, también tiene la opción de asignar
un técnico concreto a una incidencia y de borrarla permanentemente de la base de datos. Además de esto, se puede
filtrar por estado, por prioridad, mostrar las cerradas, quitar etiquetas de los botones, ver toda la información
de una incidencia, ver el histórico de cambios de esa incidencia, generar un informe (en .txt) de las incidencias
en la base de datos y cuenta con algunas funciones visuales como un reloj, actualización automática cada sesenta
segundos y un logo de empresa y de perfil de usuario (ambos placeholders). Las cuentas de la base de datos
suministrada son las siguientes:

USUARIOS:            CONTRASEÑA:
andoni@centro.edu	123
julen@centro.edu	123
victor@centro.edu	123
arkhyp@centro.edu	123
sergio@centro.edu	123

TECNICOS:            CONTRASEÑA:
gorka@centro.edu	123
edrian@centro.edu	123


ADMINISTRADOR:       CONTRASEÑA:
admin@centro.edu	123
