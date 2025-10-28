package game.level.tile;

import game.entity.Entity;
import game.level.Level;

import java.awt.*;

public class SolidGrassTile extends GrassTile {

    public SolidGrassTile(int id) { super(id); }

    public boolean blocks(Entity e) { return true; }

}