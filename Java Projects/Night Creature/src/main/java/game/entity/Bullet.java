package game.entity;

import game.entity.unit.Knight;
import game.entity.unit.Unit;
import game.level.Level;
import game.util.MathUtil;

import java.awt.*;

public class Bullet extends Entity {

    private double xo;
    private double yo;
    private int lifeTime;
    private Unit owner;

    public Bullet(Unit owner, double x, double y, double xa, double ya) {
        super(x, y, 0);
        this.owner = owner;
        xo = x;
        yo = y;
        this.xa = xa;
        this.ya = ya;
        lifeTime = 60;
    }

    public void tick() {
        if (removed) return;

        if (lifeTime-- <= 0) {
            die();
            return;
        }

        int xStep = (int)(Math.abs(xa * 100) + 1);
        for (int i = 0; i < xStep; i++) {
            if (isFree(x + xa * i / xStep, y)) continue;
            return;
        }

        xo = x;
        x += xa;

        int yStep = (int)(Math.abs(ya * 100) + 1);
        for (int i = 0; i < yStep; i++) {
            if (isFree(x, y + ya * i / yStep)) continue;
            return;
        }

        yo = y;
        y += ya;
    }

    private boolean isFree(double xn, double yn) {
        boolean result = true;

        Knight target = (Knight) level.getNearbyEntity(xn, yn, 5, Knight.class, new Level.EntityFilter<Knight>() {
            public boolean accept(Entity e) {
                return e != owner;
            }
        });

        if (target != null) {
            target.hurt(owner);
            die();
            result = false;
        }

        return result;
    }

    public void render(Graphics2D g) {
        int c0 = 0xFF0004;
        int c1 = 0xFFFF00;
        double xd = x - xo;
        double yd = y - yo;
        int steps = (int)(Math.sqrt(xd * xd + yd * yd) + 1);
        for (int i = 0; i < steps; i++) {
            if (random.nextInt(steps) < i) continue;
            double p = i / (double) steps;

            int c = MathUtil.lerpRGB(c0, c1, p);

            int xx = (int)(xo + p * xd);
            int yy = (int)(yo + p * yd);

            g.setColor(new Color(c));
            g.fillRect(xx, yy, 1, 1);
        }
    }

}