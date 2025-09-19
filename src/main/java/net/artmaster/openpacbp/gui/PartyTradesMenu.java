package net.artmaster.openpacbp.gui;

import net.artmaster.openpacbp.api.trades.PartyInventoryData;
import net.artmaster.openpacbp.init.ModMenus;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.Container;
import org.jetbrains.annotations.NotNull;
import xaero.pac.common.server.api.OpenPACServerAPI;

import static net.artmaster.openpacbp.api.trades.StorageManager.getPartyInventory;


public class PartyTradesMenu extends AbstractContainerMenu {
    private final Container container;

    public PartyTradesMenu(int windowId, Inventory playerInventory, Container container) {
        super(ModMenus.PARTY_STORAGE_TRADES.get(), windowId);
        this.container = container;

        assert playerInventory.player.getServer() != null;

        //Слоты торговли
        this.addSlot(new Slot(container, 0, 80 + 2 * 18, -1 + 1 * 21));
        this.addSlot(new Slot(container, 1, 80 + 4 * 18, -1 + 1 * 21));
        this.addSlot(new Slot(container, 2, 80 + 2 * 18, -1 + 2 * 21));
        this.addSlot(new Slot(container, 3, 80 + 4 * 18, -1 + 2 * 21));
        this.addSlot(new Slot(container, 4, 80 + 2 * 18, -1 + 3 * 21));
        this.addSlot(new Slot(container, 5, 80 + 4 * 18, -1 + 3 * 21));
        this.addSlot(new Slot(container, 6, 80 + 2 * 18, -1 + 4 * 21));
        this.addSlot(new Slot(container, 7, 80 + 4 * 18, -1 + 4 * 21));

        //Полученное на торговле
        this.addSlot(new Slot(container, 10, 8, 65));
        this.addSlot(new Slot(container, 11, 26, 65));
        this.addSlot(new Slot(container, 12, 44, 65));
        this.addSlot(new Slot(container, 13, 62, 65));
        this.addSlot(new Slot(container, 14, 8, 83));
        this.addSlot(new Slot(container, 15, 26, 83));
        this.addSlot(new Slot(container, 16, 44, 83));
        this.addSlot(new Slot(container, 17, 62, 83));



        // Инвентарь игрока (3 ряда по 9)
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 118 + i * 18));

            }
        }

        // Хотбар
        for (int k = 0; k < 9; k++) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 176));
        }
    }

    public static PartyTradesMenu create(int windowId, Inventory inv) {
        return new PartyTradesMenu(windowId, inv, new SimpleContainer(18));
    }



    @Override
    public boolean stillValid(Player player) {
        return true;
    }



    @Override
    public void removed(@NotNull Player player) {
        if (!player.level().isClientSide()) {
            PartyInventoryData storage = getPartyInventory(player.getServer(), player.getUUID());
            if (storage != null) {
                SimpleContainer inv = storage.getContainer();
                var server = player.getServer();
                if (server != null) {
                    var info = OpenPACServerAPI.get(server)
                            .getServerClaimsManager()
                            .getPlayerInfo(player.getUUID());
                    if (info != null) {
                        storage.setPartyName(info.getClaimsName());
                        storage.setPartyColor(info.getClaimsColor());
                    }
                }

                for (int i = 0; i < 9; i++) {
                    inv.setItem(i, this.slots.get(i).getItem());
                }
            }
            super.removed(player);
        }


    }


    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();
            itemstack = stack.copy();

            int containerSize = this.container.getContainerSize();

            if (index < containerSize) {
                if (!this.moveItemStackTo(stack, containerSize, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!this.moveItemStackTo(stack, 0, containerSize, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return itemstack;
    }
}
