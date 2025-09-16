package net.artmaster.openpacbp.gui;

import com.mojang.logging.LogUtils;
import net.artmaster.openpacbp.api.quests.AllPartiesContainer;
import net.artmaster.openpacbp.api.quests.PartyInventoryData;
import net.artmaster.openpacbp.init.ModMenus;
import net.artmaster.openpacbp.network.Network;
import net.artmaster.openpacbp.network.SyncPartiesPacket;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;


public class GlobalTradesMenu extends AbstractContainerMenu {
    private final AllPartiesContainer allParties;
    private final SimpleContainer pageContainer = new SimpleContainer(9); // для текущей страницы
    private static final int PAGE_SIZE = 9;

    private List<SyncPartiesPacket.PartyData> syncedParties;



    public GlobalTradesMenu(int id, Inventory playerInv, List<PartyInventoryData> parties) {
        super(ModMenus.GLOBAL_STORAGE_TRADES.get(), id);
        this.allParties = new AllPartiesContainer(parties);




        loadPage();

        // Слоты для страницы
        int[][] positions = {
                {80 + 2*18, -1 + 1*21},
                {80 + 4*18, -1 + 1*21},
                {80 + 2*18, -1 + 2*21},
                {80 + 4*18, -1 + 2*21},
                {80 + 2*18, -1 + 3*21},
                {80 + 4*18, -1 + 3*21},
                {80 + 2*18, -1 + 4*21},
                {80 + 4*18, -1 + 4*21},
                {80 + 5*18, -1 + 4*21},
        };

        for (int i = 0; i < PAGE_SIZE; i++) {
            this.addSlot(new Slot(pageContainer, i, positions[i][0], positions[i][1]));
        }

        // Инвентарь игрока
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 9; j++)
                this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 118 + i * 18));

        // Хотбар
        for (int k = 0; k < 9; k++)
            this.addSlot(new Slot(playerInv, k, 8 + k * 18, 176));
    }

    private void loadPage() {
        pageContainer.clearContent();
        List<ItemStack> items = allParties.getItemsForPage();
        for (int i = 0; i < items.size(); i++) {
            pageContainer.setItem(i, items.get(i));
        }
    }



    public void reloadPage() {
        loadPage();
    }

    public AllPartiesContainer getAllParties() {
        return allParties;
    }



    private void savePage() {
        // Можно опционально сохранять изменения, если редактируемые слоты
        // Но нужно понимать, как сопоставлять pageContainer с реальными PartyInventoryData
    }

    public void nextPage() {
        allParties.nextPage();
        loadPage();
    }

    public void prevPage() {
        allParties.prevPage();
        loadPage();
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    public int getCurrentPage() {
        return allParties.getCurrentPageIndex();
    }

    public void partyPage() {
        Network.sendButtonClick("openpac-quests");
    }

    public int getTotalParties() {
        return allParties.getTotalParties();
    }



    public void setParties(List<SyncPartiesPacket.PartyData> parties) {
        // Создаём новый AllPartiesContainer только с предметами
        List<PartyInventoryData> partyContainers = new ArrayList<>();
        for (var p : parties) {
            PartyInventoryData pid = new PartyInventoryData(p.items().size()); // только размер
            for (int i = 0; i < p.items().size(); i++) {
                pid.getContainer().setItem(i, p.items().get(i));
            }
            partyContainers.add(pid);
        }

        this.allParties.updateContainers(partyContainers); // метод внутри AllPartiesContainer
        this.syncedParties = parties; // сохраняем для имени/цвета, если нужно
        loadPage();
    }

}


//public class GlobalTradesMenu extends AbstractContainerMenu {
//    private final List<PartyInventoryData> parties;
//    private final AllPartiesContainer pageContainer;
//    private int currentPage = 0;
//
//
//
//    public GlobalTradesMenu(int id, Inventory playerInv, List<PartyInventoryData> parties) {
//        super(ModMenus.GLOBAL_STORAGE_TRADES.get(), id);
//        this.parties = parties;
//        this.pageContainer = new AllPartiesContainer(parties); // одна пати = 9 слотов
//
//        loadPage(0);
//
//        // Слоты текущей пати
//        int slotsToShow = Math.min(9, pageContainer.getContainerSize());
//        for (int i = 0; i < slotsToShow; i++) {
//            this.addSlot(new Slot(pageContainer, i, 62 + (i % 3) * 18, 17 + (i / 3) * 18));
//        }
//
//        // Инвентарь игрока
//        for (int i = 0; i < 3; i++) {
//            for (int j = 0; j < 9; j++) {
//                this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 118 + i * 18));
//
//            }
//        }
//        // Хотбар
//        for (int k = 0; k < 9; k++) {
//            this.addSlot(new Slot(playerInv, k, 8 + k * 18, 176));
//        }
//    }
//
//    @Override
//    public boolean stillValid(Player player) {
//        return true;
//    }
//
//    @Override
//    public void removed(Player player) {
//        if (!player.level().isClientSide()) {
//            savePage();
//        }
//        super.removed(player);
//    }
//
//    private void loadPage(int page) {
//        if (page < 0 || page >= parties.size()) return;
//
//        savePage(); // сохраняем текущую
//        currentPage = page;
//        pageContainer.clearContent();
//
//        SimpleContainer c = parties.get(page).getContainer();
//        for (int i = 0; i < Math.min(pageContainer.getContainerSize(), c.getContainerSize()); i++) {
//            pageContainer.setItem(i, c.getItem(i).copy());
//        }
//    }
//
//    private void savePage() {
//        if (currentPage < 0 || currentPage >= parties.size()) return;
//        SimpleContainer c = parties.get(currentPage).getContainer();
//        for (int i = 0; i < Math.min(9, c.getContainerSize()); i++) {
//            c.setItem(i, pageContainer.getItem(i));
//        }
//    }
//
//    public void nextPage() {
//        if (currentPage < parties.size() - 1) {
//            loadPage(currentPage + 1);
//        }
//    }
//
//    public void prevPage() {
//        if (currentPage > 0) {
//            loadPage(currentPage - 1);
//        }
//    }
//
//    public int getCurrentPage() {
//        return currentPage;
//    }
//
//    public int getTotalPages() {
//        return parties.size();
//    }
//
//    @Override
//    public ItemStack quickMoveStack(Player player, int index) {
//        ItemStack itemstack = ItemStack.EMPTY;
//        Slot slot = this.slots.get(index);
//        if (slot != null && slot.hasItem()) {
//            ItemStack stack = slot.getItem();
//            itemstack = stack.copy();
//
//            int containerSize = this.pageContainer.getContainerSize();
//
//            if (index < containerSize) {
//                if (!this.moveItemStackTo(stack, containerSize, this.slots.size(), true)) {
//                    return ItemStack.EMPTY;
//                }
//            } else {
//                if (!this.moveItemStackTo(stack, 0, containerSize, false)) {
//                    return ItemStack.EMPTY;
//                }
//            }
//
//            if (stack.isEmpty()) {
//                slot.set(ItemStack.EMPTY);
//            } else {
//                slot.setChanged();
//            }
//        }
//        return itemstack;
//    }
//}
