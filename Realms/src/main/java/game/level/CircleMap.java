package game.level;

public class CircleMap {

    private boolean[] circle;
    private int radius;
    private int w;
    private int h;

    public CircleMap(int radius) {
        this.radius = radius;
        w = (radius) << 1;
        h = (radius) << 1;
        circle = new boolean[w * h];
        for (int i = 0; i < w * h; i++) {
            circle[i] = false;
        }
    }

    private void fillMap(int x, int y) {
        if (x >= 0 && y >= 0 && x < w && y < h) {
            circle[x + y * w] = true;
        }
    }
    
    private void circlePoints(int cx, int cy, int x, int y) {
        if (0 == x) {
            fillMap(cx, cy + y);
            fillMap(cx, cy - y);
            fillMap(cx + y, cy);
            fillMap(cx - y, cy);
        } else if (x == y) {
            fillMap(cx + x, cy + y);
            fillMap(cx - x, cy + y);
            fillMap(cx + x, cy - y);
            fillMap(cx - x, cy - y);
        } else if (x < y) {
            fillMap(cx + x, cy + y);
            fillMap(cx - x, cy + y);
            fillMap(cx + x, cy - y);
            fillMap(cx - x, cy - y);
            fillMap(cx + y, cy + x);
            fillMap(cx - y, cy + x);
            fillMap(cx + y, cy - x);
            fillMap(cx - y, cy - x);
        }
    }

    public void drawCircle() {
        int x = 0;
        int y = radius - 1;
        int p = (5 - radius * 4) / 4;

        circlePoints(w >> 1, h >> 1, x, y);

        while (x < y) {
            p = p < 0 ? (p += 2 * ++x + 1) : (p += 2 * (++x - --y) + 1);
            circlePoints(w >> 1, h >> 1, x, y);
        }
    }

    private int findRightRow(int col) {
        for (int row = w; row > w >> 1; row--) {
            boolean currentCol = circle[row + col * w];
            boolean nextCol = circle[(row - 1) + col * w];
            if (currentCol && !nextCol) return row;
        }

        return -1;
    }

    private int findLeftRow(int col) {
        for (int row = 0; row < w >> 1; row++) {
            boolean currentCol = circle[row + col * w];
            boolean nextCol = circle[(row + 1) + col * w];
            if (currentCol && !nextCol) return row;
        }

        return -1;
    }

    public void fillCircle() {
        for (int col = 0; col < h; col++) {
            int leftRow = findLeftRow(col);
            if (leftRow < 0) continue;
            int rightRow = findRightRow(col);
            if (rightRow < 0) continue;

            if (leftRow != rightRow) {
                for (int row = leftRow; row < rightRow; row++) {
                    fillMap(row, col);
                }
            }
        }
    }

    public boolean[] getCircle() { return circle; }
    public int getRadius() { return radius; }

}