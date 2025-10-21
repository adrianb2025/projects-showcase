package game.entity;

public class CharacterStats {

    private int strength;
    private int endurance;
    private int slowPeriod;
    private int defense;
    private int regeneration;
    private int attackDelay;

    public CharacterStats(int defense, int endurance, int strength, int regeneration, int slowPeriod) {
        this.defense = defense;
        this.endurance = endurance;
        this.strength = strength;
        this.regeneration = regeneration;
        this.slowPeriod = slowPeriod;
    }

    public CharacterStats(int defense, int endurance, int strength, int regeneration, int slowPeriod, int attackDelay) {
        this.attackDelay = attackDelay;
        this.defense = defense;
        this.endurance = endurance;
        this.strength = strength;
        this.regeneration = regeneration;
        this.slowPeriod = slowPeriod;
    }

    public CharacterStats() {}

    public CharacterStats mergeStats(CharacterStats stats) {
        CharacterStats result = new CharacterStats();
        result.setDefense(stats.getDefense() + defense);
        result.setEndurance(stats.getEndurance() + endurance);
        result.setStrength(stats.getStrength() + strength);
        result.setRegeneration(stats.getRegeneration() + regeneration);
        result.setAttackDelay(stats.getAttackDelay() + attackDelay);
        return result;
    }

    public boolean match(CharacterStats stats) {
        return stats.getAttackDelay() == attackDelay && stats.getDefense() == defense && stats.getEndurance() == endurance && stats.getStrength() == strength && stats.getRegeneration() == regeneration && stats.getSlowPeriod() == slowPeriod;
    }

    public int getDefense() { return defense; }
    public void setDefense(int defense) { this.defense = defense; }
    public int getEndurance() { return endurance; }
    public void setEndurance(int endurance) { this.endurance = endurance; }
    public int getStrength() { return strength; }
    public void setStrength(int strength) { this.strength = strength; }
    public int getRegeneration() { return regeneration; }
    public void setRegeneration(int regeneration) { this.regeneration = regeneration; }
    public int getSlowPeriod() { return slowPeriod; }
    public void setSlowPeriod(int slowPeriod) { this.slowPeriod = slowPeriod; }
    public int getAttackDelay() { return attackDelay; }
    public void setAttackDelay(int attackDelay) { this.attackDelay = attackDelay; }

}