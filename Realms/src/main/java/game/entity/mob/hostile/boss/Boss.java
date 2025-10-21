package game.entity.mob.hostile.boss;

import game.entity.Entity;
import game.entity.item.equipment.Equipment;
import game.entity.mob.Mob;

public class Boss extends Mob {

    public static final Equipment[][] drops = {
            { Equipment.basicArmor, Equipment.basicShoes },
            { Equipment.intermediateBow },
            { Equipment.advancedArmor, Equipment.intermediateShoes },
            { Equipment.expertArmor },
            { Equipment.advancedBow, Equipment.advancedShoes, Equipment.mythicalArmor },
            { Equipment.divineArmor, Equipment.masterBow, Equipment.masterShoes },
            {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}};

    public boolean canSwim() { return true; }
    public boolean blocks(Entity e) { return true; }

}