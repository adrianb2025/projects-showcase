package game.entity.hopper;

public class OrangeHopper extends Hopper {

    public OrangeHopper(double x, double y) {
        super(x, y);
        sprite = 2;
        ignoresLava = true;
    }

    public boolean useSkill() { return false; }

}