package net.artmaster.openpacbp.api.quests.menu;

import com.mojang.blaze3d.vertex.PoseStack;
import net.artmaster.openpacbp.api.quests.GlobalStorageData;
import net.artmaster.openpacbp.api.quests.MyAttachments;
import net.artmaster.openpacbp.api.quests.PlayerInventoryData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import xaero.pac.common.server.api.OpenPACServerAPI;

import java.util.Objects;


public class GlobalStorageMenu extends AbstractContainerMenu {
    private final Container container;
    String partyInfo = "";

    public GlobalStorageMenu(int windowId, Inventory playerInventory, Container container) {
        super(ModMenus.GLOBAL_STORAGE.get(), windowId);
        this.container = container;
        Level level = playerInventory.player.level();







        GlobalStorageData storage = level.getData(MyAttachments.GLOBAL_STORAGE);
        PlayerInventoryData inv = storage.getOrCreatePlayerInv(playerInventory.player.getUUID());


                // Слоты контейнера (3x3)
//        for (int i = 0; i < 3; i++) {
//            for (int j = 0; j < 3; j++) {
//                this.addSlot(new Slot(container, j + i * 3, 62 + j * 18, 17 + i * 18)).set(inv.getContainer().getItem(j + i *3));
//            }
//        }

        this.addSlot(new Slot(container, 0, 80 + 2 * 18, -1 + 1 * 21)).set(inv.getContainer().getItem(0));
        this.addSlot(new Slot(container, 1, 80 + 4 * 18, -1 + 1 * 21)).set(inv.getContainer().getItem(1));
        this.addSlot(new Slot(container, 2, 80 + 2 * 18, -1 + 2 * 21)).set(inv.getContainer().getItem(2));
        this.addSlot(new Slot(container, 3, 80 + 4 * 18, -1 + 2 * 21)).set(inv.getContainer().getItem(3));
        this.addSlot(new Slot(container, 4, 80 + 2 * 18, -1 + 3 * 21)).set(inv.getContainer().getItem(4));
        this.addSlot(new Slot(container, 5, 80 + 4 * 18, -1 + 3 * 21)).set(inv.getContainer().getItem(5));
        this.addSlot(new Slot(container, 6, 80 + 2 * 18, -1 + 4 * 21)).set(inv.getContainer().getItem(6));
        this.addSlot(new Slot(container, 7, 80 + 4 * 18, -1 + 4 * 21)).set(inv.getContainer().getItem(7));

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

    public static GlobalStorageMenu create(int windowId, Inventory inv) {
        return new GlobalStorageMenu(windowId, inv, new SimpleContainer(9));
    }



    @Override
    public boolean stillValid(Player player) {
        return true;
    }



    @Override
    public void removed(@NotNull Player player) {
        if (!player.level().isClientSide()) { // только сервер
            Level level = player.level();
            GlobalStorageData storage = level.getData(MyAttachments.GLOBAL_STORAGE);

            PlayerInventoryData inv = storage.getOrCreatePlayerInv(player.getUUID());

            inv.setOwnerName(player.getGameProfile().getName());



            var server = player.getServer();
            if (server != null) {
                var info = OpenPACServerAPI.get(server)
                        .getServerClaimsManager()
                        .getPlayerInfo(player.getUUID());
                if (info != null) {
                    inv.setPartyName(info.getClaimsName());
                }
            }

            for (int i = 0; i < 9; i++) {
                inv.getContainer().setItem(i, this.slots.get(i).getItem());
            }
        }

        super.removed(player);
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
