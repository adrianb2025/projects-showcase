package game.gfx.weather;

public class Weather {

    private boolean isRain;
    private boolean isThunder;

    public Weather() {}

    public Weather(boolean rain, boolean thunder) {
        isRain = rain;
        isThunder = thunder;
    }

    public boolean isRain() { return isRain; }
    public void setRain(boolean rain) { isRain = rain; }
    public boolean isThunder() { return isThunder; }
    public void setThunder(boolean thunder) { isThunder = thunder; }

}
