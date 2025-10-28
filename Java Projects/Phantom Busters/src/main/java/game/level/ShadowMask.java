package game.level;

import game.gfx.Bitmap;

import java.util.Random;

public class ShadowMask {

    private static final Random random = new Random();
    public Bitmap[] masks = new Bitmap[32];
    private int[] hm = new NoiseMap().getNoise(1019, 4, 1);

    public ShadowMask() {
        for (int i = 0; i < 16; i++) {
            masks[i] = gen(i);
            masks[i + 16] = gen2(i);
        }
    }

    private Bitmap gen(int corners) {
        int w = 96;
        Bitmap bm = new Bitmap(w, w);

        int a = (corners >> 0) & 1;
        int b = (corners >> 1) & 1;
        int c = (corners >> 2) & 1;
        int d = (corners >> 3) & 1;

        int rr = 0;
        int gg = 0;
        int bb = 0;

        for (int y = 0; y < w; y++) {
            for (int x = 0; x < w; x++) {
                double xx = ((x + 0.5) / 2 + y - 64) / 48;
                double yy = (y - (x + 0.5) / 2 - 16) / 48;

                rr = gg = bb = random.nextInt(200) / 199 * 10 + 10;

                if (xx >= 0 && yy >= 0 && xx < 1 && yy < 1) {
                    double ab = a + (b - a) * xx;
                    double cd = c + (d - c) * xx;

                    double val = ab + (cd - ab) * yy;
                    val = val * 4 - 0.4 + hm[(int)(xx * 8) + (int)(yy * 8) * 16] * 0.005;
                    if (val < 0) val = 0;
                    if (val > 1) val = 1;

                    int aa = 255 - (int)(val * 255);
                    if (corners == 0) aa = 255;
                    bm.pixels[x + y * w] = (aa << 24) | (rr << 16) | (gg << 8) | bb;
                }
            }
        }

        return bm;
    }

    private Bitmap gen2(int corners) {
        int w = 96;
        Bitmap bm = new Bitmap(w, w);

        int a = (corners >> 0) & 1;
        int b = (corners >> 1) & 1;
        int c = (corners >> 2) & 1;
        int d = (corners >> 3) & 1;

        for (int y = 0; y < w; y++) {
            for (int x = 0; x < w; x++) {
                double xx = ((x + 0.5) / 2 + y - 64) / 48;
                double yy = (y - (x + 0.5) / 2 - 16) / 48;

                if (xx >= 0 && yy >= 0 && xx < 1 && yy < 1) {
                    double ab = a + (b - a) * xx;
                    double cd = c + (d - c) * xx;

                    double val = ab + (cd - ab) * yy;
                    val = val * 4 - 0.5 + hm[(int)(xx * 8) + (int)(yy * 8) * 16] * 0.005;
                    if (val < 0) val = 0;
                    if (val > 1) val = 1;

                    int col = (int)(val * 255);

                    bm.pixels[x + y * w] = 0xFF000000 | col;
                }
            }
        }

        return bm;
    }

}