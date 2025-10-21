package game.entity.particle;

import game.gfx.Font;
import game.gfx.Screen;
import game.gfx.util.PaletteHelper;

public class TextParticle extends SmashParticle {

    protected String text;

    public TextParticle(String text, int x, int y, int col) {
        super(x, y, col);
        this.text = text;
    }

    public void render(Screen screen) {
        Font.draw(text, screen, x - text.length() * 4 + 1, y - (int) zz + 1, PaletteHelper.getColor(-1, 0, 0, 0));
        Font.draw(text, screen, x - text.length() * 4, y - (int) zz, col);
    }

}