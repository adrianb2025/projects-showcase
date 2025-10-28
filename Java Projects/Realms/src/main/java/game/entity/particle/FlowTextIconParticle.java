package game.entity.particle;

import game.gfx.Screen;

public class FlowTextIconParticle extends FlowTextParticle {

    private final int xSprite;
    private final int ySprite;
    private final int iconCol;
    public FlowTextIconParticle(String text, int x, int y, int textCol, int xSprite, int ySprite, int iconCol) {
        super(text, x, y, textCol);
        this.xSprite = xSprite;
        this.ySprite = ySprite;
        this.iconCol = iconCol;
    }

    public void render(Screen screen) {
        super.render(screen);
        screen.render(x + text.length() * 6, y, xSprite * 8, ySprite * 8, iconCol, 0);
    }

}