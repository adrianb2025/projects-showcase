package game.gfx.util;

import game.gfx.Bitmap;
import game.gfx.weather.Weather;
import game.gfx.weather.WeatherManager;

public class PaletteHelper {

    private int[] colors = new int[256];
    private static PaletteHelper i = new PaletteHelper();

    public static PaletteHelper getInstance() { return i; }

    private PaletteHelper() { init(); }

    private void init() {
        int pp = 0;
        for (int r = 0; r < 6; r++) {
            for (int g = 0; g < 6; g++) {
                for (int b = 0; b < 6; b++) {
                    int rr = (r * 255 / 5);
                    int gg = (g * 255 / 5);
                    int bb = (b * 255 / 5);

                    int mid = (rr * 30 + gg * 59 + bb * 11) / 100;

                    int r1 = ((rr + mid * 1) / 2) * 230 / 255 + 10;
                    int g1 = ((gg + mid * 1) / 2) * 230 / 255 + 10;
                    int b1 = ((bb + mid * 1) / 2) * 230 / 255 + 10;
                    colors[pp++] = r1 << 16 | g1 << 8 | b1;
                }

            }
        }
    }

    public void wrap(Bitmap src, boolean isGrey, Weather weather) {
        for (int y = 0; y < src.getHeight(); y++) {
            for (int x = 0; x < src.getWidth(); x++) {
                int cc = src.getPixels()[x + y * src.getWidth()];
                if (cc < 255 && cc > -1) {
                    int val = colors[cc];

                    if (weather.isThunder()) val = 0xFFFFFF;
                    else if (isGrey) {
                        int red = ((val >> 16) & 0xff);
                        int green = ((val >> 8) & 0xff);
                        int blue = (val & 0xff);
                        blue = (int) (0.21 * red + 0.71 * green + 0.07 * blue) & 0xff;
                        red = (int) (0.21 * red + 0.71 * green + 0.07 * blue) & 0xff;
                        green = (int) (0.21 * red + 0.71 * green + 0.07 * blue) & 0xff;
                        val = (red << 16) | (green << 8) | blue;
                    }


                    src.getPixels()[x + y * src.getWidth()] = val;
                }
            }
        }
    }

    public static int getColor(int a, int b, int c, int d) { return (map(d) << 24) + (map(c) << 16) + (map(b) << 8) + (map(a)); }

    public static int map(int d) {
        if (d < 0) return 255;
        int r = d / 100 % 10;
        int g = d / 10 % 10;
        int b = d % 10;
        return r * 36 + g * 6 + b;
    }


}