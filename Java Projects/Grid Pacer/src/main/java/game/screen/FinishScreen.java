package game.screen;

import game.GridPacer;
import game.InputHandler;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class FinishScreen extends Screen {

    private Font font = new Font(null, Font.BOLD, 25);
    private Font smallFont = new Font(null, Font.PLAIN, 10);
    private Screen screen;
    private BufferedImage fadeImage;
    private int[] fadePixels;
    private int tickTime;
    private double fadeTime = 1.5;
    private final String text;
    private double br = 1.0;
    private final MouseEventHandler eventHandler;

    public FinishScreen(Screen screen, String text, MouseEventHandler eventHandler) {
        this.screen = screen;
        this.text = text;
        this.eventHandler = eventHandler;
        fadeImage = new BufferedImage(GridPacer.WIDTH, GridPacer.HEIGHT, BufferedImage.TYPE_INT_ARGB);
        fadePixels = ((DataBufferInt)fadeImage.getRaster().getDataBuffer()).getData();
    }


    public void tick(InputHandler input) {
        tickTime++;
        if (tickTime < 60.0 * fadeTime) br = Math.max(0.1, 1.0 - tickTime / (60.0 * fadeTime));
        else if(input.leftClicked || input.rightClicked || input.start.clicked) eventHandler.onClick(this);
    }

    public void render(Graphics2D g) {
        Graphics2D fadeGraphics = fadeImage.createGraphics();
        screen.render(fadeGraphics);
        fadeGraphics.dispose();

        for (int i = 0; i < fadePixels.length; i++) {
            int c = fadePixels[i];
            int aa = (c >> 24) & 0xFF;
            int rr = (c >> 16) & 0xFF;
            int gg = (c >> 8) & 0xFF;
            int bb = c & 0xFF;

            rr = (int)(rr * br);
            gg = (int)(gg * br);
            bb = (int)(bb * br);

            fadePixels[i] = (aa << 24) | (rr << 16) | (gg << 8) | bb;
        }

        g.drawImage(fadeImage, 0, 0, null);
        if (tickTime > 60.0 * fadeTime) {
            String s = text;
            g.setColor(Color.WHITE);
            g.setFont(font);
            g.drawString(s, (int)(GridPacer.WIDTH - s.length() * 12.5) >> 1, 60);
            s = "Click to continue";
            g.setFont(smallFont);
            g.drawString(s, (int)(GridPacer.WIDTH - s.length() * 4.5) >> 1, GridPacer.HEIGHT - 45 - (int)Math.abs(Math.sin(tickTime * 0.1) * 5));
        }
    }

}