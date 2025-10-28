package game.entity.mob.task;

import game.entity.Entity;

import java.awt.*;
import java.util.Random;

public abstract class Task {

    public static final Random random = new Random();
    protected Entity owner;

    public void init(Entity owner) { this.owner = owner; }

    public abstract void tick();
    public abstract boolean finished();

}