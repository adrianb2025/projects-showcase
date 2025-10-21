package game.entity.item.resource;

import game.entity.mob.Player;
import game.gfx.util.PaletteHelper;
import game.level.Level;
import game.level.tile.Tile;

import java.util.HashMap;
import java.util.Map;

public class Resource {

    public static final Map<String, Resource> resources = new HashMap<String, Resource>();
    public static final Resource health = new Resource("Health", 1, 4, PaletteHelper.getColor(-1, 0, 500, 555));
    public static final Resource apple = new Resource("Apple", 0, 3, PaletteHelper.getColor(-1, 0, 510, 555));
    public static final Resource coin = new Resource("Coin", 0, 3, PaletteHelper.getColor(-1, 0, 552, 555));
    public static final Resource bigCoin = new Resource("B.Coin", 1, 3, PaletteHelper.getColor(-1, 0, 552, 555));
    public static final Resource berry = new Resource("Berry", 1, 3, PaletteHelper.getColor(-1, 0, 15, 555));

    private final String name;
    private final int xSprite;
    private final int ySprite;
    private final int col;

    public Resource(String name, int xSprite, int ySprite, int col) {
        if (name.length() > 6) throw new RuntimeException("Name can't be longer than six characters!");
        this.name = name;
        this.xSprite = xSprite;
        this.ySprite = ySprite;
        this.col = col;
        resources.put(name.toLowerCase(), this);
    }

    public static Resource getResourceByName(String name) { return resources.get(name); }
    public boolean interactOn(Tile tile, Level level, int xt, int yt, Player player, int attackDir) { return false; }
    public int getColor() { return col; }
    public String getName() { return name; }
    public int getXSprite() { return xSprite; }
    public int getYSprite() { return ySprite; }

}