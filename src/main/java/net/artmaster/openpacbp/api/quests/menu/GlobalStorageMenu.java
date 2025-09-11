package net.artmaster.openpacbp.api.quests.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.Container;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;


public class GlobalStorageMenu extends AbstractContainerMenu {
    private final Container container;

    public GlobalStorageMenu(int windowId, Inventory playerInventory, Container container) {
        super(ModMenus.GLOBAL_STORAGE.get(), windowId);
        this.container = container;

        // Слоты контейнера (3x3)
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.addSlot(new Slot(container, j + i * 3, 62 + j * 18, 17 + i * 18));
            }
        }

        // Инвентарь игрока (3 ряда по 9)
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        // Хотбар
        for (int k = 0; k < 9; k++) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }

    // "Фабричный" конструктор, если нужен, например, клиенту
    public static GlobalStorageMenu create(int windowId, Inventory inv) {
        return new GlobalStorageMenu(windowId, inv, new SimpleContainer(9));
    }



    @Override
    public boolean stillValid(Player player) {
        return true;
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
