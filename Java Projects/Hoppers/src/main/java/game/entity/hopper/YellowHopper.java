package game.entity.hopper;

import game.InputHandler;

public class YellowHopper extends Hopper {

    public YellowHopper(double x, double y) {
        super(x, y);
        sprite = 1;
        skillDelay = 0;
        skillTime = 0;
    }

    public void tick(InputHandler input) {
        super.tick(input);
        acceleration *= 0.8;
    }

    public boolean useSkill() {
        acceleration = 2;
        return true;
    }

}