package game.gfx;

import game.util.MathUtil;

import java.awt.image.BufferedImage;
import java.util.Random;

public class TileGenerator {

    public static final int[] grass = TileGenerator.genTexture(16, 0x4A823F, 0x659352, 0.15);
    public static final int[] water = TileGenerator.genTexture(16, 0x2D64E5, 0x408DED, 0.001);
    public static final int[] sand = TileGenerator.genTexture(16, 0xDBB600, 0xE8D617, 0.03);

    public static final int[] genTexture(int size, int baseCol, int col, double rnd) {
        Random random = new Random(3271);
        int[] result = new int[size * size];
        for (int i = 0; i < size * size; i++) {
            result[i] = baseCol;
            if (random.nextDouble() > rnd) continue;
            result[i] = col;
        }

        return result;
    }

    public static final BufferedImage[] gen(int[] t0, int[] t1, int size) {
        int[] hm = new NoiseMap().getNoise(1019, 4, 2);
        double iSize = 1.0 / size;
        BufferedImage[] result = new BufferedImage[16];
        for (int i = 0; i < 16; i++) {
            result[i] = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);

            int[] data = new int[size * size];

            int a = (i >> 0) & 1;
            int b = (i >> 1) & 1;
            int c = (i >> 2) & 1;
            int d = (i >> 3) & 1;

            for (int y = 0; y < size; y++) {
                double yy = y * iSize;
                for (int x = 0; x < size; x++) {
                    double xx = x * iSize;

                    double ab = a + (b - a) * xx;
                    double cd = c + (d - c) * xx;

                    double val = ab + (cd - ab) * yy;
                    val = val * 4.0 - 0.4 + hm[(int)(xx * 16) + (int)(yy * 16) * 16] * 0.005;
                    if (val < 0) val = 0;
                    if (val > 1) val = 1;

                    int c0 = t0[x + y * size];
                    int c1 = t1[x + y * size];
                    data[x + y * size] = MathUtil.lerpRGB(c0, c1, val);
                }
            }

            result[i].setRGB(0, 0, size, size, data, 0, size);

        }

        return result;
    }

}