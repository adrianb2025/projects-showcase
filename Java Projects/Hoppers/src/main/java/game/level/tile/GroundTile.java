package game.level.tile;

public class GroundTile extends Tile {

    protected GroundTile(int tile) { super(tile); }

    public boolean blocks() { return true; }

}