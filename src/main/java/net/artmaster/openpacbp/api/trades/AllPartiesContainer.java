package net.artmaster.openpacbp.api.trades;

import net.artmaster.openpacbp.network.parties.SyncPartiesPacket;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AllPartiesContainer {
    private final List<PartyInventoryData> parties;
    private int currentPage = 0;
    private final int pageSize = SyncPartiesPacket.TRADE_SLOTS;


    //Конструктор
    public AllPartiesContainer(List<PartyInventoryData> parties) {
        this.parties = new ArrayList<>(parties);
    }


    //Методы управления страницами
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

    public int getTotalPages() {
        int totalItems = parties.stream()
                .mapToInt(p -> p.getContainer().getContainerSize())
                .sum();
        return Math.max(1, (int)Math.ceil(totalItems / (double)pageSize));
    }

    public int getCurrentPageIndex() {
        return currentPage;
    }



    //Синхронизация гильдий, контейнеров
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
            this.parties.add(pd.toPartyInventoryData());
        }
        currentPage = 0;
    }



    //Получение предметов на страницу
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

    //Получение предметов только из торговых слотов
    public List<ItemStack> getTradeItemsForPage() {
        if (currentPage < 0 || currentPage >= parties.size()) {
            return Collections.emptyList();
        }

        PartyInventoryData currentParty = parties.get(currentPage);
        SimpleContainer container = currentParty.getContainer();
        List<ItemStack> items = new ArrayList<>();

        for (int i = 0; i < 9; i++) {
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty()) {
                items.add(stack);
            }
        }

        return items;
    }
}
