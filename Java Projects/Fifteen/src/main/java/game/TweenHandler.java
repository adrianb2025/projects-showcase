package game;

import java.util.ArrayList;
import java.util.List;

public class TweenHandler {

    private List<TweenEntry> tweens = new ArrayList<>();

    public void tween(int duration, Tween.TweenAction action) {
        if (action == null || duration <= 0) return;
        tweens.add(new TweenEntry(duration, action));
    }

    public void tick() {
        tweens.removeIf(TweenEntry::update);
    }

    private static class TweenEntry {
        Tween.TweenAction action;
        int currentTick;
        int totalTicks;

        TweenEntry(int duration, Tween.TweenAction action) {
            this.action = action;
            totalTicks = duration;
            currentTick = 0;
        }

        boolean update() {
            currentTick++;

            double t = Math.min((double) currentTick / totalTicks, 1.0);
            action.update(t);

            if (currentTick >= totalTicks) {
                action.onComplete();
                return true;
            }

            return false;
        }
    }

    public class Tween {
        public interface TweenAction {
            void update(double t);
            void onComplete();
        }
    }

}