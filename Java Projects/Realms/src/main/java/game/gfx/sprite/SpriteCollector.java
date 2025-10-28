package game.gfx.sprite;

import game.gfx.Bitmap;
import game.gfx.util.BitmapHelper;

import java.util.*;

public class SpriteCollector {

    private List<SpriteWrapper> wrappers = new ArrayList<SpriteWrapper>();
    private Map<String, Bitmap> sprites = new HashMap<String, Bitmap>();
    private int w = 0;
    private int h = 0;
    private Bitmap src;

    public SpriteCollector(Bitmap src) { this.src = src; }

    public void resetWrappers() { wrappers.clear(); }

    public void addWrapper(SpriteWrapper spriteWrapper) {
        w = Math.max(spriteWrapper.getWidth(), w);
        h = Math.max(spriteWrapper.getHeight(), h);
        wrappers.add(spriteWrapper);
    }

    public Bitmap mergeWrappers(String name, int scale, int bits, int auraColor) {
        name += "_" + scale + "_" + bits + "_" + (auraColor >> 24);

        Bitmap result = sprites.get(name);
        if (result != null) return result;

        if (wrappers.size() == 0) return null;

        result = new Bitmap(w * scale, h * scale);
        BitmapHelper.fill(result, 0xFF00FF);


        for (SpriteWrapper spriteWrapper : wrappers) {
            BitmapHelper.scaleDraw(src, scale, 0, 0, spriteWrapper.getXo(), spriteWrapper.getYo(), spriteWrapper.getWidth(), spriteWrapper.getHeight(), spriteWrapper.getColors(), bits, result);
        }

        if ((auraColor >> 24) == 1) BitmapHelper.drawAura(result, 0xFF00FF, auraColor & 0xFFFFFF);

        sprites.put(name, result);

        return result;
    }

}