package net.artmaster.openpacbp.gui;

import net.artmaster.openpacbp.api.gui.GlobalTradeSlot;
import net.artmaster.openpacbp.api.trades.AllPartiesContainer;
import net.artmaster.openpacbp.api.trades.PartyInventoryData;
import net.artmaster.openpacbp.init.ModMenus;
import net.artmaster.openpacbp.network.Network;
import net.artmaster.openpacbp.network.parties.SyncPartiesPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;


public class GlobalTradesMenu extends AbstractContainerMenu {
    private final AllPartiesContainer allParties;
    private final SimpleContainer pageContainer = new SimpleContainer(9);
    private static final int PAGE_SIZE = 8;




    public GlobalTradesMenu(int id, Inventory playerInv, List<PartyInventoryData> parties) {
        super(ModMenus.GLOBAL_STORAGE_TRADES.get(), id);

        this.allParties = new AllPartiesContainer(parties);




        loadPage();
        if (this.allParties.getCurrentParty() != null) {
            getPartyName();
            getPartyColor();
        }


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
            this.addSlot(new GlobalTradeSlot(pageContainer, i, positions[i][0], positions[i][1]));

        }

        // Инвентарь игрока
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 9; j++)
                this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 118 + i * 18));

        // Хотбар
        for (int k = 0; k < 9; k++)
            this.addSlot(new Slot(playerInv, k, 8 + k * 18, 176));
    }



    public static void addToSlot(SimpleContainer container, int slot, ItemStack stackToAdd, Player player) {
        if (stackToAdd.isEmpty()) return;

        ItemStack existing = container.getItem(slot);

        //Если слот пустой - положить копию
        if (existing.isEmpty()) {
            container.setItem(slot, stackToAdd.copy());
            return;
        }

        //(Item + NBT)
        if (ItemStack.isSameItemSameComponents(existing, stackToAdd)) {
            int transferable = Math.min(
                    stackToAdd.getCount(),
                    existing.getMaxStackSize() - existing.getCount()
            );

            if (transferable > 0) {
                existing.grow(transferable);
                stackToAdd.shrink(transferable);
            }
        //Если слот занят:
        } else {
            boolean placed = false;
            //Проверить все оставшиеся слоты
            for (int i=slot+1; i < container.getContainerSize(); i++) {
                ItemStack existingNew = container.getItem(i);
                //Если слот пустой - положить копию
                if (existingNew.isEmpty()) {
                    container.setItem(slot, stackToAdd.copy());
                    placed=true;
                    break;
                }
                //(Item + NBT)
                if (ItemStack.isSameItemSameComponents(existingNew, stackToAdd)) {
                    int transferable = Math.min(
                            stackToAdd.getCount(),
                            existingNew.getMaxStackSize() - existingNew.getCount()
                    );

                    if (transferable > 0) {
                        existingNew.grow(transferable);
                        stackToAdd.shrink(transferable);
                    }
                }
            }
            //Если таки нет свободных слотов - отменить всю операцию
            if (!placed) {
                player.closeContainer();
                player.displayClientMessage(Component.translatable("text.openpacbp.no_slots_on_party"), false);
                return;
            }

        }
    }

    //Метод покупки предмета
    public void buyItem(int requiredSlot, int resultSlot, Player player) {
        if (!player.level().isClientSide()) {
            var currentParty = allParties.getCurrentParty();
            if (currentParty == null) return;
            ItemStack requiredItem = allParties.getCurrentParty().getContainer().getItem(requiredSlot);
            ItemStack resultItem = allParties.getCurrentParty().getContainer().getItem(resultSlot);
            if (player.getInventory().contains(requiredItem)) {

                PartyInventoryData party = allParties.getCurrentParty();
                if (party != null) {
                    SimpleContainer container = party.getContainer();
                    player.addItem(resultItem);

                    //Удалить требуемый предмет
                    for (int i = 0; i < player.getInventory().items.size(); i++) {
                        ItemStack stack = player.getInventory().items.get(i);
                        if (ItemStack.isSameItem(stack, requiredItem)) { // проверка Item + NBT
                            int removeCount = Math.min(stack.getCount(), requiredItem.getCount());
                            stack.shrink(removeCount); // уменьшает количество в стеке
                            if (stack.isEmpty()) player.getInventory().items.set(i, ItemStack.EMPTY);
                            break;
                        }
                    }

                    //Добавить результат
                    addToSlot(container, 10, requiredItem.copyWithCount(requiredItem.getCount()), player);


                    // Удалить результат из инвентаря гильдии
                    container.removeItem(resultSlot, resultItem.getCount());
                    container.removeItem(requiredSlot, requiredItem.getCount());

                    player.containerMenu.broadcastChanges(); // синхронизировать все слоты с клиентом
                    player.inventoryMenu.broadcastChanges();

                    //Обновить всё
                    reloadPage();
                    player.closeContainer();
                    Network.sendButtonClick("openpacbp market global");
                }
            }
        }
    }

    public AllPartiesContainer getAllParties() {
        return allParties;
    }

    public String getPartyName() {
        assert allParties.getCurrentParty() != null;
        return allParties.getCurrentParty().getPartyName();
    }

    public int getPartyColor() {
        assert allParties.getCurrentParty() != null;
        return allParties.getCurrentParty().getPartyColor();
    }

    public int getCurrentPage() {
        return allParties.getCurrentPageIndex();
    }

    public int getTotalParties() {
        return allParties.getTotalParties();
    }



    ////Методы управления страницами


    //Загрузить страницу
    private void loadPage() {
        pageContainer.clearContent();
        List<ItemStack> items = allParties.getTradeItemsForPage();
        for (int i = 0; i < items.size(); i++) {
            pageContainer.setItem(i, items.get(i));

        }
        if (allParties.getTotalParties() == 0)
            return;
        if (allParties.getTotalPages() == 0)
            return;
        if (allParties.getCurrentParty() == null) return;
        //System.out.println("allParties size: " + allParties.getTotalParties());
    }


    //Перезагрузить страницу
    public void reloadPage() {
        loadPage();
    }

    //Следующая страница
    public void nextPage() {
        allParties.nextPage();
        loadPage();
    }

    //Предыдущая страница
    public void prevPage() {
        allParties.prevPage();
        loadPage();
    }

    ////Методы управления GUI

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    //Технический метод синхронизации списка гильдий и их предметов
    public void setParties(List<SyncPartiesPacket.PartyData> parties) {
        // AllPartiesContainer только с предметами
        List<PartyInventoryData> partyContainers = new ArrayList<>();
        for (var p : parties) {
            PartyInventoryData pid = new PartyInventoryData(9);

            assert !p.items().isEmpty();
            for (int i = 0; i < 8; i++) { // ограничение
                pid.getContainer().setItem(i, p.items().get(i));
                pid.setPartyName(p.partyName());
                pid.setPartyColor(p.color());

            }
            partyContainers.add(pid);
        }

        this.allParties.updateContainers(partyContainers);
        loadPage();
    }

}
