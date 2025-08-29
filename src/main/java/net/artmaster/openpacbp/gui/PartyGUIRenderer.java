package net.artmaster.openpacbp.gui;

import net.artmaster.openpacbp.network.Network;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.components.Button;
import xaero.pac.client.api.OpenPACClientAPI;
import xaero.pac.client.claims.api.IClientClaimsManagerAPI;
import xaero.pac.client.parties.party.api.IClientPartyStorageAPI;
import xaero.pac.common.parties.party.member.api.IPartyMemberAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class PartyGUIRenderer extends Screen {

    Minecraft mc = Minecraft.getInstance();

    List<Object> members = new ArrayList<>();

    IClientClaimsManagerAPI claimsManager = OpenPACClientAPI.get().getClaimsManager();
    IClientPartyStorageAPI partyManager = OpenPACClientAPI.get().getClientPartyStorage();
    List<IPartyMemberAPI> members_list = Objects.requireNonNull(partyManager.getParty()).getMemberInfoStream().toList();

    protected void getPartyMembers() {
        if (members_list.size() == 1) {
            members.add(partyManager.getParty().getOwner().getUsername()+"["+partyManager.getParty().getOwner().getRank()+"]");
        } else {
            for (int i=0; i<=members_list.size(); i++) {
                members.add(members_list.get(i).getUsername()+"["+members_list.get(i).getRank()+"]");
            }
        }
    }












    private EditBox textBox;
    private EditBox colorBox;
    public PartyGUIRenderer() {
        super(Component.literal("Управление гильдией"));
    }

    public boolean isPauseScreen() {
        return true;
    }
    public boolean shouldCloseOnEsc() {
        return true;
    }

    @Override
    protected void init() {

        this.textBox = new EditBox(
                this.font,
                0,
                20,
                200,
                20,
                Component.literal("Имя гильдии")
        );
        this.colorBox = new EditBox(
                this.font,
                0,
                60,
                200,
                20,
                Component.literal("Цвет гильдии")
        );
        this.addRenderableWidget(
                Button.builder(
                        Component.literal("->"),
                        (btn) -> onButtonClick()
                        ).bounds(
                        this.textBox.getWidth()+2,
                        20,
                        20,
                        20)
                        .build()
        );
        System.out.println(members);
        if (members.isEmpty()) {
            getPartyMembers();
        }




    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(guiGraphics, mouseX, mouseY, delta); // фон
        super.render(guiGraphics, mouseX, mouseY, delta);



        assert this.minecraft != null;
        assert this.minecraft.player != null;
        int claims = claimsManager.getPlayerInfo(this.minecraft.player.getUUID()).getClaimCount();







        String limit = "Чанки: "+claims+"/"+claimsManager.getClaimLimit();;
        String partyName = "Гильдия: "+partyManager.getPartyName();
        String partyOwner = "Владелец: "+partyManager.getParty().getOwner().getUsername();
        String partyMembersCount = "Участников: "+partyManager.getUIMemberCount()+"/"+partyManager.getMemberLimit();
        String partyColor = "Цвет: "+claimsManager.getPlayerInfo(this.minecraft.player.getUUID()).getClaimsColor();


        guiGraphics.drawString(this.font, "СИСТЕМА УПРАВЛЕНИЯ ГИЛЬДИЯМИ", this.width/2, this.height/2, 0xFFFFFF);

        guiGraphics.drawString(this.font, "Имя гильдии", 0, this.textBox.getY()-10, 0xFFFFFF);
        guiGraphics.drawString(this.font, "Цвет гильдии", 0, this.colorBox.getY()-10, 0xFFFFFF);





        guiGraphics.drawString(this.font, partyName,this.width/2, this.height/2 + 20, 0xFFFFFF);
        guiGraphics.drawString(this.font, partyOwner, this.width/2, this.height/2 + 40, 0xFFFFFF);
        guiGraphics.drawString(this.font, partyColor, this.width/2, this.height/2 + 80, 0xFFFFFF);
        guiGraphics.drawString(this.font, limit, this.width/2, this.height/2 + 100, 0xFFFFFF);
        guiGraphics.drawString(this.font, partyMembersCount, this.width/2, this.height/2 + 120, 0xFFFFFF);
        guiGraphics.drawString(this.font, members.toString(), this.width/2, this.height/2 + 140, 0xFFFFFF);


        this.textBox.render(guiGraphics, mouseX, mouseY, delta);
        this.colorBox.render(guiGraphics, mouseX, mouseY, delta);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.textBox.keyPressed(keyCode, scanCode, modifiers) || this.textBox.canConsumeInput()) {
            return true;
        }
        if (this.colorBox.keyPressed(keyCode, scanCode, modifiers) || this.colorBox.canConsumeInput()) {
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }


    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (this.textBox.charTyped(chr, modifiers)) {
            return true;
        }
        if (this.colorBox.charTyped(chr, modifiers)) {
            return true;
        }
        return super.charTyped(chr, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean handled = super.mouseClicked(mouseX, mouseY, button);

        // сбросить фокус у всех
        this.textBox.setFocused(false);
        this.colorBox.setFocused(false);

        // проверить, попал ли клик внутрь editBox
        if (this.textBox.isMouseOver(mouseX, mouseY)) {
            this.textBox.setFocused(true);
        } else if (this.colorBox.isMouseOver(mouseX, mouseY)) {
            this.colorBox.setFocused(true);
        }

        return handled;
    }


    private void onButtonClick() {
        if (this.minecraft != null && this.minecraft.player != null) {
            this.minecraft.player.sendSystemMessage(Component.literal("Кнопка нажата!"));
            String text = this.textBox.getValue();
            String color = this.colorBox.getValue();

            if (minecraft.getConnection() != null) {
                LocalPlayer player = minecraft.player;
                player.closeContainer();
                Network.sendButtonClick("openpac player-config set parties.name "+text); //отправляет пакет, который выполняет команду(аргумент String) от имени игрока
                Network.sendButtonClick("openpac player-config set claims.color "+color);

            }
        }
    }
}
