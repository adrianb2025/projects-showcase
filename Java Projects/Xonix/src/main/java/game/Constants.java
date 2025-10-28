package game;

import java.awt.*;

public class Constants {
    public static final int CELL_SIZE = 5;
    public static final int ENTITY_SIZE = CELL_SIZE - 2;

    public static final int NOTHING = 0;
    public static final int PERMANENT = 1;
    public static final int PATH = 2;
    public static final int GROUND = 3;

    public static final Color[] TILE_COLS = {
            Color.BLUE,
            Color.DARK_GRAY,
            Color.ORANGE,
            Color.DARK_GRAY
    };

    public static final int[] TILE_HEIGHTS = {
            0,
            1,
            1,
            1
    };

}