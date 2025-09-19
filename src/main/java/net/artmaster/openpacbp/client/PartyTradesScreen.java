package net.artmaster.openpacbp.client;

import net.artmaster.openpacbp.ModMain;
import net.artmaster.openpacbp.gui.PartyTradesMenu;
import net.artmaster.openpacbp.network.Network;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PartyTradesScreen extends AbstractContainerScreen<PartyTradesMenu> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(ModMain.MODID, "textures/gui/party_storage_menu.png");

    public PartyTradesScreen(PartyTradesMenu menu, Inventory playerInv, Component title) {
        super(menu, playerInv, title);
        this.imageWidth = 176; // ширина текстуры
        this.imageHeight = 200; // высота текстуры
    }

    boolean render_end = false;

    @Override
    protected void init() {
        super.init();
        //Network.requestPartyName();
        int x = leftPos + 8;
        int y = topPos + 20;

        this.addRenderableWidget(Button.builder(
                Component.literal("Рынок"),
                (btn) -> {
                    Network.sendButtonClick("openpacbp market global");
                }
        ).bounds(x, y, 70, 20).build());
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
        graphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 94, 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        graphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);
        //graphics.drawString(this.font, Network.getPartyName(), leftPos+9, topPos+42, 0xFFFFFF);
    }



}
