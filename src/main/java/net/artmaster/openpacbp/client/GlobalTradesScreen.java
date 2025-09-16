package net.artmaster.openpacbp.client;

import net.artmaster.openpacbp.gui.GlobalTradesMenu;
import net.artmaster.openpacbp.network.Network;
import net.artmaster.openpacbp.network.SyncPartiesPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;


public class GlobalTradesScreen extends AbstractContainerScreen<GlobalTradesMenu> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("openpacbp", "textures/gui/quest_menu.png");

    public GlobalTradesScreen(GlobalTradesMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.imageWidth = 176;
        this.imageHeight = 200;
    }

    public void setParties(List<SyncPartiesPacket.PartyData> parties) {
        this.menu.getAllParties().updateParties(parties); // Добавим метод в GlobalTradesMenu/AllPartiesContainer
        this.menu.reloadPage(); // Обновляем текущую страницу
    }

    @Override
    protected void init() {
        super.init();
        Network.requestPartyName(menu.getCurrentPage());
        Network.requestPartyColor(menu.getCurrentPage());

        int left = this.leftPos;
        int top = this.topPos;


        // Кнопки
        this.addRenderableWidget(Button.builder(Component.literal("<"), b -> {
            menu.prevPage();
        }).bounds(left + 7, top + 80, 20, 20).build());

        this.addRenderableWidget(Button.builder(Component.literal(">"), b -> {
            if (menu.getCurrentPage() != menu.getTotalParties() -1) menu.nextPage();
        }).bounds(left + 27, top + 80, 20, 20).build());
        this.addRenderableWidget(Button.builder(Component.literal("Гильдия"), b -> {
            menu.partyPage();
        }).bounds(left + 47, top + 80, 49, 20).build());
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {


        Component colorizedPartyName = Component.literal(Network.getPartyName())
                .withStyle(style -> style.withColor(TextColor.fromRgb(Network.getPartyColor())));

        graphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
        graphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 94, 4210752, false);
        graphics.drawString(this.font, colorizedPartyName, 8, this.titleLabelY+20, 0xFFFFFF, true);
        graphics.drawString(this.font,
                "Страница " + (menu.getCurrentPage() + 1) + "/" + menu.getTotalParties(),
                this.titleLabelX+90, this.titleLabelY, 4210752, false);

    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        graphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);

    }



}
