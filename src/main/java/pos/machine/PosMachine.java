package pos.machine;

import java.util.*;

import static pos.machine.ItemDataLoader.loadBarcodes;

public class PosMachine {

    public static String printReceipt(List<String> barcodes)
    {
        ItemDataLoader itemDataLoader = new ItemDataLoader();
        List <String> items = itemDataLoader.loadBarcodes(); // shopping cart
        List <ItemInfo> productInformation = itemDataLoader.loadAllItemInfos();
        List <ItemInfo> itemWithProductInformation = new ArrayList<>();
        itemWithProductInformation = getProductInformation(items, productInformation);
        Map<ItemInfo, Integer> hashMap = computeSubtotal(itemWithProductInformation);
        String receipt = generateReceipt(hashMap);
        return  receipt;
    }

    private static String generateReceipt(Map<ItemInfo, Integer> hashMap) {
        int totalItems = 0;
        String receipt = "***<store earning no money>Receipt***\n";

        for (Map.Entry<ItemInfo, Integer> val : hashMap.entrySet()) {
            receipt+="Name: " + val.getKey().getName() + ", "
                    + "Quantity: " + val.getValue() + ", "
                    + "Unit price: " + val.getKey().getPrice()+ " (yuan), "
                    + "Subtotal: " + (val.getValue() * val.getKey().getPrice()) + " (yuan)\n";
            totalItems+=(val.getValue() * val.getKey().getPrice());
        }
        receipt+="----------------------\n";
        receipt+="Total: " + totalItems + " (yuan)\n";
        receipt+="**********************";

        return receipt;
    }

    private static Map<ItemInfo, Integer> computeSubtotal(List <ItemInfo> itemWithProductInformation) {
        Map<ItemInfo, Integer> hashMap = new HashMap<ItemInfo, Integer>();
        for (ItemInfo item : itemWithProductInformation) {
            Integer count = hashMap.get(item);
            hashMap.put(item, (count == null) ? 1 : count + 1);
        }
        return hashMap;
    }

    private static List<ItemInfo> getProductInformation(List<String> items, List<ItemInfo> productInformation) {
        List <ItemInfo> itemWithProductInformation = new ArrayList<>();
        items.stream().forEach(item -> {
            ItemInfo itemInfo = productInformation.stream().filter(product -> item.equals(product.getBarcode())).findFirst().orElse(null);
            itemWithProductInformation.add(itemInfo);
        });
        return itemWithProductInformation;
    }
}
