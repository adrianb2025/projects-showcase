package game.entity.mob.hostile.boss;

import game.entity.Team;
import game.entity.weapon.Spark;
import game.gfx.Screen;
import game.gfx.util.PaletteHelper;

public class AirMage extends Boss {

    private int xa;
    private int ya;
    private int randomWalkTime;
    private int attackDelay;
    private int attackTime;
    private int attackType;

    public AirMage() {
        team = Team.HOSTILE;
        health = 200;
    }

    public AirMage(int x, int y) {
        this.x = x;
        this.y = y;
        team = Team.HOSTILE;
        health = 200;
    }

    public void tick() {
        super.tick();

        if (attackDelay > 0) {
            dir = (attackDelay - 45) / 4 % 4;
            dir = (dir * 2 % 4) + (dir / 2);
            if (attackDelay < 45) dir = 0;
            if (--attackDelay == 0) {
                attackType = 0;
                if (health < maxHealth / 2) attackType = 1;
                if (health < maxHealth / 10) attackType = 2;
                attackTime = 120;
            }

            return;
        }

        if (attackTime > 0) {
            attackTime--;
            double dir = attackTime * 0.25 * (attackTime % 2 * 2 - 1);
            double speed = 0.7 + attackType * 0.2;
            level.add(new Spark(this, Math.cos(dir) * speed, Math.sin(dir) * speed));
            return;
        }

        if (level.getPlayer() != null && randomWalkTime == 0) {
            int xd = level.getPlayer().getX() - x;
            int yd = level.getPlayer().getY() - y;
            if (xd * xd + yd * yd < 1024) {
                xa = 0;
                ya = 0;
                if (xd < 0) xa = +1;
                if (xd > 0) xa = -1;
                if (yd < 0) ya = +1;
                if (yd > 0) ya = -1;
            } else if (xd * xd + yd * yd > 6400) {
                xa = 0;
                ya = 0;
                if (xd < 0) xa = -1;
                if (xd > 0) xa = +1;
                if (yd < 0) ya = -1;
                if (yd > 0) ya = +1;
            }
        }

        int speed = (tickTime % 4) == 0 ? 0 : 1;
        if (!move(xa * speed, ya * speed) || random.nextInt(100) == 0) {
            randomWalkTime = 30;
            xa = (random.nextInt(3) - 1);
            ya = (random.nextInt(3) - 1);
        }

        if (randomWalkTime > 0) {
            randomWalkTime--;
            if (level.getPlayer() != null && randomWalkTime == 0) {
                int xd = level.getPlayer().getX() - x;
                int yd = level.getPlayer().getY() - y;
                if (random.nextInt(4) == 0 && xd * xd + yd * yd < 2500) {
                    if (attackDelay == 0 && attackTime == 0) attackDelay = 120;
                }
            }
        }
    }

    public void render(Screen screen) {
        int xt = 0;
        int yt = 14;

        int flip1 = (walkDist >> 3) & 1;
        int flip2 = (walkDist >> 3) & 1;

        if (dir == 1) xt += 2;

        if (dir > 1) {
            flip1 = 0;
            flip2 = ((walkDist >> 4) & 1);

            if (dir == 2) flip1 = 1;

            xt += 4 + ((walkDist >> 3) & 1) * 2;
        }

        int xo = x - 8;
        int yo = y - 11;

        if (isSwimming()) {
            yo += 4;
            int waterCol = PaletteHelper.getColor(-1, -1, -1, 444);
            if (tickTime / 8 % 2 == 0) waterCol = PaletteHelper.getColor(-1, 444, -1, -1);
            screen.render(xo + 0, yo + 3, 5 * 8, 13 * 8, waterCol, 0);
            screen.render(xo + 8, yo + 3, 5 * 8, 13 * 8, waterCol, 1);
        }

        int col1 = PaletteHelper.getColor(-1, 100, 550 - (level.getNumber() % 5) * 100, 555);
        int col2 = PaletteHelper.getColor(-1, 100, 550 - (level.getNumber() % 5) * 100, 532);

        if (health < maxHealth / 10) {
            if (tickTime / 3 % 2 == 0) {
                col1 = PaletteHelper.getColor(-1, 500, 100, 555);
                col2 = PaletteHelper.getColor(-1, 500, 100, 532);
            }
        } else if (health < maxHealth / 2) {
            if (tickTime / 5 % 4 == 0) {
                col1 = PaletteHelper.getColor(-1, 500, 100, 555);
                col2 = PaletteHelper.getColor(-1, 500, 100, 532);
            }
        }


        if (hurtTime > 0) {
            col1 = PaletteHelper.getColor(-1, 555, 555, 555);
            col2 = PaletteHelper.getColor(-1, 555, 555, 555);
        }

        screen.render(xo + 8 * flip1, yo + 0, xt * 8, yt * 8, col1, flip1);
        screen.render(xo + 8 - 8 * flip1, yo + 0, (xt + 1) * 8, yt * 8, col1, flip1);

        if (!isSwimming()) {
            screen.render(xo + 8 * flip2, yo + 8, xt * 8, (yt + 1) * 8, col2, flip2);
            screen.render(xo + 8 - 8 * flip2, yo + 8, (xt + 1) * 8, (yt + 1) * 8, col2, flip2);
        }

    }

    protected void doHurt(int dmg, int attackDir) {
        super.doHurt(dmg, attackDir);
        if (attackDelay == 0 && attackTime == 0) attackDelay = 120;
    }

}