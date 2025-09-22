package net.artmaster.openpacbp.network;

import net.artmaster.openpacbp.client.PartyManageScreen;
import net.artmaster.openpacbp.network.parties.SyncAllyPartiesPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ClientPacketHandler {
    public static void handleOpenGui() {
        Minecraft.getInstance().setScreen(new PartyManageScreen());
    }

    public static void handleRunCommand(RunCommandPacket packet) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            player.connection.sendCommand(packet.command());
        }
    }

    public static void handleSyncAllyParties(SyncAllyPartiesPacket pkt) {
        Minecraft mc = Minecraft.getInstance();
        mc.execute(() -> {
            if (mc.screen instanceof PartyManageScreen screen) {
                screen.setAllyNames(pkt.allies()); // добавим метод в экран
            }
        });
    }
}
