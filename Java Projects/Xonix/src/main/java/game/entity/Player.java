package game.entity;

import java.awt.*;
import static game.utils.Utils.*;

public class Player extends Entity {

    public Player(int x, int y) {
        super(x, y);
        color = Color.cyan;
    }

    public void tick() {
        int xo = xt;
        int yo = yt;

        if (attemptMove(xa, 0)) {
            xt += xa;
        } else {
            xa = 0;
        }

        if (attemptMove(0, ya)) {
            yt += ya;
        } else {
            ya = 0;
        }

        if (xt != xo || yt != yo) {
            level.steppedOn(this, xt, yt, xo, yo);
        }
    }

    public void move(int xa, int ya) {
        if (xa != 0 && ya != 0) {
            throw new IllegalStateException("Failed to move");
        }

        this.xa += xa;
        this.ya += ya;

        this.xa = clamp(-1, this.xa, 1);
        this.ya = clamp(-1, this.ya, 1);

        if (xa != 0) this.ya = 0;
        if (ya != 0) this.xa = 0;
    }

    public boolean attemptMove(int xxa, int yya) {
        int xn = xt + xxa;
        int yn = yt + yya;
        if (xn < 0 || yn < 0 || xn >= level.w || yn >= level.h) return false;
        return true;
    }

}