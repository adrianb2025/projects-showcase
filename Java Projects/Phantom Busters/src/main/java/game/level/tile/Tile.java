package game.level.tile;

import game.gfx.Sprite;
import game.level.tile.tree.DeadTreeTile;
import game.level.tile.tree.GreatTreeTile;
import game.level.tile.tree.TreeTile;

public class Tile extends Sprite {

    public static final Tile[] tiles = new Tile[256];
    public static final Tile grass = new GrassTile(0);
    public static final Tile wall = new WallTile(1);
    public static final Tile tombstone = new TombstoneTile(2);
    public static final Tile tree = new TreeTile(3);
    public static final Tile deadTree = new DeadTreeTile(4);
    public static final Tile greatTree = new GreatTreeTile(5);
    public boolean isFloor = true;
    protected int id;

    public Tile(int id) {
        if (tiles[id] != null) throw new RuntimeException("Duplicate Tile IDs");
        this.id = id;
        tiles[id] = this;
    }

    public boolean blocks() { return false; }

}