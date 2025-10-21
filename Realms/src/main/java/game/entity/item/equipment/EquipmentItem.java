package game.entity.item.equipment;

import game.entity.CharacterStats;
import game.entity.Entity;
import game.entity.item.Item;
import game.entity.item.ItemEntity;
import game.gfx.Screen;

public class EquipmentItem extends Item {

    private Equipment equipment;

    public EquipmentItem(Equipment equipment) { this.equipment = equipment; }

    public boolean matches(Item item) {
        if (item instanceof EquipmentItem) {
            EquipmentItem other = (EquipmentItem) item;
            return other.getEquipment() == equipment;
        }

        return false;
    }

    public void renderIcon(Screen screen, int x, int y) {
        screen.render(x, y, equipment.getXSprite() * 8, equipment.getYSprite() * 8, equipment.getColor(), 0);
    }

    public void renderInventory(Screen screen, int x, int y) {
        screen.render(x, y, equipment.getXSprite() * 8, equipment.getYSprite() * 8, equipment.getColor(), 0);
    }

    public int getColor() { return equipment.getColor(); }
    public int getXSprite() { return equipment.getXSprite(); }
    public int getYSprite() { return equipment.getYSprite(); }
    public int getScale() { return 3; }
    public int getMaxTime() { return 2000; }
    public String getName() { return equipment.getName(); }
    public boolean canAttack() { return true; }
    public CharacterStats getBonusStats() { return equipment.getBonusStats(); }
    public EquipmentType getEquipmentType() { return equipment.getEquipmentType(); }
    public Equipment getEquipment() { return equipment; }
    public void onTake(ItemEntity e) {}

}