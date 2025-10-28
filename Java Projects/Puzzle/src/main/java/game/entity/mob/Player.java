package game.entity.mob;

import game.entity.Entity;
import game.gfx.Art;

public class Player extends Entity {

    public int score;
    public int prevScore;
    public int animTime;

    public Player() {
        xp = 5;
        yp = 10;
        score = 0;
        prevScore = 0;
        b = Art.faces[0][0];
    }

    public void setAnim(int anim) {
        if (animTime > 0) return;

        anim = Math.min(3, anim);

        if (anim != 0) animTime = 30;
        b = Art.faces[0][anim];
    }

    public void addScore(int newScore) {
        if (newScore > 35) setAnim(Math.max(1, newScore / 50));

        score += newScore;
        prevScore = newScore;
    }

    public void subScore(int subScore) { score = Math.max(0, score - subScore); }

}