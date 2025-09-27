package net.artmaster.openpacbp.client;

import net.artmaster.openpacbp.api.gui.ColorButton;
import net.artmaster.openpacbp.api.gui.GuiGroup;
import net.artmaster.openpacbp.network.Network;
import net.artmaster.openpacbp.network.parties.SyncAllyPartiesPacket;
import net.artmaster.openpacbp.utils.TooltipUtil;
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
import xaero.pac.client.parties.party.api.IClientPartyAPI;
import xaero.pac.client.parties.party.api.IClientPartyStorageAPI;
import xaero.pac.common.parties.party.api.IPartyPlayerInfoAPI;
import xaero.pac.common.parties.party.member.api.IPartyMemberAPI;


import java.util.*;


public class PartyManageScreen extends Screen {





    ResourceLocation redButtonTexture = ResourceLocation.fromNamespaceAndPath("openpacbp", "textures/buttons/red_button.png");
    ResourceLocation orangeButtonTexture = ResourceLocation.fromNamespaceAndPath("openpacbp", "textures/buttons/orange_button.png");
    ResourceLocation yellowButtonTexture = ResourceLocation.fromNamespaceAndPath("openpacbp", "textures/buttons/yellow_button.png");
    ResourceLocation greenButtonTexture = ResourceLocation.fromNamespaceAndPath("openpacbp", "textures/buttons/green_button.png");
    ResourceLocation lightBlueButtonTexture = ResourceLocation.fromNamespaceAndPath("openpacbp", "textures/buttons/light_blue_button.png");
    ResourceLocation blueButtonTexture = ResourceLocation.fromNamespaceAndPath("openpacbp", "textures/buttons/blue_button.png");
    ResourceLocation purpleButtonTexture = ResourceLocation.fromNamespaceAndPath("openpacbp", "textures/buttons/purple_button.png");
    ResourceLocation darkGrayButtonTexture = ResourceLocation.fromNamespaceAndPath("openpacbp", "textures/buttons/dark_gray_button.png");
    ResourceLocation grayButtonTexture = ResourceLocation.fromNamespaceAndPath("openpacbp", "textures/buttons/gray_button.png");
    ResourceLocation blackButtonTexture = ResourceLocation.fromNamespaceAndPath("openpacbp", "textures/buttons/black_button.png");



    List<Object> members = new ArrayList<>();

    private final Map<UUID, String> allyNames = new HashMap<>();

    IClientClaimsManagerAPI claimsManager = OpenPACClientAPI.get().getClaimsManager();
    IClientPartyStorageAPI partyManager = OpenPACClientAPI.get().getClientPartyStorage();
    List<IPartyMemberAPI> members_list = Objects.requireNonNull(partyManager.getParty()).getMemberInfoStream().toList();

    protected void getPartyMembers() {
        if (members_list.size() == 1) {
            members.add(partyManager.getParty().getOwner().getUsername() + "[" + partyManager.getParty().getOwner().getRank() + "]");
        } else {
            for (int i = 0; i < members_list.size(); i++) {
                members.add(members_list.get(i).getUsername() + "[" + members_list.get(i).getRank() + "]");
            }
        }
    }



    public void setAllyNames(List<SyncAllyPartiesPacket.AllyData> allies) {
        allyNames.clear();
        for (SyncAllyPartiesPacket.AllyData ally : allies) {
            allyNames.put(ally.partyId(), ally.ownerName());
        }
    }












    private EditBox textBox;
    private EditBox colorBox;
    private EditBox managePlayerBox;
    private EditBox manageAlliesBox;
    public PartyManageScreen() {
        super(Component.translatable("gui.openpacbp.party_manage_screen"));
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
                Component.translatable("gui.openpacbp.party_manage_name_textbox")
        );
        this.colorBox = new EditBox(
                this.font,
                0,
                100,
                200,
                20,
                Component.translatable("gui.openpacbp.party_manage_color_textbox")
        );
        this.manageAlliesBox = new EditBox(
                this.font,
                0,
                160,
                200,
                20,
                Component.translatable("gui.openpacbp.party_manage_allies_textbox")
        );

        this.managePlayerBox = new EditBox(
                this.font,
                0,
                220,
                200,
                20,
                Component.translatable("gui.openpacbp.party_manage_player_textbox")
        );


        this.addRenderableWidget(
                Button.builder(
                                Component.translatable("gui.openpacbp.party_manage_ally_add_button_text"),
                                (btn) -> onAllyAddButtonClick()
                        ).bounds(
                                this.textBox.getX(),
                                this.manageAlliesBox.getY()+20,
                                100,
                                20)
                        .build()
        );
        this.addRenderableWidget(
                Button.builder(
                                Component.translatable("gui.openpacbp.party_manage_ally_remove_button_text"),
                                (btn) -> onAllyRemButtonClick()
                        ).bounds(
                                this.textBox.getX()+100,
                                this.manageAlliesBox.getY()+20,
                                100,
                                20)
                        .build()
        );


        this.addRenderableWidget(
                Button.builder(
                        Component.translatable("gui.openpacbp.party_manage_save_button_text"),
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
                                Component.translatable("gui.openpacbp.party_manage_close_button_text"),
                                (btn) -> onExitButtonClick()
                        ).bounds(
                                this.textBox.getX(),
                                this.managePlayerBox.getY()+45,
                                100,
                                20)
                        .build()
        );

        this.addRenderableWidget(
                Button.builder(
                                Component.translatable("gui.openpacbp.party_manage_invite_button_text"),
                                (btn) -> onInviteButtonClick()
                        ).bounds(
                                this.managePlayerBox.getX(),
                                this.managePlayerBox.getY()+20,
                                75,
                                20)
                        .build()
        );
        this.addRenderableWidget(
                Button.builder(
                                Component.translatable("gui.openpacbp.party_manage_kick_button_text"),
                                (btn) -> onKickButtonClick()
                        ).bounds(
                                this.managePlayerBox.getX()+75,
                                this.managePlayerBox.getY()+20,
                                65,
                                20)
                        .build()
        );
        this.addRenderableWidget(
                Button.builder(
                                Component.translatable("gui.openpacbp.party_manage_button_rank_member"),
                                (btn) -> onRankMemberButtonClick()
                        ).bounds(
                                this.managePlayerBox.getX()+140,
                                this.managePlayerBox.getY()+20,
                                20,
                                20)
                        .build()
        );
        this.addRenderableWidget(
                Button.builder(
                                Component.translatable("gui.openpacbp.party_manage_button_rank_moderator"),
                                (btn) -> onRankModeratorButtonClick()
                        ).bounds(
                                this.managePlayerBox.getX()+160,
                                this.managePlayerBox.getY()+20,
                                20,
                                20)
                        .build()
        );
        this.addRenderableWidget(
                Button.builder(
                                Component.translatable("gui.openpacbp.party_manage_button_rank_admin"),
                                (btn) -> onRankAdminButtonClick()
                        ).bounds(
                                this.managePlayerBox.getX()+180,
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

        this.addRenderableWidget(new ColorButton(
                this.colorBox.getX()+140, this.colorBox.getY()+20, 20, 20,
                grayButtonTexture,
                Component.literal(""),
                btn -> onColorButtonClick("787878")
        ));


        this.addRenderableWidget(new ColorButton(
                this.colorBox.getX()+160, this.colorBox.getY()+20, 20, 20,
                darkGrayButtonTexture,
                Component.literal(""),
                btn -> onColorButtonClick("434343")
        ));

        this.addRenderableWidget(new ColorButton(
                this.colorBox.getX()+180, this.colorBox.getY()+20, 20, 20,
                blackButtonTexture,
                Component.literal(""),
                btn -> onColorButtonClick("101010")
        ));






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
        claimsManager.getPlayerInfo(this.minecraft.player.getUUID()).getClaimsColor();
        int claims = claimsManager.getPlayerInfo(Objects.requireNonNull(partyManager.getParty()).getOwner().getUUID()).getClaimCount();


        if (mouseX > this.managePlayerBox.getX()+180 && mouseX < this.managePlayerBox.getX()+200 && mouseY > this.managePlayerBox.getY()+20 && mouseY < this.managePlayerBox.getY()+40) {
            guiGraphics.renderTooltip(font, Component.translatable("tooltip.openpacbp.admin_rank_button_desc"), mouseX, mouseY);
        }
        if (mouseX > this.managePlayerBox.getX()+160 && mouseX < this.managePlayerBox.getX()+180 && mouseY > this.managePlayerBox.getY()+20 && mouseY < this.managePlayerBox.getY()+40) {
            guiGraphics.renderTooltip(font, Component.translatable("tooltip.openpacbp.moderator_rank_button_desc"), mouseX, mouseY);
        }
        if (mouseX > this.managePlayerBox.getX()+140 && mouseX < this.managePlayerBox.getX()+160 && mouseY > this.managePlayerBox.getY()+20 && mouseY < this.managePlayerBox.getY()+40) {
            guiGraphics.renderTooltip(font, Component.translatable("tooltip.openpacbp.member_rank_button_desc"), mouseX, mouseY);
        }
        if (mouseX > this.managePlayerBox.getX() && mouseX < this.managePlayerBox.getX()+200 && mouseY > this.managePlayerBox.getY() && mouseY < this.managePlayerBox.getY()+20) {
            guiGraphics.renderComponentTooltip(font, TooltipUtil.splitTooltip("tooltip.openpacbp.manage_player_box_desc"), mouseX, mouseY);
        }
        if (mouseX > this.colorBox.getX() && mouseX < this.colorBox.getX()+200 && mouseY > this.colorBox.getY() && mouseY < this.colorBox.getY()+20) {
            guiGraphics.renderComponentTooltip(font, TooltipUtil.splitTooltip("tooltip.openpacbp.color_box_desc"), mouseX, mouseY);
        }
        if (mouseX > this.textBox.getX() && mouseX < this.textBox.getX()+200 && mouseY > this.textBox.getY() && mouseY < this.textBox.getY()+20) {
            guiGraphics.renderComponentTooltip(font, TooltipUtil.splitTooltip("tooltip.openpacbp.text_box_desc"), mouseX, mouseY);
        }
        if (mouseX > this.textBox.getX() && mouseX < this.textBox.getX()+100 && mouseY > this.managePlayerBox.getY()+45 && mouseY < this.managePlayerBox.getY()+65) {
            guiGraphics.renderTooltip(font, Component.translatable("tooltip.openpacbp.leave_button_desc"), mouseX, mouseY);
        }
        if (mouseX > this.textBox.getX()+100 && mouseX < this.textBox.getX()+200 && mouseY > this.managePlayerBox.getY()+45 && mouseY < this.managePlayerBox.getY()+65) {
            guiGraphics.renderTooltip(font, Component.translatable("tooltip.openpacbp.save_button_desc"), mouseX, mouseY);
        }
        if (mouseX > this.manageAlliesBox.getX() && mouseX < this.manageAlliesBox.getX()+200 && mouseY > this.manageAlliesBox.getY() && mouseY < this.manageAlliesBox.getY()+20) {
            guiGraphics.renderComponentTooltip(font, TooltipUtil.splitTooltip("tooltip.openpacbp.manage_allies_box_desc"), mouseX, mouseY);
        }




        Component limit = Component.translatable(
                "gui.openpacbp.party_manage_chunks_text",
                claims, claimsManager.getClaimLimit()
        );

        Component partyName = Component.translatable(
                "gui.openpacbp.party_manage_party_name_text",
                partyManager.getPartyName()
        );

        Component partyOwner = Component.translatable(
                "gui.openpacbp.party_manage_party_owner_text",
                partyManager.getParty().getOwner().getUsername()
        );

//        Component partyMembersCount = Component.translatable(
//                "gui.openpacbp.party_manage_member_count_text",
//                partyManager.getUIMemberCount(),
//                partyManager.getMemberLimit()
//        );
//
//        Component partyAlliesCount = Component.translatable(
//                "gui.openpacbp.party_manage_allies_count_text",
//                partyManager.getUIAllyCount(),
//                partyManager.getAllyLimit()
//        );
//
//        Component partyInvitesCount = Component.translatable(
//                "gui.openpacbp.party_manage_invites_count_text",
//                partyManager.getUIInviteCount(),
//                partyManager.getInviteLimit()
//        );

        Component partyColor = Component.translatable(
                "gui.openpacbp.party_manage_party_color_text",
                String.valueOf(TextColor.fromRgb(claimsManager.getPlayerInfo(this.minecraft.player.getUUID()).getClaimsColor()))
        );


        Component partyManageTitleText = Component.translatable("gui.openpacbp.party_manage_screen_title");

        float bigScale = 2.0f; // 2x больше обычного
        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(bigScale, bigScale, 1.0f);

        int x = (int) (this.textBox.getWidth()*1.655 / 2f / bigScale);
        int y = (int) (20 / bigScale);

        guiGraphics.drawCenteredString(this.font, partyManageTitleText, x, y, 0xFFFFFF);

        guiGraphics.pose().popPose();

        guiGraphics.pose().pushPose();



        guiGraphics.drawString(this.font, Component.translatable("gui.openpacbp.party_manage_name_text_string"), 0, this.textBox.getY()-10, 0xFFFFFF);
        guiGraphics.drawString(this.font, Component.translatable("gui.openpacbp.party_manage_color_text_string"), 0, this.colorBox.getY()-10, 0xFFFFFF);
        guiGraphics.drawString(this.font, Component.translatable("gui.openpacbp.party_manage_allies_actions_text_string"), 0, this.manageAlliesBox.getY()-10, 0xFFFFFF);

        guiGraphics.drawString(this.font, Component.translatable("gui.openpacbp.party_manage_player_actions_text_string"), 0, this.managePlayerBox.getY()-10, 0xFFFFFF);







        guiGraphics.drawString(this.font, partyName,this.textBox.getWidth()+20, this.textBox.getY(), 0xFFFFFF);
        guiGraphics.drawString(this.font, partyOwner, this.textBox.getWidth()+20, this.textBox.getY() + 20, 0xFFFFFF);
        guiGraphics.drawString(this.font, partyColor.copy().withStyle(style -> style.withColor(
                TextColor.fromRgb(
                        claimsManager.getPlayerInfo(this.minecraft.player.getUUID()).getClaimsColor()
                )
        )), this.textBox.getWidth()+20, this.textBox.getY() + 40, 0xFFFFFF);
        guiGraphics.drawString(this.font, limit, this.textBox.getWidth()+20, this.textBox.getY() + 60, 0xFFFFFF);

        //guiGraphics.drawString(this.font, members.toString(), this.textBox.getWidth()+20, this.textBox.getY() + 140, 0xFFFFFF);

        int baseX = this.textBox.getWidth() + 20;
        GuiGroup.MutableInt currentY = new GuiGroup.MutableInt(this.textBox.getY() + 80);

// Создаём блоки
        GuiGroup invitesGroup = new GuiGroup(
                "gui.openpacbp.party_manage_invites_title",
                partyManager.getParty().getInviteCount(),
                partyManager.getInviteLimit(),
                partyManager.getParty().getInvitedPlayersStream().map(IPartyPlayerInfoAPI::getUsername).toList()
        );

        GuiGroup alliesGroup = new GuiGroup(
                "gui.openpacbp.party_manage_allies_title",
                partyManager.getUIAllyCount(),
                partyManager.getAllyLimit(),
                new ArrayList<>(allyNames.values())
        );

        GuiGroup membersGroup = new GuiGroup(
                "gui.openpacbp.party_manage_members_title",
                partyManager.getUIMemberCount(),
                partyManager.getMemberLimit(),
                members.stream().map(Object::toString).toList()
        );

// Рендерим все блоки
        invitesGroup.render(this.font, guiGraphics, baseX, currentY, 0xFFFFFF);
        alliesGroup.render(this.font, guiGraphics, baseX, currentY, 0xFFFFFF);
        membersGroup.render(this.font, guiGraphics, baseX, currentY, 0xFFFFFF);

//// 1. Приглашения
//        guiGraphics.drawString(this.font, partyInvitesCount, baseX, currentY, 0xFFFFFF);
//        currentY += this.font.lineHeight + 2; // добавляем немного пространства
//        List<String> invites = partyManager.getParty().getInvitedPlayersStream()
//                .map(IPartyPlayerInfoAPI::getUsername)
//                .toList();
//        if (!invites.isEmpty()) {
//            guiGraphics.drawString(this.font, Component.translatable("gui.openpacbp.party_manage_invites_title"), baseX, currentY, 0xFFFFFF);
//            currentY += this.font.lineHeight + 2;
//            for (String invite : invites) {
//                guiGraphics.drawString(this.font, Component.literal(invite), baseX, currentY, 0xFFFFFF);
//                currentY += this.font.lineHeight + 2;
//            }
//            currentY += 4; // небольшой отступ после списка
//        }
//
//// 2. Союзники
//        guiGraphics.drawString(this.font, partyAlliesCount, baseX, currentY, 0xFFFFFF);
//        currentY += this.font.lineHeight + 2;
//
//        if (!allyNames.isEmpty()) {
//            guiGraphics.drawString(this.font, Component.translatable("gui.openpacbp.party_manage_allies_title"), baseX, currentY, 0xFFFFFF);
//            currentY += this.font.lineHeight + 2;
//            for (String ownerName : allyNames.values()) {
//                guiGraphics.drawString(this.font, Component.literal(ownerName), baseX, currentY, 0xFFFFFF);
//                currentY += this.font.lineHeight + 2;
//            }
//            currentY += 4; // небольшой отступ после списка
//        }
//
//// 3. Участники
//        guiGraphics.drawString(this.font, partyMembersCount, baseX, currentY, 0xFFFFFF);
//        currentY += this.font.lineHeight + 2;
//
//        if (!members.isEmpty()) {
//            guiGraphics.drawString(this.font, Component.translatable("gui.openpacbp.party_manage_members_title"), baseX, currentY, 0xFFFFFF);
//            currentY += this.font.lineHeight + 2;
//            for (Object member : members) {
//                guiGraphics.drawString(this.font, Component.literal(member.toString()), baseX, currentY, 0xFFFFFF);
//                currentY += this.font.lineHeight + 2;
//            }
//        }







        this.textBox.render(guiGraphics, mouseX, mouseY, delta);
        this.colorBox.render(guiGraphics, mouseX, mouseY, delta);
        this.managePlayerBox.render(guiGraphics, mouseX, mouseY, delta);
        this.manageAlliesBox.render(guiGraphics, mouseX, mouseY, delta);
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

        if (this.manageAlliesBox.isFocused() && keyCode == 258) {
            String current = this.manageAlliesBox.getValue();
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
                        this.manageAlliesBox.setValue(matches.get(0));
                    } else {
                        this.manageAlliesBox.setValue(matches.get(0));
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
        if (this.manageAlliesBox.keyPressed(keyCode, scanCode, modifiers) || this.manageAlliesBox.canConsumeInput()) {
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
        if (this.manageAlliesBox.charTyped(chr, modifiers)) {
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
        this.manageAlliesBox.setFocused(false);

        if (this.textBox.isMouseOver(mouseX, mouseY)) {
            this.textBox.setFocused(true);
        } else if (this.colorBox.isMouseOver(mouseX, mouseY)) {
            this.colorBox.setFocused(true);
        } else if (this.managePlayerBox.isMouseOver(mouseX, mouseY)) {
            this.managePlayerBox.setFocused(true);
        } else if (this.manageAlliesBox.isMouseOver(mouseX, mouseY)) {
            this.manageAlliesBox.setFocused(true);
        }

        return handled;
    }


    private void onButtonClick() {
        if (this.minecraft != null && this.minecraft.player != null) {
            String text = this.textBox.getValue();
            String color = this.colorBox.getValue();

            if (minecraft.getConnection() != null) {


                IClientPartyAPI clientPartyAPI = OpenPACClientAPI.get().getClientPartyStorage().getParty();



                if (clientPartyAPI == null) return;

                if (!this.textBox.getValue().isEmpty()) {
                    if (!clientPartyAPI.getOwner().getUUID().equals(this.minecraft.player.getUUID())) {
                        this.minecraft.player.closeContainer();
                        this.minecraft.player.sendSystemMessage(Component.translatable("text.openpacbp.player_not_owner"));
                        return;
                    }
                    Network.sendButtonClick("openpac player-config set parties.name "+text);
                    Network.sendButtonClick("openpac player-config set claims.name "+text);
                }
                if (!this.colorBox.getValue().isEmpty()) {
                    if (!clientPartyAPI.getOwner().getUUID().equals(this.minecraft.player.getUUID())) {
                        this.minecraft.player.closeContainer();
                        this.minecraft.player.sendSystemMessage(Component.translatable("text.openpacbp.player_not_owner"));
                        return;
                    }
                    Network.sendButtonClick("openpac player-config set claims.color "+color);
                }
                if (!this.textBox.getValue().isEmpty() || !this.colorBox.getValue().isEmpty()) {
                    this.minecraft.player.sendSystemMessage(Component.translatable("text.openpacbp.changes_applied"));
                }
            }
        }
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






    private void onRankMemberButtonClick() {
        if (this.minecraft != null && this.minecraft.player != null) {
            if (!this.managePlayerBox.getValue().isEmpty()) {
                String text = this.managePlayerBox.getValue();
                Network.sendButtonClick("/openpac-parties member rank MEMBER " + text);
            }
        }
    }
    private void onRankModeratorButtonClick() {
        if (this.minecraft != null && this.minecraft.player != null) {
            if (!this.managePlayerBox.getValue().isEmpty()) {
                String text = this.managePlayerBox.getValue();
                Network.sendButtonClick("/openpac-parties member rank MODERATOR " + text);
            }
        }
    }
    private void onRankAdminButtonClick() {
        if (this.minecraft != null && this.minecraft.player != null) {
            if (!this.managePlayerBox.getValue().isEmpty()) {
                String text = this.managePlayerBox.getValue();
                Network.sendButtonClick("/openpac-parties member rank ADMIN " + text);
            }
        }
    }
    private void onAllyAddButtonClick() {
        if (this.minecraft != null && this.minecraft.player != null) {
            if (!this.manageAlliesBox.getValue().isEmpty()) {
                String text = this.manageAlliesBox.getValue();
                Network.sendButtonClick("/openpac-parties ally add " + text);
            }
        }
    }
    private void onAllyRemButtonClick() {
        if (this.minecraft != null && this.minecraft.player != null) {
            if (!this.manageAlliesBox.getValue().isEmpty()) {
                String text = this.manageAlliesBox.getValue();
                Network.sendButtonClick("/openpac-parties ally remove " + text);
            }
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
                    this.minecraft.player.sendSystemMessage(Component.translatable(
                            "text.openpacbp.player_already_in_party",
                            this.managePlayerBox.getValue()
                    ));
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
                    this.minecraft.player.sendSystemMessage(Component.translatable(
                            "text.openpacbp.player_not_in_party",
                            this.managePlayerBox.getValue()
                    ));
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
