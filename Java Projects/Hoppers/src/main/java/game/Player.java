package game;

import game.entity.hopper.Hopper;
import game.gfx.Art;
import game.level.Level;
import game.snd.Sound;

import java.util.ArrayList;
import java.util.List;

public class Player {

    public List<Hopper> hoppers = new ArrayList<Hopper>();
    public int index;
    private Level level;
    private Game game;
    private Hopper hopper;
    private boolean finish;

    public Player(Game game) {
        this.game = game;
        index = 0;
    }

    public void init(Level level) {
        this.level = level;
        hoppers = level.hoppers;
    }

    public Hopper getAlive() {
        int finished = 0;

        for (int i = 0; i < hoppers.size(); i++) {
            Hopper hopper = hoppers.get(i);
            if (hopper.finished) finished++;

            if (!hopper.removed) {
                index = i;
                return hopper;
            }
        }

        finish = finished == hoppers.size();
        return null;
    }

    public Hopper get(int index) {
        Hopper result = null;

        if (!hoppers.isEmpty()) {
            result = hoppers.get(index % hoppers.size());
            if (result.removed) result = getAlive();
        }

        if (result != null && hopper != null && hopper != result) {
            Sound.select.play();
            result.select();
        }

        return result;
    }

    public void tick(InputHandler input) {
        if (input.nextL.clicked) {
            index--;
            if (index < 0) index = hoppers.size() - 1;
        }

        if (input.nextR.clicked) {
            index++;
            if (index >= hoppers.size()) index = 0;
        }

        hopper = get(index);

        if (hopper != null) hopper.tick(input);
        else {
            if (finish) {
                if (level.num == Art.levels.length - 1) game.win();
                else game.nextLevel();
            } else game.gameOver();
        }
    }

}