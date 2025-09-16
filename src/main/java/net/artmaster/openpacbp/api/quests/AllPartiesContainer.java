package net.artmaster.openpacbp.api.quests;

import net.artmaster.openpacbp.network.SyncPartiesPacket;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AllPartiesContainer {
    private final List<PartyInventoryData> parties;
    private int currentPage = 0;
    private final int pageSize = 9; // слотов на одной странице

    public AllPartiesContainer(List<PartyInventoryData> parties) {
        // Гарантируем, что всегда ArrayList
        this.parties = new ArrayList<>(parties);
    }

    public PartyInventoryData getCurrentParty() {
        if (currentPage < 0 || currentPage >= parties.size()) return null;
        return parties.get(currentPage);
    }

    public void nextPage() {
        if (currentPage < getTotalPages() - 1) currentPage++;
    }

    public void prevPage() {
        if (currentPage > 0) currentPage--;
    }

    public int getCurrentPageIndex() {
        return currentPage;
    }

    public int getTotalPages() {
        int totalItems = parties.stream()
                .mapToInt(p -> p.getContainer().getContainerSize())
                .sum();
        return Math.max(1, (int)Math.ceil(totalItems / (double)pageSize));
    }

    public int getTotalParties() {
        return parties.size();
    }

    public void updateContainers(List<PartyInventoryData> containers) {
        this.parties.clear();
        this.parties.addAll(containers);
        this.currentPage = 0;
    }

    public void updateParties(List<SyncPartiesPacket.PartyData> newParties) {
        this.parties.clear();
        for (var pd : newParties) {
            this.parties.add(pd.toPartyInventoryData()); // Нужно написать метод конверсии PartyData -> PartyInventoryData
        }
        currentPage = 0;
    }



    /** Получаем список ItemStack для текущей страницы */
//    public List<ItemStack> getItemsForPage() {
//        List<ItemStack> allItems = new ArrayList<>();
//        for (PartyInventoryData party : parties) {
//            SimpleContainer container = party.getContainer();
//            for (int i = 0; i < container.getContainerSize(); i++) {
//                ItemStack stack = container.getItem(i);
//                if (!stack.isEmpty()) allItems.add(stack);
//            }
//        }
//
//        int start = currentPage * pageSize;
//        int end = Math.min(start + pageSize, allItems.size());
//        if (start >= allItems.size()) return Collections.emptyList();
//        return allItems.subList(start, end);
//    }



    public List<ItemStack> getItemsForPage() {
        if (currentPage < 0 || currentPage >= parties.size()) {
            return Collections.emptyList();
        }

        PartyInventoryData currentParty = parties.get(currentPage);
        SimpleContainer container = currentParty.getContainer();
        List<ItemStack> items = new ArrayList<>();

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty()) {
                items.add(stack);
            }
        }

        return items;
    }
}
