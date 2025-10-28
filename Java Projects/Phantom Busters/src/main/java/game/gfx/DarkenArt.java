package game.gfx;

import java.util.Random;

public class DarkenArt {

    private static final Random random = new Random();
    private static final int TWO_POW_15 = (int) Math.pow(2, 15);

    public static Bitmap darken(Bitmap b) {
        darkenColumns(b);
        darkenBottom(b);
        darkenRandom(b);
        return b;
    }

    private static void darkenRandom(Bitmap b) {
        for (int i = 0; i < b.w * b.h; i++) {
            b.pixels[i] = darkenPixel(b.pixels[i], random.nextInt(15) + 10);
        }
    }

    private static void darkenColumns(Bitmap b) {
        for (int y = 0; y < b.h; y++) {
            for (int x = 0; x < b.w; x += 8) {
                for (int i = 0; i < 4; i++) {
                    int index = y * b.w + x + i;
                    b.pixels[index] = darkenPixel(b.pixels[index]);
                }
            }
        }
    }

    private static void darkenBottom(Bitmap b) {
        int darkenCount = b.w * 2;
        for (int y = 0; y < b.h / 4; y++) {
            for (int x = 0; x < darkenCount; x++) {
                int index = darkenCount + x + y * 4 * b.w;
                b.pixels[index] = darkenPixel(b.pixels[index]);
            }
        }
    }

    public static int darkenPixel(int pixel) { return darkenPixel(pixel, -1); }

    private static int darkenPixel(int pixel, int tone) {
        int a = (pixel >> 24) & 0xFF;
        int r = (pixel >> 16) & 0xFF;
        int g = (pixel >> 8) & 0xFF;
        int b = pixel & 0xFF;

        int colSum = r + g + b;

        if (tone == -1) tone = 7 + (int)(Math.pow(colSum, 2) / (double)TWO_POW_15);

        r = Math.max(0, Math.min(255, r - tone));
        g = Math.max(0, Math.min(255, g - tone));
        b = Math.max(0, Math.min(255, b - tone));

        return (a << 24) | (r << 16) | (g << 8) | b;
    }

}