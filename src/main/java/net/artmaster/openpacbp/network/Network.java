package net.artmaster.openpacbp.network;

import net.artmaster.openpacbp.ModMain;

import net.artmaster.openpacbp.network.parties.SyncAllyPartiesPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import xaero.pac.common.server.api.OpenPACServerAPI;
import xaero.pac.common.server.parties.party.api.IServerPartyAPI;

import java.util.ArrayList;
import java.util.List;


@Mod("openpacbp")
@EventBusSubscriber(modid = ModMain.MODID)
public class Network {


    public static PayloadRegistrar registrarClient;
    public static PayloadRegistrar registrarServer;

    // Регистрация пакетов
    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        registrarClient = event.registrar("openpacbp");
        registrarServer = event.registrar("openpacbp");



        // -------------------- Клиентские пакеты (сервер -> клиент) --------------------


        registrarServer.playToClient(
                OpenGuiPacket.TYPE,
                OpenGuiPacket.CODEC,
                (packet, ctx) -> ctx.enqueueWork(ClientPacketHandler::handleOpenGui)
        );

        registrarServer.playToClient(
                RunCommandPacket.TYPE,
                RunCommandPacket.CODEC,
                (packet, ctx) -> ctx.enqueueWork(() -> ClientPacketHandler.handleRunCommand(packet))
        );

        registrarServer.playToClient(
                SyncAllyPartiesPacket.TYPE,
                SyncAllyPartiesPacket.CODEC,
                (packet, ctx) -> ctx.enqueueWork(() -> ClientPacketHandler.handleSyncAllyParties(packet))
        );




        // -------------------- Серверные пакеты (клиент -> сервер) --------------------
        registrarClient.playToServer(
                ButtonClickPacket.TYPE,
                ButtonClickPacket.CODEC,
                (packet, ctx) -> ctx.enqueueWork(() -> {
                    ServerPlayer player = (ServerPlayer) ctx.player();
                    sendCommand(player, packet.command());
                })
        );

    }






    // Отправка GUI (сервер -> клиент)
    public static void sendOpenGui(ServerPlayer player) {
        player.connection.send(new OpenGuiPacket());
    }

    // Отправка команды (сервер -> клиент)
    public static void sendCommand(ServerPlayer player, String command) {
        player.server.getCommands().performPrefixedCommand(
                player.createCommandSourceStack(),
                command
        );
    }



    // Отправка нажатия кнопки (клиент -> сервер)
    public static void sendButtonClick(String command) {
        Minecraft.getInstance().getConnection().send(new ButtonClickPacket(command));
    }


    public static void sendAllyParties(ServerPlayer player, IServerPartyAPI party) {
        List<SyncAllyPartiesPacket.AllyData> allies = new ArrayList<>();
        party.getAllyPartiesStream().forEach(ally -> {
            IServerPartyAPI allyParty = OpenPACServerAPI.get(player.server).getPartyManager().getPartyById(ally.getPartyId());

            if (allyParty != null) {
                allies.add(new SyncAllyPartiesPacket.AllyData(ally.getPartyId(), allyParty.getOwner().getUsername()));
            }
        });
        player.connection.send(new SyncAllyPartiesPacket(allies));
    }
}
