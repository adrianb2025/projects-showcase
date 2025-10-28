package game.entity.tree;

import game.gfx.sprite.SpriteCollector;
import game.gfx.sprite.SpriteWrapper;
import game.gfx.util.PaletteHelper;

public class PineTree extends Tree {

    public PineTree(int x, int y, SpriteCollector collector) {
        super(x, y, 8, 8);

        collector.resetWrappers();
        collector.addWrapper(new SpriteWrapper(17 * 8,  16 * 8, 4 * 8, 8 * 8, PaletteHelper.getColor(30, 20, 40, -1)));
        collector.addWrapper(new SpriteWrapper(21 * 8,  16 * 8, 4 * 8, 8 * 8, PaletteHelper.getColor(10, 20, 10, -1)));
        collector.addWrapper(new SpriteWrapper(25 * 8,  16 * 8, 4 * 8, 8 * 8, PaletteHelper.getColor(100, 210, 320, -1)));

        sprite = collector.mergeWrappers("tree_pine", 1, random.nextInt(2), 0x01000000);
    }

}