package game.gfx.gui;

import game.Game;
import game.gfx.Font;
import game.gfx.Screen;

import java.security.MessageDigest;
import java.util.LinkedList;
import java.util.List;

public class GuiTextPanel extends GuiPanel {

    protected int textCol;
    protected String originalText;

    List<String> formattedText = new LinkedList<String>();

    public GuiTextPanel(String text, int posX, int posY, int textCol, int panelCol) {
        super(posX, posY, panelCol);
        init(text, textCol);
    }

    public GuiTextPanel(String text, int posX, int posY, int textCol) {
        super(posX, posY);
        init(text, textCol);
    }

    public GuiTextPanel(String text, int posX, int posY) {
        super(posX, posY);
        init(text, GuiManager.FONT_COL);
    }

    public GuiTextPanel(List<String> texts, int x, int y, int w) {
        super(x, y);
        textCol = GuiManager.FONT_COL;
        setFormattedText(texts, w);
    }

    private void init(String text, int textCol) {
        originalText = text;
        this.textCol = textCol;
        changed = true;
        visible = true;
        formatString(text);
    }

    protected void formatString(String text) {
        if (formattedText.size() > 0) formattedText.clear();
        if (text.isEmpty()) text = "TEXT";

        final int maxLen = (Game.WIDTH / 8) / 2 + 5;
        String temp;
        int strWidth = 0;
        while (text.length() > maxLen) {
            int i = text.substring(0, maxLen).lastIndexOf(" ");
            if (i != -1 && i != maxLen) {
                temp = text.substring(0, i);
                text = text.substring(i + 1);
            } else {
                temp = text.substring(0, maxLen);
                text = text.substring(maxLen);
            }

            strWidth = Math.max(strWidth, temp.length());
            formattedText.add(temp);
        }

        if (text.length() <= maxLen) formattedText.add(text);


        sizeX = (formattedText.size() == 1) ? text.length() + 2 : maxLen + 2;
        sizeY = formattedText.size() + 2;
    }

    public void setText(String text) {
        formatString(text);
        changed = true;
    }

    public void setFormattedText(List<String> formattedText, int w) {
        this.formattedText = formattedText;
        sizeX = w + 2;
        sizeY = formattedText.size() + 2;
        changed =true;
        visible = true;
    }

    protected void paintF(Screen screen) {
        super.paintF(screen);

        int h = 1;
        for (String text: formattedText) {
            Font.drawToBitmap(text, screen, 8, h * 8, textCol, image);
            h++;
        }

        changed = false;
    }

}