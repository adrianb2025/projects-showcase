package game.entity.unit.task;

import game.entity.unit.Unit;

public abstract class Task {

    public static final Task idle = new IdleTask();
    protected Unit owner;

    public void init(Unit owner) { this.owner = owner; }

    public abstract void tick();
    public abstract boolean finished();

}