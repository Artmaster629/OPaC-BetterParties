package net.artmaster.openpacbp.client;

import net.artmaster.openpacbp.ModMain;
import net.artmaster.openpacbp.api.quests.menu.GlobalStorageMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GlobalStorageScreen extends AbstractContainerScreen<GlobalStorageMenu> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(ModMain.MODID, "textures/buttons/red_button.png");

    public GlobalStorageScreen(GlobalStorageMenu menu, Inventory playerInv, Component title) {
        super(menu, playerInv, title);
        this.imageWidth = 176; // ширина текстуры
        this.imageHeight = 166; // высота текстуры
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        graphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }
}
