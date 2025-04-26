package LLD.VendingMachine.V1;

import java.util.HashMap;
import java.util.Map;

public class Inventory {
    
    private Map<Item, Integer> items = new HashMap<>();

    public void addItem(Item item, int quantity) {
        int currentQty = items.getOrDefault(item, 0);
        items.put(item, currentQty + quantity);
    }

    public void removeItem(Item item, int quantity) throws InventoryException {
        int currentQty = items.getOrDefault(item, 0);
        if (currentQty < quantity) {
            throw new InventoryException("Invalid request. Cannot remove the quantity more that the available quantity");
        }
        currentQty -= quantity;
        if (currentQty == 0) {
            items.remove(item);
        }
    }

    public int getQuantity(Item item) {
        return items.getOrDefault(item, 0);
    }

    public boolean isAvailable(Item item, int quantity) {
        return getQuantity(item) >= quantity;
    }

}
