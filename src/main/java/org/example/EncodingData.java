package org.example;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class that keeps the encoding data.
 * Has the number of tiles per image, the GOP value and a list of the tiles for every image.
 */
public class EncodingData implements Serializable {
    private ArrayList<int[]> data;
    private int nTiles;
    private int gop;

    /**
     * Initializes the object.
     * @param nTiles Number of tiles per imatge.
     * @param gop GOP
     */
    public EncodingData(int nTiles, int gop) {
        this.nTiles = nTiles;
        this.gop = gop;
        this.data = new ArrayList<>();
    }

    /**
     * Adds the tile of an image and its corresponding to the previous image.
     * @param index
     * @param k
     * @param i
     * @param prefk
     * @param prefi
     */
    public void addData(int index, int k, int i, int prefk, int prefi){
        int[] teselInfo = new int[5];
        teselInfo[0] = index;
        teselInfo[1] = k;
        teselInfo[2] = i;
        teselInfo[3] = prefk;
        teselInfo[4] = prefi;

        data.add(teselInfo);
    }

    /**
     * Returns the quantity of tiles per image.
     * @return Quantitat de teseles per imatge.
     */
    public int getnTiles() {
        return nTiles;
    }

    /**
     * Returns the GOP value.
     * @return GOP.
     */
    public int getGop() {
        return gop;
    }

    /**
     * Returns the list with the tiles data.
     * @return List with the tiles data.
     */
    public ArrayList<int[]> getData() {
        return data;
    }
}
