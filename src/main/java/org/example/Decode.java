package org.example;

import me.tongfei.progressbar.ProgressBar;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Decode {
    private int nTiles;
    private int gop;
    private ArrayList<EncodedImages> encodedListedImages;

    /**
     * Inicialitza el decode.
     */
    public Decode(){
        this.encodedListedImages = new ArrayList<>();
    }

    /**
     * Procés íntegre del decodificador.
     * @throws IOException
     */
    public void decode() throws IOException {
        Path currentRelativePath = Paths.get("");

        String destDir = currentRelativePath.toAbsolutePath()+"/EncodedImages";

        File f = new File(destDir);

        File[] files = f.listFiles();

        // Obrim el fitxer on tenim guardada tota la informació de les teseles substituïdes.
        File file = new File("encode_information.txt");
        Scanner sc = new Scanner(file);

        // Llegim la primera línia que ens indicarà quantes teseles i quin GOP haurem de fer servir per a reconstruir
        // les imatges.
        String line = sc.nextLine();
        this.nTiles = Integer.valueOf(line.split(" ")[0]);
        this.gop = Integer.valueOf(line.split(" ")[1]);

        // A partir de les imatges codificades, crea una llista d'imatges teseleades.
        for(File image: files) {
            EncodedImages encodedImage = new EncodedImages(nTiles, image);
            encodedListedImages.add(encodedImage);
        }

        // Reconstrueix les imatges.
        rebuildImages();

        // Guarda les imatges reconstruides en una carpeta per a poder-les veure.
        saveDecodedImages();
    }

    /**
     * Reconstrueix les imatges a partir de les imatges codificades teselades i un txt amb la informació de les
     * teseles substituides.
     * @throws FileNotFoundException
     */
    public void rebuildImages() throws FileNotFoundException {
        ProgressBar pb = new ProgressBar("Decoding files", 100); // name, initial max
        pb.start();

        // Obrim el fitxer txt que conté la informació de les teseles substituïdes.
        File file = new File("encode_information.txt");
        Scanner sc = new Scanner(file);

        // Llegim la primera línia que conté la quantitat de teseles per imatge i el GOP.
        // No fa falta que guardem les dades ja que les hem guardat al principi del mètode decode(),
        // tot i així l'hem de llegir per a començar a llegir els valors de les teseles substituïdes.
        String line = sc.nextLine();

        // Itera per a cada línia del fitxer txt per a reconstruir les imatges tesela a tesela.
        while(sc.hasNextLine()){
            line = sc.nextLine();

            // De cada línia obtenim el número d'imatge a la que correspon la tesela
            // i el número de tesela que hem de recuperar
            int numImage = Integer.valueOf(line.split(" ")[0]);
            int numTessel = Integer.valueOf(line.split(" ")[1]);

            // Agafem la tesela corresponent al número de tesela que hem de recuperar de la imatge model
            // i la posem a la imatge a reconstruir.
            for(int i = 0; i < encodedListedImages.get(numImage).getTesselsList()[numTessel].length; i++){
                for(int j = 0; j < encodedListedImages.get(numImage).getTesselsList()[numTessel][i].length; j++){
                    encodedListedImages.get(numImage).getTesselsList()[numTessel][i][j] = encodedListedImages.get(numImage - (numImage % gop)).getTesselsList()[numTessel][i][j];
                }
            }

            pb.step();
        }

        pb.stop();
    }

    /**
     * Agafa les imatges teselades, les converteix en imatges en format jpg i les guarda en una carpeta.
     * @throws IOException
     */
    public void saveDecodedImages() throws IOException {
        Path currentRelativePath = Paths.get("");

        Path path = Paths.get(currentRelativePath.toAbsolutePath()+"/DecodedImages");
        Files.createDirectories(path);

        // Iterem sobre la llista d'imatges teselades
        for (int x = 0; x < encodedListedImages.size(); x++){

            // Obtenim un BI a partir d'una imatge teselada.
            BufferedImage bufferedImage = encodedListedImages.get(x).buildBufferedImage();
            File file = new File("");
            file = new File(currentRelativePath.toAbsolutePath()+"/DecodedImages/Cubo"+String.format("%02d", x)+".jpg");


            try {
                ImageIO.write(bufferedImage, "jpg", file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
