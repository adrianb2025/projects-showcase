package game.entity.hopper.task;

import game.entity.Bubble;
import game.entity.hopper.Hopper;

public class CreateBubbleTask extends Task {

    private Bubble bubble;
    private int tickTime;

    public CreateBubbleTask(Hopper owner) {
        super(owner);
        bubble = new Bubble(owner.x + (owner.dir == 0 ? -1 : 1) * 10, owner.y);
        owner.level.addEntity(bubble);
    }

    public void tick() {
        tickTime++;
        bubble.blow();
        owner.scale += Math.sin(tickTime * 0.1) * 0.02;
    }

    public boolean finished() {
        boolean result = bubble.created();
        if (result) {
            bubble.ya -= 0.3;
            bubble.xa += (owner.dir == 0 ? -1 : 1) * 5;
            owner.scale = 1;
        }

        return result;
    }

}