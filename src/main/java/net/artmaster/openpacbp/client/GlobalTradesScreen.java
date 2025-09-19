package net.artmaster.openpacbp.client;

import net.artmaster.openpacbp.gui.GlobalTradesMenu;
import net.artmaster.openpacbp.network.Network;
import net.artmaster.openpacbp.network.parties.SyncPartiesPacket;
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
            ResourceLocation.fromNamespaceAndPath("openpacbp", "textures/gui/market_storage_menu.png");

    public GlobalTradesScreen(GlobalTradesMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.imageWidth = 176;
        this.imageHeight = 200;

    }

    public void setParties(List<SyncPartiesPacket.PartyData> parties) {
        this.menu.getAllParties().updateParties(parties);
        this.menu.setParties(parties);
        this.menu.reloadPage();
    }


    @Override
    protected void init() {
        super.init();


        if (menu.getAllParties().getCurrentParty() != null) {
            menu.getPartyName();
            menu.getPartyColor();
        }

        Network.requestPartyName(menu.getCurrentPage());
        Network.requestPartyColor(menu.getCurrentPage());




        int left = this.leftPos;
        int top = this.topPos;

        int x = leftPos + 8;
        int y = topPos + 20;



        // Кнопки
        this.addRenderableWidget(Button.builder(Component.literal("<"), b -> {
            menu.prevPage();
        }).bounds(left + 7, top + 80, 20, 20).build());

        this.addRenderableWidget(Button.builder(Component.literal(">"), b -> {
            if (menu.getCurrentPage() != menu.getTotalParties() -1) menu.nextPage();
        }).bounds(left + 27, top + 80, 20, 20).build());


        //Торговые кнопки

        this.addRenderableWidget(Button.builder(Component.literal("✓"), b -> {

            if (menu.getAllParties().getCurrentParty() == null) return;
            Network.buyItem(menu.getCurrentPage(), 0, 1);
        }).bounds(left + 96, top + 19, 18, 18).build());

        this.addRenderableWidget(Button.builder(Component.literal("✓"), b -> {

            if (menu.getAllParties().getCurrentParty() == null) return;
            Network.buyItem(menu.getCurrentPage(), 2, 3);
        }).bounds(left + 96, top + 40, 18, 18).build());

        this.addRenderableWidget(Button.builder(Component.literal("✓"), b -> {

            if (menu.getAllParties().getCurrentParty() == null) return;
            Network.buyItem(menu.getCurrentPage(), 4, 5);
        }).bounds(left + 96, top + 61, 18, 18).build());

        this.addRenderableWidget(Button.builder(Component.literal("✓"), b -> {

            if (menu.getAllParties().getCurrentParty() == null) return;
            Network.buyItem(menu.getCurrentPage(), 6, 7);
        }).bounds(left + 96, top + 82, 18, 18).build());



        this.addRenderableWidget(Button.builder(Component.literal("Наша страница"), b -> {
            Network.sendButtonClick("openpacbp market settings");
        }).bounds(x, y, 70, 20).build());
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {


        String partyName = "Нет данных";
        int partyColor;

        if ((menu.getTotalParties() == 0)) return;
        if ((menu.getAllParties() == null)) return;


        if (menu.getAllParties().getCurrentParty() != null) {
            partyName = menu.getPartyName();
            partyColor = menu.getPartyColor();
        } else {
            partyColor = 0xAAAAAA;
        }

        Component colorizedPartyName = Component.literal(partyName)
                .withStyle(style -> style.withColor(TextColor.fromRgb(partyColor)));

        graphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
        graphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 94, 4210752, false);
        graphics.drawString(this.font, colorizedPartyName, 8, this.titleLabelY+40, 0xFFFFFF, true);
        graphics.drawString(this.font,
                "Страница " + (menu.getCurrentPage() + 1) + "/" + menu.getTotalParties(),
                this.titleLabelX+90, this.titleLabelY, 4210752, false);

    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        graphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);

    }



}
