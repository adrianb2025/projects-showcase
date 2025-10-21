package game.gfx;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;

public class Bitmap {

    public final int w;
    public final int h;
    public int[] pixels;
    public boolean xFlip = false;
    public int xOffs;
    public int yOffs;

    public Bitmap(int w, int h) {
        this.w = w;
        this.h = h;
        this.pixels = new int[w * h];
    }

    public Bitmap(int w, int h, int[] pixels) {
        this.w = w;
        this.h = h;
        this.pixels = pixels;
    }

    public Bitmap(BufferedImage image) {
        this.w = image.getWidth();
        this.h = image.getHeight();
        this.pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
    }

    public void setPixel(int xp, int yp, int color) {
        if ((xp += this.xOffs) >= 0 && (yp += this.yOffs) >= 0 && xp < this.w && yp < this.h) {
            this.pixels[xp + yp * this.w] = color;
        }
    }

    public void draw(Bitmap b, int xp, int yp) {
        int x0 = xp += this.xOffs;
        int y0 = yp += this.yOffs;
        int x1 = xp + b.w;
        int y1 = yp + b.h;
        if (x0 < 0) {
            x0 = 0;
        }
        if (y0 < 0) {
            y0 = 0;
        }
        if (x1 > this.w) {
            x1 = this.w;
        }
        if (y1 > this.h) {
            y1 = this.h;
        }
        if (this.xFlip) {
            for (int y = y0; y < y1; ++y) {
                int sp = (y - yp) * b.w + xp + b.w - 1;
                int dp = y * this.w;
                for (int x = x0; x < x1; ++x) {
                    int c = b.pixels[sp - x];
                    if (c >= 0) continue;
                    this.pixels[dp + x] = b.pixels[sp - x];
                }
            }
        } else {
            for (int y = y0; y < y1; ++y) {
                int sp = (y - yp) * b.w - xp;
                int dp = y * this.w;
                for (int x = x0; x < x1; ++x) {
                    int c = b.pixels[sp + x];
                    if (c >= 0) continue;
                    this.pixels[dp + x] = b.pixels[sp + x];
                }
            }
        }
    }

    public void fill(int x0, int y0, int x1, int y1, int color) {
        y0 += this.yOffs;
        x1 += this.xOffs;
        y1 += this.yOffs;
        if ((x0 += this.xOffs) < 0) {
            x0 = 0;
        }
        if (y0 < 0) {
            y0 = 0;
        }
        if (x1 >= this.w) {
            x1 = this.w - 1;
        }
        if (y1 >= this.h) {
            y1 = this.h - 1;
        }
        for (int y = y0; y <= y1; ++y) {
            for (int x = x0; x <= x1; ++x) {
                this.pixels[x + y * this.w] = color;
            }
        }
    }

    public void clear(int color) {
        Arrays.fill(this.pixels, color);
    }

    public void shade(Bitmap shadows) {
        for (int i = 0; i < this.pixels.length; ++i) {
            if (shadows.pixels[i] <= 0) continue;
            int r = (this.pixels[i] & 0xFF0000) * 200 >> 8 & 0xFF0000;
            int g = (this.pixels[i] & 0xFF00) * 200 >> 8 & 0xFF00;
            int b = (this.pixels[i] & 0xFF) * 200 >> 8 & 0xFF;
            this.pixels[i] = 0xFF000000 | r | g | b;
        }
    }

    public void box(int x0, int y0, int x1, int y1, int color) {
        int xx0 = x0 += this.xOffs;
        int yy0 = y0 += this.yOffs;
        int xx1 = x1 += this.xOffs;
        int yy1 = y1 += this.yOffs;
        if (x0 < 0) {
            x0 = 0;
        }
        if (y0 < 0) {
            y0 = 0;
        }
        if (x1 >= this.w) {
            x1 = this.w - 1;
        }
        if (y1 >= this.h) {
            y1 = this.h - 1;
        }
        for (int y = y0; y <= y1; ++y) {
            for (int x = x0; x <= x1; ++x) {
                if (x == xx0 || y == yy0 || x == xx1 || y == yy1) {
                    this.pixels[x + y * this.w] = color;
                }
                if (y <= yy0 || y >= yy1 || x >= xx1 - 1) continue;
                x = xx1 - 1;
            }
        }
    }

    public void blendDraw(Bitmap b, int xp, int yp, int col) {
        int x0 = xp += this.xOffs;
        int x1 = xp + b.w;
        int y0 = yp += this.yOffs;
        int y1 = yp + b.h;
        if (x0 < 0) {
            x0 = 0;
        }
        if (y0 < 0) {
            y0 = 0;
        }
        if (x1 > this.w) {
            x1 = this.w;
        }
        if (y1 > this.h) {
            y1 = this.h;
        }
        if (this.xFlip) {
            for (int y = y0; y < y1; ++y) {
                int sp = (y - yp) * b.w + xp + b.w - 1;
                int dp = y * this.w;
                for (int x = x0; x < x1; ++x) {
                    int c = b.pixels[sp - x];
                    if (c >= 0) continue;
                    this.pixels[dp + x] = (b.pixels[sp - x] & 0xFEFEFEFE) + (col & 0xFEFEFEFE) >> 1;
                }
            }
        } else {
            for (int y = y0; y < y1; ++y) {
                int sp = (y - yp) * b.w - xp;
                int dp = y * this.w;
                for (int x = x0; x < x1; ++x) {
                    int c = b.pixels[sp + x];
                    if (c >= 0) continue;
                    this.pixels[dp + x] = (b.pixels[sp + x] & 0xFEFEFEFE) + (col & 0xFEFEFEFE) >> 1;
                }
            }
        }
    }

    public void blendDraw(int xp, int yp, int ww, int hh, int c, int col) {
        int x0 = xp += this.xOffs;
        int x1 = xp + ww;
        int y0 = yp += this.yOffs;
        int y1 = yp + hh;
        if (x0 < 0) {
            x0 = 0;
        }
        if (y0 < 0) {
            y0 = 0;
        }
        if (x1 > this.w) {
            x1 = this.w;
        }
        if (y1 > this.h) {
            y1 = this.h;
        }
        if (this.xFlip) {
            for (int y = y0; y < y1; ++y) {
                int dp = y * this.w;
                for (int x = x0; x < x1; ++x) {
                    if (c >= 0) continue;
                    this.pixels[dp + x] = (c & 0xFEFEFEFE) + (col & 0xFEFEFEFE) >> 1;
                }
            }
        } else {
            for (int y = y0; y < y1; ++y) {
                int dp = y * this.w;
                for (int x = x0; x < x1; ++x) {
                    if (c >= 0) continue;
                    this.pixels[dp + x] = (c & 0xFEFEFEFE) + (col & 0xFEFEFEFE) >> 1;
                }
            }
        }
    }

    public void blend(Bitmap b, int xp, int yp) {
        int x0 = xp += this.xOffs;
        int x1 = xp + b.w;
        int y0 = yp += this.yOffs;
        int y1 = yp + b.h;
        if (x0 < 0) {
            x0 = 0;
        }
        if (y0 < 0) {
            y0 = 0;
        }
        if (x1 > this.w) {
            x1 = this.w;
        }
        if (y1 > this.h) {
            y1 = this.h;
        }
        for (int y = y0; y < y1; ++y) {
            int sp = (y - yp) * b.w - xp;
            int dp = y * this.w;
            for (int x = x0; x < x1; ++x) {
                int ir;
                int c = b.pixels[sp + x];
                int a = c >> 24 & 0xFF;
                if (a == 0) continue;
                int ia = 255 - a;
                int rr = this.pixels[dp + x] >> 16 & 0xFF;
                int gg = this.pixels[dp + x] >> 8 & 0xFF;
                int bb = this.pixels[dp + x] & 0xFF;
                int ig = ir = ((x ^ y) & 1) * 10 + 10;
                int ib = ir;
                rr = (rr * ia + ir * a) / 255;
                gg = (gg * ia + ig * a) / 255;
                bb = (bb * ia + ib * a) / 255;
                this.pixels[dp + x] = 0xFF000000 | rr << 16 | gg << 8 | bb;
            }
        }
    }

    public void fogBlend(Bitmap b, int xp, int yp) {
        int x0 = xp += this.xOffs;
        int x1 = xp + b.w;
        int y0 = yp += this.yOffs;
        int y1 = yp + b.h;
        if (x0 < 0) {
            x0 = 0;
        }
        if (y0 < 0) {
            y0 = 0;
        }
        if (x1 > this.w) {
            x1 = this.w;
        }
        if (y1 > this.h) {
            y1 = this.h;
        }
        if (this.xFlip) {
            for (int y = y0; y < y1; ++y) {
                int sp = (y - yp) * b.w + xp + b.w - 1;
                int dp = y * this.w;
                for (int x = x0; x < x1; ++x) {
                    int c = b.pixels[sp - x];
                    if (c == 0) continue;
                    int ic = 255 - (c &= 0xFF);
                    int rr = this.pixels[dp + x] >> 16 & 0xFF;
                    int gg = this.pixels[dp + x] >> 8 & 0xFF;
                    int bb = this.pixels[dp + x] & 0xFF;
                    int gray = (rr * 30 + gg * 59 + bb * 11) / 255;
                    rr = (rr * c + gray * ic) / 255;
                    gg = (gg * c + gray * ic) / 255;
                    bb = (bb * c + gray * ic) / 255;
                    this.pixels[dp + x] = 0xFF000000 | rr << 16 | gg << 8 | bb;
                }
            }
        } else {
            for (int y = y0; y < y1; ++y) {
                int sp = (y - yp) * b.w - xp;
                int dp = y * this.w;
                for (int x = x0; x < x1; ++x) {
                    int c = b.pixels[sp + x];
                    if (c == 0) continue;
                    int ic = 255 - (c &= 0xFF);
                    int rr = this.pixels[dp + x] >> 16 & 0xFF;
                    int gg = this.pixels[dp + x] >> 8 & 0xFF;
                    int bb = this.pixels[dp + x] & 0xFF;
                    int gray = (rr * 30 + gg * 59 + bb * 11) / 255;
                    rr = (rr * c + gray * ic) / 255;
                    gg = (gg * c + gray * ic) / 255;
                    bb = (bb * c + gray * ic) / 255;
                    this.pixels[dp + x] = 0xFF000000 | rr << 16 | gg << 8 | bb;
                }
            }
        }
    }

}