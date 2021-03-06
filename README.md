# Implementación Codec de video
 Este proyecto se basa en la realización de un codec para la visualización posterior del vídeo. Principalmente sus uso se basa en la compresión y descompresión de archivos, codificación y descodificación de imágenes y posterior visualización de estas. Por último, también se aporta información acerca de la eficacia de compresión y descompresión en susodicho proceso.


> **Autores:** Daniel Yeste y Salvador Mira .

# Herramientas de creación

Para desarrollar esta práctica, hemos utilizado el IDE Intell IJ Idea y hemos basado nuestra compilación en la versión 1.8 de java.

· [Intell IJ Idea](https://www.jetbrains.com/idea)
· [Java 1.8](https://www.oracle.com/java/technologies/javase/jdk1.8-archive-downloads.html)




## Ejecución

Para la ejecución de nuestro algoritmo, se deben ejecutar los comandos en la carpeta principal (donde está el archivo F1.jar).

Para las opciones -i y -o es importante que se especifique todo el path, por ejemplo:

`
java -jar F1.jar -i /home/danielyeste/IdeaProjects/F1/Cubo.zip --batch
`

Ejemplo para la ejecución por consola sin filtros:

`
java -jar F1.jar -i /home/danielyeste/IdeaProjects/F1/Cubo.zip --fps 60 -o /home/danielyeste/Desktop/Das/Tet.zip
`

Ejemplo para la ejecución por consola del codec:

`
-i /home/danielyeste/IdeaProjects/F1/Cubo.zip -o /home/danielyeste/Desktop/Das/Tet.zip --encode --decode --quality 7 --nTiles 16 --GOP 4 --seekRange 0 --fps 24
`

Hay un archivo .txt con más ejemplos más específicos listos para ejecutar, que son los que usamos en la demo final del proyecto.

## Especificaciones para la ejecución

1. Si se intenta reproducir el vídeo, es decir se ejecuta el programa sin el argumento --batch se debe especificar el número de fps, con --fps <value>.
2. Para usar el argumento de codificación --encode, se han de especificar el gop(--GOP <value>) seekRange(--seekRange<value>) y el número de teselas 
 (--nTiles<4,16,64>).

También se puede ejecutar desde el IDE con
argumentos sin ningun problema. El proyecto se ha realizado en 
jdk 1.8 y es un tipo de proyecto maven, un organizador de 
paquetes de java. Para su correcta importación y ejecución
se debe trabajar en un proyecto maven.
`
## Libreras utilizadas

· Jcommander

· Tongfei ProgressBar

· Apache Math

· Jinahyay Ocap

# Últimas correciones
Respecto a la demostración se han añadido las opciones para seekRange (aunque sólo por teselas en vez de píxeles ya que no nos ha dado tiempo a cambiar toda la estructura interna de las teselas) de manera que el algoritmo se adecue a las peticiones. También se han fixeado los problemas con GOP(Group of Files).
