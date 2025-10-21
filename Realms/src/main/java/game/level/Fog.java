package game.level;

import game.gfx.Screen;
import game.gfx.util.PaletteHelper;

import java.nio.file.attribute.UserPrincipal;

public class Fog {

    private boolean[] fog;
    private int width;
    private int height;
    private CircleMap circleMap;

    public Fog(int width, int height, boolean defaultValue) {
        this.width = width;
        this.height = height;
        fog = new boolean[width * height];
        for (int i = 0; i < width * height; i++) {
            fog[i] = defaultValue;
        }
    }

    public boolean getFog(int x, int y) {
        return x < 0 || y < 0 || x >= this.width || y >= this.height || fog[x + y * this.width];
    }

    private CircleMap getCircleMap(int radius) {
        if (this.circleMap != null) {
            if (this.circleMap.getRadius() == radius) {
                return this.circleMap;
            }
        }

        this.circleMap = new CircleMap(radius);
        this.circleMap.drawCircle();
        this.circleMap.fillCircle();
        return this.circleMap;
    }

    void clearFog(int xCenter, int yCenter, int radius) {
        int w = radius << 1;
        int h = radius << 1;
        int xOffs = Math.max(0, xCenter - radius);
        int yOffs = Math.max(0, yCenter - radius);

        for (int j = 0; j < h; j++) {
            for (int i = 0; i < w; i++) {
                try {
                    if (i + xOffs >= this.width || j + yOffs >= this.height) continue;
                    fog[i + xOffs + (j + yOffs) * this.width] = !getCircleMap(radius).getCircle()[i + j * w] && fog[i + xOffs + (j + yOffs) * this.width];

                } catch (Exception e) {
                    //ignore
                }
            }
        }
    }


    /*public void clearFog1(int xCenter, int yCenter, int radius) {
        int x0 = Math.max(xCenter - radius, 0);
        int y0 = Math.max(yCenter - radius, 0);
        int x1 = Math.min(xCenter + radius, this.width - 1);
        int y1 = Math.min(yCenter + radius, this.height - 1);
        for (int j = y0; j < y1; j++) {
            for (int i = x0; i < x1; i++) {
                fog[i + j * this.width] = false;
            }
        }
    }

    public void clearFog(int x, int y) {

        if (x >= 0 && y >= 0 && x < this.width && y < this.height) {
            fog[x + y * this.width] = false;
        }
    }*/

    public void render(Screen screen, int x, int y) {

        if (getFog(x, y)) {
            boolean u = !getFog(x, y - 1);
            boolean d = !getFog(x, y + 1);
            boolean l = !getFog(x - 1, y);
            boolean r = !getFog(x + 1, y);
            boolean ul = !getFog(x - 1, y - 1);
            boolean dl = !getFog(x - 1, y + 1);
            boolean ur = !getFog(x + 1, y - 1);
            boolean dr = !getFog(x + 1, y + 1);

            int col = PaletteHelper.getColor(0, 0, 0, -1);
            int fogFill = PaletteHelper.getColor(0, 0, 0, 0);

            x <<= 4;
            y <<= 4;

            if (!u && !l) {
                if (!ul)
                    screen.render(x, y, 0, 0, 8, 8, fogFill, 0);
                else
                    screen.render(x, y, 5 * 8, 0 * 8, 8, 8, col, 3);
            } else
                screen.render(x, y, (l ? 9 : 8) * 8, (u ? 2 : 1) * 8, 8, 8, col, 3);

            if (!u && !r) {
                if (!ur)
                    screen.render(x + 8, y, 0, 0, 8, 8, fogFill, 0);
                else
                    screen.render(x + 8, y, 6 * 8, 0 * 8, 8, 8, col, 3);
            } else
                screen.render(x + 8, y, (r ? 7 : 8) * 8, (u ? 2 : 1) * 8, 8, 8, col, 3);


            if (!d && !l) {
                if (!dl)
                    screen.render(x, y + 8, 0, 0, 8, 8, fogFill, 0);
                else
                    screen.render(x, y + 8, 5 * 8, 1 * 8, 8, 8, col, 3);
            } else
                screen.render(x, y + 8, (l ? 9 : 8) * 8, (d ? 0 : 1) * 8, 8, 8, col, 3);
            if (!d && !r) {
                if (!dr)
                    screen.render(x + 8, y + 8, 0, 0, 8, 8, fogFill, 0);
                else
                    screen.render(x + 8, y + 8, 6 * 8, 1 * 8, 8, 8, col, 3);
            } else
                screen.render(x + 8, y + 8, (r ? 7 : 8) * 8, (d ? 0 : 1) * 8, 8, 8, col, 3);

        }
    }


}
