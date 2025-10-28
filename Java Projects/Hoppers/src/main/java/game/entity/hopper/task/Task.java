package game.entity.hopper.task;

import game.entity.hopper.Hopper;

import java.util.Random;

public abstract class Task {

    protected static final Random random = new Random();
    protected Hopper owner;

    public Task(Hopper owner) { this.owner = owner; }

    public abstract void tick();
    public abstract boolean finished();

    public void destroy() {}

}