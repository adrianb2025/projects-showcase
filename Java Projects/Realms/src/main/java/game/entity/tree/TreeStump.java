package game.entity.tree;

import game.gfx.sprite.SpriteCollector;
import game.gfx.sprite.SpriteWrapper;
import game.gfx.util.PaletteHelper;

public class TreeStump extends Tree {

    private boolean type = random.nextBoolean();

    public TreeStump(int x, int y, SpriteCollector collector) {
        super(x, y, 4, 1);

        collector.resetWrappers();
        collector.addWrapper(new SpriteWrapper(((type) ? 21 : 25) * 8,  8 << 1, 8 << 1, 8 << 1, PaletteHelper.getColor(100, 210, 320, -1)));
        collector.addWrapper(new SpriteWrapper(((type) ? 23 : 27) * 8,  8 << 1, 8 << 1, 8 << 1, PaletteHelper.getColor(100, 210, 320, -1)));

        sprite = collector.mergeWrappers("tree_stump_" + (type ? "0" : "1"), 1, random.nextInt(2), 0x01000000);
    }

}