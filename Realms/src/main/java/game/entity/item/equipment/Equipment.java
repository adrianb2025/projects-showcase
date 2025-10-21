package game.entity.item.equipment;

import game.entity.CharacterStats;
import game.gfx.util.PaletteHelper;

import java.util.HashMap;
import java.util.Map;

public class Equipment {

    public static final Map<String, Equipment> equippable = new HashMap<String, Equipment>();

    public static final Equipment basicBow = new Equipment("Bow", 0, 12, PaletteHelper.getColor(-1, 100, 220, 555), new CharacterStats(0, 0, 8, 0, 0, 20), EquipmentType.WEAPON);
    public static final Equipment intermediateBow = new Equipment("I.Bow", 0, 12, PaletteHelper.getColor(-1, 100, 500, 555), new CharacterStats(0, 0, 12, 0, 0, 15), EquipmentType.WEAPON);
    public static final Equipment advancedBow = new Equipment("A.Bow", 0, 12, PaletteHelper.getColor(-1, 100, 0, 555), new CharacterStats(0, 0, 15, 0, 0, 10), EquipmentType.WEAPON);
    public static final Equipment masterBow = new Equipment("M.Bow", 0, 12, PaletteHelper.getColor(-1, 100, 541, 555), new CharacterStats(0, 0, 20, 0, 0, 5), EquipmentType.WEAPON);

    public static final Equipment basicArmor = new Equipment("Armor", 1, 12, PaletteHelper.getColor(-1, 100, 220, 555), new CharacterStats(0, 5, 0, 0, 0), EquipmentType.ARMOR);
    public static final Equipment intermediateArmor = new Equipment("I.Armor", 1, 12, PaletteHelper.getColor(-1, 100, 220, 555), new CharacterStats(1, 10, 0, 0, 0), EquipmentType.ARMOR);
    public static final Equipment advancedArmor = new Equipment("A.Armor", 1, 12, PaletteHelper.getColor(-1, 100, 220, 555), new CharacterStats(2, 15, 0, 0, 0), EquipmentType.ARMOR);
    public static final Equipment expertArmor = new Equipment("E.Armor", 1, 12, PaletteHelper.getColor(-1, 100, 220, 555), new CharacterStats(3, 20, 0, 0, 0), EquipmentType.ARMOR);
    public static final Equipment legendaryArmor = new Equipment("L.Armor", 1, 12, PaletteHelper.getColor(-1, 100, 220, 555), new CharacterStats(4, 25, 0, 0, 0), EquipmentType.ARMOR);
    public static final Equipment mythicalArmor = new Equipment("M.Armor", 1, 12, PaletteHelper.getColor(-1, 100, 220, 555), new CharacterStats(5, 50, 0, 0, 0), EquipmentType.ARMOR);
    public static final Equipment divineArmor = new Equipment("D.Armor", 1, 12, PaletteHelper.getColor(-1, 100, 220, 555), new CharacterStats(10, 100, 0, 0, 0), EquipmentType.ARMOR);

    public static final Equipment basicShoes = new Equipment("Shoes", 2, 12, PaletteHelper.getColor(-1, 100, 220, 555), new CharacterStats(0, 0, 0, 0, 1), EquipmentType.SHOES);
    public static final Equipment intermediateShoes = new Equipment("I.Shoes", 2, 12, PaletteHelper.getColor(-1, 100, 500, 555), new CharacterStats(0, 0, 0, 0, 2), EquipmentType.SHOES);
    public static final Equipment advancedShoes = new Equipment("A.Shoes", 2, 12, PaletteHelper.getColor(-1, 100, 0, 555), new CharacterStats(0, 0, 0, 0, 5), EquipmentType.SHOES);
    public static final Equipment masterShoes = new Equipment("M.Shoes", 2, 12, PaletteHelper.getColor(-1, 100, 541, 555), new CharacterStats(0, 0, 0, 0, 20), EquipmentType.SHOES);

    private final EquipmentType equipmentType;
    private final CharacterStats bonusStats;
    private final String name;
    private final int xSprite;
    private final int ySprite;
    private final int col;

    public Equipment(String name, int xSprite, int ySprite, int col, CharacterStats bonusStats, EquipmentType equipmentType) {
        this.name = name;
        this.xSprite = xSprite;
        this.ySprite = ySprite;
        this.col = col;
        this.bonusStats = bonusStats;
        this.equipmentType = equipmentType;
        equippable.put(name.toLowerCase(), this);
    }

    public static Equipment getEquipmentByName(String name) { return equippable.get(name); }
    public CharacterStats getBonusStats() { return bonusStats; }
    public int getColor() { return col; }
    public String getName() { return name; }
    public EquipmentType getEquipmentType() { return equipmentType; }
    public int getXSprite() { return xSprite; }
    public int getYSprite() { return ySprite; }

}