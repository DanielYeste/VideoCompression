# Implementación F1


> **Autores:** Daniel Yeste y Salvador Mira .

# Herramientas de creación

Para desarrollar esta práctica, hemos utilizado el IDE Intell IJ Idea y hemos basado nuestra compilación en la versión 1.8 de java.

· [Intell IJ Idea](https://www.jetbrains.com/idea)
· [Java 1.8](https://www.oracle.com/java/technologies/javase/jdk1.8-archive-downloads.html)




# Ejecución

Para la ejecución de nuestro algoritmo, se deben usar los comandos
que se pueden encontrar en: 

`
java -jar F1.jar -h 
`

Para la opción -o, es importante que se especifique todo el path, por ejemplo:

`
java -jar F1.jar -o /home/danielyeste/Desktop/Das/Test.zip
`

Ejemplo para la ejecución por consola sin filtros:

`
java -jar F1.jar -i /home/danielyeste/IdeaProjects/F1/Cubo.zip --fps 60 -o /home/danielyeste/Desktop/Das/Tet.zip
`
## Informacion modificada
Tras los problemas en clase, hemos hecho un fix de última hora
para que se pueda probar sin problema. Simplemente teníamos que poner las librerías en la carpeta del JAR.
Por tanto, es importante que para ejecutar el **jar** desde consola se haga desde
la carpeta de este mismo proyecto, la cual se encuentra en:

`
F1/out/artifacts/F1_jar
`

También se puede ejecutar desde el IDE con
argumentos sin ningun problema. El proyecto se ha realizado en 
jdk 1.8 y es un tipo de proyecto maven, un organizador de 
paquetes de java. Para su correcta importación y ejecución
se debe trabajar en un proyecto maven.


# Últimas correciones
Se han eliminado las preguntas en la interfaz para agilizar el proceso de ejecución.
Eliminación arte AASCII.
Correción en uso de filtros

Rehacer la parte de vídeo para usar los hilos.

Rehacer la parte de filtros de imagen. MarvinImage

Negative filter y convolutional filter
 
Negative filter hecho, video hecho.
Convolutional filter por hacer
Timer y TimerTask