package net.artmaster.openpacbp.network;

import net.artmaster.openpacbp.client.GlobalTradesScreen;
import net.artmaster.openpacbp.client.PartyManageScreen;
import net.artmaster.openpacbp.network.parties.SyncPartiesPacket;
//import net.artmaster.openpacbp.network.parties.party_color.PartyColorResponsePacket;
//import net.artmaster.openpacbp.network.parties.party_name.PartyNameResponsePacket;
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

//    public static void handlePartyName(PartyNameResponsePacket packet) {
//        Network.setPartyName(packet.partyName()); // или напрямую обновляй кэш
//    }
//
//    public static void handlePartyColor(PartyColorResponsePacket packet) {
//        Network.setPartyColor(packet.partyColor());
//    }

    public static void handleSyncParties(SyncPartiesPacket packet) {
        Minecraft mc = Minecraft.getInstance();
        mc.execute(() -> {
            if (mc.screen instanceof GlobalTradesScreen screen) {
                screen.setParties(packet.parties());
            }
        });
    }
}
