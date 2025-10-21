package game.entity.item;

import game.entity.item.equipment.Equipment;
import game.entity.item.equipment.EquipmentItem;
import game.entity.item.equipment.EquipmentType;
import game.entity.item.resource.Resource;
import game.entity.item.resource.ResourceItem;

import java.util.ArrayList;
import java.util.List;

public class Inventory {

    public List<Item> items = new ArrayList<Item>();

    public void add(Item item) { add(items.size(), item); }

    public void add(int slot, Item item) {
        if (item instanceof ResourceItem) {
            ResourceItem toTake = (ResourceItem) item;
            ResourceItem has = findResource(toTake.getResource());
            if (has == null) items.add(slot, toTake);
            else has.addCount(toTake.getCount());
        } else if (item instanceof EquipmentItem) {
            EquipmentItem toTake = (EquipmentItem) item;
            EquipmentItem has = findEquipmentByType(toTake.getEquipmentType());
            if (has == null) items.add(slot, toTake);
            else {
                slot = items.indexOf(has);
                items.remove(has);
                items.add(slot, toTake);
            }
        } else items.add(slot, item);
    }

    public EquipmentItem findEquipment(Equipment equipment) {
        for (Item item : items) {
            if (item instanceof EquipmentItem) {
                EquipmentItem has = (EquipmentItem) item;
                if (has.getEquipment() == equipment) return has;
            }
        }

        return null;
    }

    public EquipmentItem findEquipmentByType(EquipmentType equipType) {
        for (Item item : items) {
            if (item instanceof EquipmentItem) {
                EquipmentItem has = (EquipmentItem) item;
                if (has.getEquipmentType() == equipType) return has;
            }
        }

        return null;
    }

    public ResourceItem findResource(Resource resource) {
        for (Item item : items) {
            if (item instanceof ResourceItem) {
                ResourceItem has = (ResourceItem) item;
                if (has.getResource() == resource) return has;
            }
        }

        return null;
    }

    public boolean hasResources(Resource r, int count) {
        ResourceItem ri = findResource(r);
        if (ri == null) return false;
        return ri.getCount() >= count;
    }

    public boolean removeResource(Resource r, int count) {
        ResourceItem ri = findResource(r);
        if (ri == null) return false;
        if (ri.getCount() < count) return false;
        ri.addCount(-count);
        if (ri.getCount() <= 0) items.remove(ri);
        return true;
    }

    public int count(Item item) {
        if (item instanceof ResourceItem) {
            ResourceItem ri = findResource(((ResourceItem) item).getResource());
            if (ri != null) return ri.getCount();
        } else {
            int count = 0;
            for (Item item1 : items) {
                if (item1.matches(item)) count++;
            }

            return count;
        }

        return 0;
    }

    public List<Item> getItems() { return items; }

}