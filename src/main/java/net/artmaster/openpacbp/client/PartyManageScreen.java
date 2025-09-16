package net.artmaster.openpacbp.client;

import net.artmaster.openpacbp.api.gui.ColorButton;
import net.artmaster.openpacbp.network.Network;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import xaero.pac.client.api.OpenPACClientAPI;
import xaero.pac.client.claims.api.IClientClaimsManagerAPI;
import xaero.pac.client.parties.party.api.IClientPartyStorageAPI;
import xaero.pac.common.parties.party.ally.api.IPartyAllyAPI;
import xaero.pac.common.parties.party.api.IPartyPlayerInfoAPI;
import xaero.pac.common.parties.party.member.api.IPartyMemberAPI;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class PartyManageScreen extends Screen {


    Minecraft mc = Minecraft.getInstance();



    ResourceLocation redButtonTexture = ResourceLocation.fromNamespaceAndPath("openpacbp", "textures/buttons/red_button.png");
    ResourceLocation orangeButtonTexture = ResourceLocation.fromNamespaceAndPath("openpacbp", "textures/buttons/orange_button.png");
    ResourceLocation yellowButtonTexture = ResourceLocation.fromNamespaceAndPath("openpacbp", "textures/buttons/yellow_button.png");
    ResourceLocation greenButtonTexture = ResourceLocation.fromNamespaceAndPath("openpacbp", "textures/buttons/green_button.png");
    ResourceLocation lightBlueButtonTexture = ResourceLocation.fromNamespaceAndPath("openpacbp", "textures/buttons/light_blue_button.png");
    ResourceLocation blueButtonTexture = ResourceLocation.fromNamespaceAndPath("openpacbp", "textures/buttons/blue_button.png");
    ResourceLocation purpleButtonTexture = ResourceLocation.fromNamespaceAndPath("openpacbp", "textures/buttons/purple_button.png");

    List<Object> members = new ArrayList<>();
    List<Object> allies = new ArrayList<>();
    List<Object> invites = new ArrayList<>();

    IClientClaimsManagerAPI claimsManager = OpenPACClientAPI.get().getClaimsManager();
    IClientPartyStorageAPI partyManager = OpenPACClientAPI.get().getClientPartyStorage();
    List<IPartyMemberAPI> members_list = Objects.requireNonNull(partyManager.getParty()).getMemberInfoStream().toList();
    List<IPartyAllyAPI> allies_list = Objects.requireNonNull(partyManager.getParty()).getAllyPartiesStream().toList();
    List<IPartyPlayerInfoAPI> invites_list = Objects.requireNonNull(partyManager.getParty()).getInvitedPlayersStream().toList();

    protected void getPartyMembers() { //вот тут что-то не так
        if (members_list.size() == 1) {
            members.add(partyManager.getParty().getOwner().getUsername()+"["+partyManager.getParty().getOwner().getRank()+"]");
        } else {
            for (int i=0; i < members_list.size(); i++) {
                members.add(members_list.get(i).getUsername()+"["+members_list.get(i).getRank()+"]");
            }
        }
    }
    protected void getPartyAllies() {
        for (int i=0; i< allies_list.size(); i++) {
            allies.add(allies_list.get(i).getPartyId());
        }
    }
    protected void getPartyInvites() {
        for (int i=0; i< invites_list.size(); i++) {
            invites.add(invites_list.get(i).getUsername());
        }
    }












    private EditBox textBox;
    private EditBox colorBox;
    private EditBox managePlayerBox;
    public PartyManageScreen() {
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
                60,
                200,
                20,
                Component.literal("Имя гильдии")
        );
        this.colorBox = new EditBox(
                this.font,
                0,
                100,
                200,
                20,
                Component.literal("Цвет гильдии")
        );
        this.managePlayerBox = new EditBox(
                this.font,
                0,
                160,
                200,
                20,
                Component.literal("Выбор игрока")
        );
        this.addRenderableWidget(
                Button.builder(
                        Component.literal("Сохранить"),
                        (btn) -> onButtonClick()
                        ).bounds(
                        this.textBox.getX()+100,
                        this.managePlayerBox.getY()+45,
                        this.textBox.getWidth()/2,
                        20)
                        .build()
        );
        this.addRenderableWidget(
                Button.builder(
                                Component.literal("Выйти"),
                                (btn) -> onExitButtonClick()
                        ).bounds(
                                this.textBox.getX(),
                                this.managePlayerBox.getY()+45,
                                this.textBox.getWidth()/2,
                                20)
                        .build()
        );
        this.addRenderableWidget(
                Button.builder(
                                Component.literal("Пригласить"),
                                (btn) -> onInviteButtonClick()
                        ).bounds(
                                this.managePlayerBox.getX(),
                                this.managePlayerBox.getY()+20,
                                65,
                                20)
                        .build()
        );
        this.addRenderableWidget(
                Button.builder(
                                Component.literal("Кикнуть"),
                                (btn) -> onKickButtonClick()
                        ).bounds(
                                this.managePlayerBox.getX()+65,
                                this.managePlayerBox.getY()+20,
                                65,
                                20)
                        .build()
        );
        this.addRenderableWidget(
                Button.builder(
                                Component.literal("У"),
                                (btn) -> onButtonClick()
                        ).bounds(
                                this.managePlayerBox.getX()+130,
                                this.managePlayerBox.getY()+20,
                                20,
                                20)
                        .build()
        );
        this.addRenderableWidget(
                Button.builder(
                                Component.literal("М"),
                                (btn) -> onButtonClick()
                        ).bounds(
                                this.managePlayerBox.getX()+150,
                                this.managePlayerBox.getY()+20,
                                20,
                                20)
                        .build()
        );
        this.addRenderableWidget(
                Button.builder(
                                Component.literal("А"),
                                (btn) -> onButtonClick()
                        ).bounds(
                                this.managePlayerBox.getX()+170,
                                this.managePlayerBox.getY()+20,
                                20,
                                20)
                        .build()
        );
        //red
        this.addRenderableWidget(new ColorButton(
                this.colorBox.getX(), this.colorBox.getY()+20, 20, 20,
                redButtonTexture,
                Component.literal(""),
                btn -> onColorButtonClick("ff484f")
        ));
        //orange
        this.addRenderableWidget(new ColorButton(
                this.colorBox.getX()+20, this.colorBox.getY()+20, 20, 20,
                orangeButtonTexture,
                Component.literal(""),
                btn -> onColorButtonClick("c07830")
        ));
        //yellow
        this.addRenderableWidget(new ColorButton(
                this.colorBox.getX()+40, this.colorBox.getY()+20, 20, 20,
                yellowButtonTexture,
                Component.literal(""),
                btn -> onColorButtonClick("c2a932")
        ));
        //green
        this.addRenderableWidget(new ColorButton(
                this.colorBox.getX()+60, this.colorBox.getY()+20, 20, 20,
                greenButtonTexture,
                Component.literal(""),
                btn -> onColorButtonClick("3ac131")
        ));
        //light-blue
        this.addRenderableWidget(new ColorButton(
                this.colorBox.getX()+80, this.colorBox.getY()+20, 20, 20,
                lightBlueButtonTexture,
                Component.literal(""),
                btn -> onColorButtonClick("339cc3")
        ));
        //blue
        this.addRenderableWidget(new ColorButton(
                this.colorBox.getX()+100, this.colorBox.getY()+20, 20, 20,
                blueButtonTexture,
                Component.literal(""),
                btn -> onColorButtonClick("316fc1")
        ));
        //purple
        this.addRenderableWidget(new ColorButton(
                this.colorBox.getX()+120, this.colorBox.getY()+20, 20, 20,
                purpleButtonTexture,
                Component.literal(""),
                btn -> onColorButtonClick("6a2e6c")
        ));


        this.addRenderableWidget(
                Button.builder(
                                Component.literal("Квесты"),
                                (btn) -> onQuestMenuOpen()
                        ).bounds(
                                this.managePlayerBox.getX()+130,
                                this.managePlayerBox.getY()+80,
                                50,
                                20)
                        .build()
        );



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
        String partyAlliesCount = "Союзников: "+partyManager.getUIAllyCount()+"/"+partyManager.getAllyLimit();
        String partyInvitesCount = "Приглашений: "+partyManager.getUIInviteCount()+"/"+partyManager.getInviteLimit();
        Component partyColor = Component.literal("Цвет: ").withStyle(ChatFormatting.WHITE)
                .append(Component.literal(""+TextColor.fromRgb(claimsManager.getPlayerInfo(this.minecraft.player.getUUID()).getClaimsColor())).withStyle(style -> style.withColor(TextColor.fromRgb(claimsManager.getPlayerInfo(this.minecraft.player.getUUID()).getClaimsColor())))
                );



        float bigScale = 2.0f; // 2x больше обычного
        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(bigScale, bigScale, 1.0f);

        int x = (int) (this.textBox.getWidth()*1.655 / 2f / bigScale);
        int y = (int) (20 / bigScale);

        guiGraphics.drawCenteredString(this.font, "СИСТЕМА УПРАВЛЕНИЯ ГИЛЬДИЯМИ", x, y, 0xFFFFFF);

        guiGraphics.pose().popPose();

        guiGraphics.pose().pushPose();







        guiGraphics.drawString(this.font, "Имя гильдии", 0, this.textBox.getY()-10, 0xFFFFFF);
        guiGraphics.drawString(this.font, "Цвет гильдии", 0, this.colorBox.getY()-10, 0xFFFFFF);
        guiGraphics.drawString(this.font, "Действия с игроком", 0, this.managePlayerBox.getY()-10, 0xFFFFFF);





        guiGraphics.drawString(this.font, partyName,this.textBox.getWidth()+20, this.textBox.getY(), 0xFFFFFF);
        guiGraphics.drawString(this.font, partyOwner, this.textBox.getWidth()+20, this.textBox.getY() + 20, 0xFFFFFF);
        guiGraphics.drawString(this.font, partyColor, this.textBox.getWidth()+20, this.textBox.getY() + 40, 0xFFFFFF);
        guiGraphics.drawString(this.font, limit, this.textBox.getWidth()+20, this.textBox.getY() + 60, 0xFFFFFF);
        guiGraphics.drawString(this.font, partyInvitesCount, this.textBox.getWidth()+20, this.textBox.getY() + 80, 0xFFFFFF);
        guiGraphics.drawString(this.font, partyAlliesCount, this.textBox.getWidth()+20, this.textBox.getY() + 100, 0xFFFFFF);
        guiGraphics.drawString(this.font, partyMembersCount, this.textBox.getWidth()+20, this.textBox.getY() + 120, 0xFFFFFF);
        guiGraphics.drawString(this.font, members.toString(), this.textBox.getWidth()+20, this.textBox.getY() + 140, 0xFFFFFF);


        this.textBox.render(guiGraphics, mouseX, mouseY, delta);
        this.colorBox.render(guiGraphics, mouseX, mouseY, delta);
        this.managePlayerBox.render(guiGraphics, mouseX, mouseY, delta);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {

        if (this.managePlayerBox.isFocused() && keyCode == 258) {
            String current = this.managePlayerBox.getValue();
            if (this.minecraft != null && this.minecraft.player != null && this.minecraft.getConnection() != null) {
                var connection = this.minecraft.getConnection();
                List<String> players = connection.getOnlinePlayers().stream()
                        .map(info -> info.getProfile().getName())
                        .toList();

                List<String> matches = players.stream()
                        .filter(name -> name.toLowerCase().startsWith(current.toLowerCase()))
                        .toList();

                if (!matches.isEmpty()) {
                    if (matches.size() == 1) {
                        this.managePlayerBox.setValue(matches.get(0));
                    } else {
                        this.managePlayerBox.setValue(matches.get(0));
                    }
                }
            }
            return true;
        }


        if (this.textBox.keyPressed(keyCode, scanCode, modifiers) || this.textBox.canConsumeInput()) {
            return true;
        }
        if (this.colorBox.keyPressed(keyCode, scanCode, modifiers) || this.colorBox.canConsumeInput()) {
            return true;
        }
        if (this.managePlayerBox.keyPressed(keyCode, scanCode, modifiers) || this.managePlayerBox.canConsumeInput()) {
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
        if (this.managePlayerBox.charTyped(chr, modifiers)) {
            return true;
        }
        return super.charTyped(chr, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean handled = super.mouseClicked(mouseX, mouseY, button);

        this.textBox.setFocused(false);
        this.colorBox.setFocused(false);
        this.managePlayerBox.setFocused(false);

        if (this.textBox.isMouseOver(mouseX, mouseY)) {
            this.textBox.setFocused(true);
        } else if (this.colorBox.isMouseOver(mouseX, mouseY)) {
            this.colorBox.setFocused(true);
        } else if (this.managePlayerBox.isMouseOver(mouseX, mouseY)) {
            this.managePlayerBox.setFocused(true);
        }

        return handled;
    }


    private void onButtonClick() {
        if (this.minecraft != null && this.minecraft.player != null) {
            String text = this.textBox.getValue();
            String color = this.colorBox.getValue();

            if (minecraft.getConnection() != null) {
                LocalPlayer player = minecraft.player;



                if (!this.textBox.getValue().isEmpty()) {
                    Network.sendButtonClick("openpac player-config set parties.name "+text);
                    Network.sendButtonClick("openpac player-config set claims.name "+text);
                    //player.closeContainer();
                }
                if (!this.colorBox.getValue().isEmpty()) {
                    Network.sendButtonClick("openpac player-config set claims.color "+color);
                    //player.closeContainer();
                }
                if (!this.textBox.getValue().isEmpty() || !this.colorBox.getValue().isEmpty()) {
                    this.minecraft.player.sendSystemMessage(Component.literal("Изменения применены!"));
                }
            }
        }
    }

    private void onQuestMenuOpen() {
        Network.sendButtonClick("openpac-quests");
    }

    private void onExitButtonClick() {
        if (this.minecraft != null && this.minecraft.player != null) {

            if (minecraft.getConnection() != null) {
                LocalPlayer player = minecraft.player;
                player.closeContainer();
            }
        }
    }


    private void onColorButtonClick(String color) {
        if (this.minecraft != null && this.minecraft.player != null) {
            this.colorBox.setValue(color);
            this.colorBox.setFocused(true);
        }
    }
    private void onInviteButtonClick() {
        if (this.minecraft != null && this.minecraft.player != null) {
            if (minecraft.getConnection() != null) {
                LocalPlayer player = minecraft.player;
                if (this.managePlayerBox.getValue().isEmpty()) {
                    return;
                }
                if (!members.toString().contains(this.textBox.getValue())) {
                    player.closeContainer();
                    Network.sendButtonClick("openpac-parties member invite "+this.managePlayerBox.getValue()); //отправляет пакет, который выполняет команду(аргумент String) от имени игрока

                } else if (members.toString().contains(this.textBox.getValue())) {
                    player.closeContainer();
                    this.minecraft.player.sendSystemMessage(Component.literal("Игрок "+this.managePlayerBox.getValue()+" уже в нашей гильдии!"));
                }

            }
        }
    }
    private void onKickButtonClick() {
        if (this.minecraft != null && this.minecraft.player != null) {
            if (minecraft.getConnection() != null) {
                LocalPlayer player = minecraft.player;
                if (this.managePlayerBox.getValue().isEmpty()) {
                    return;
                }
                if (members.toString().contains(this.textBox.getValue())) {
                    player.closeContainer();
                    Network.sendButtonClick("openpac-parties member kick "+this.managePlayerBox.getValue()); //отправляет пакет, который выполняет команду(аргумент String) от имени игрока

                } else {
                    player.closeContainer();
                    this.minecraft.player.sendSystemMessage(Component.literal("Игрока "+this.managePlayerBox.getValue()+" нет в нашей гильдии!"));
                }
            }
        }
    }
}

// вызываем отрисовку миникарты
//        XaeroMinimapSession session = XaeroMinimapSession.getCurrentSession();
//        if (session == null) return;
//
//        MinimapProcessor processor = session.getMinimapProcessor();
//        if (processor == null) return;
//
//        int size = processor.getMinimapSize();
//        int boxSize = size;
//        int width = this.width;
//        int height = this.height;
//        double scale = 0.75;
//
//        CustomVertexConsumers cvc = new CustomVertexConsumers();
//
//
//        processor.onRender(
//                guiGraphics,
//                this.textBox.getWidth()*5, -100,
//                width, height,
//                scale,
//                size, boxSize,
//                delta,
//                cvc
//        );
//
//        guiGraphics.pose().popPose();
//// переносим "камеру"
////        guiGraphics.pose().translate(50, 50, 0); // x=50, y=50
////        guiGraphics.pose().scale(0.5f, 0.5f, 1.0f); // уменьшаем карту в 2 раза
