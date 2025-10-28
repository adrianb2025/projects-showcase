package game.entity.hopper;

import game.entity.hopper.task.CreateBubbleTask;

public class GrayHopper extends Hopper {

    public GrayHopper(double x, double y) {
        super(x, y);
        sprite = 0;
        skillDelay = 120;
        skillTime = 120;
    }

    public boolean useSkill() {
        if (onGround) {
            setTask(new CreateBubbleTask(this));
            return true;
        }

        return false;
    }
}