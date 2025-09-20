package net.artmaster.openpacbp.network;

import net.artmaster.openpacbp.ModMain;
import net.artmaster.openpacbp.api.trades.GlobalStorageData;
import net.artmaster.openpacbp.api.trades.PartyInventoryData;
import net.artmaster.openpacbp.api.trades.StorageManager;
import net.artmaster.openpacbp.gui.GlobalTradesMenu;
import net.artmaster.openpacbp.network.parties.RequestAllPartiesPacket;
import net.artmaster.openpacbp.network.parties.SyncPartiesPacket;
//import net.artmaster.openpacbp.network.parties.party_color.GetPartyColorClientPacket;
//import net.artmaster.openpacbp.network.parties.party_color.PartyColorResponsePacket;
//import net.artmaster.openpacbp.network.parties.party_name.GetPartyNameClientPacket;
//import net.artmaster.openpacbp.network.parties.party_name.PartyNameResponsePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static net.artmaster.openpacbp.api.trades.StorageManager.getGlobalStorage;


@Mod("openpacbp")
@EventBusSubscriber(modid = ModMain.MODID)
public class Network {

    // StreamCodec для OpenGuiPacket
    public static final StreamCodec<RegistryFriendlyByteBuf, OpenGuiPacket> CODEC =
            new StreamCodec<>() {
                @Override
                public OpenGuiPacket decode(RegistryFriendlyByteBuf buf) {
                    return new OpenGuiPacket();
                }

                @Override
                public void encode(RegistryFriendlyByteBuf buf, OpenGuiPacket packet) {
                    // пусто, пакет не содержит данных
                }
            };

    // StreamCodec для ClientCommandPacket
    public static final StreamCodec<RegistryFriendlyByteBuf, ButtonClickPacket> COMMAND_CODEC =
            StreamCodec.of(
                    (buf, packet) -> buf.writeUtf(packet.command()),
                    buf -> new ButtonClickPacket(buf.readUtf())
            );





    public static PayloadRegistrar registrarClient;
    public static PayloadRegistrar registrarServer;

    // Регистрация пакетов
    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        registrarClient = event.registrar("openpacbp");
        registrarServer = event.registrar("openpacbp");



        // -------------------- Клиентские пакеты (сервер -> клиент) --------------------

        registrarServer.playToClient(
                SyncPartiesPacket.TYPE,
                SyncPartiesPacket.CODEC,
                (packet, ctx) -> ctx.enqueueWork(() -> ClientPacketHandler.handleSyncParties(packet))
        );

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

//        registrarServer.playToClient(
//                PartyNameResponsePacket.TYPE,
//                PartyNameResponsePacket.CODEC,
//                (packet, ctx) -> ctx.enqueueWork(() -> ClientPacketHandler.handlePartyName(packet))
//        );
//
//        registrarServer.playToClient(
//                PartyColorResponsePacket.TYPE,
//                PartyColorResponsePacket.CODEC,
//                (packet, ctx) -> ctx.enqueueWork(() -> ClientPacketHandler.handlePartyColor(packet))
//        );

        registrarClient.playToServer(
                BuyItemPacket.TYPE,
                BuyItemPacket.CODEC,
                (packet, ctx) -> ctx.enqueueWork(() -> {
                    ServerPlayer player = (ServerPlayer) ctx.player();
                    if (player.containerMenu instanceof GlobalTradesMenu menu) {
                        if (menu.getAllParties().getTotalParties() == 0) {
                            return;
                        }
                        menu.buyItem(packet.requiredSlot(), packet.resultSlot(), player);
                    }
                })
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

//        registrarClient.playToServer(
//                GetPartyNameClientPacket.TYPE,
//                GetPartyNameClientPacket.CODEC,
//                (packet, ctx) -> ctx.enqueueWork(() -> {
//                    ServerPlayer player = (ServerPlayer) ctx.player();
//                    var inv = getGlobalStorage(player.server, player.getUUID());
//                    var partyName = inv.getAll().values().stream().toList().get(packet.partyIndex()).getPartyName();
//                    ctx.reply(new PartyNameResponsePacket(partyName));
//                })
//        );
//
//        registrarClient.playToServer(
//                GetPartyColorClientPacket.TYPE,
//                GetPartyColorClientPacket.CODEC,
//                (packet, ctx) -> ctx.enqueueWork(() -> {
//                    ServerPlayer player = (ServerPlayer) ctx.player();
//                    var inv = getGlobalStorage(player.server, player.getUUID());
//                    var partyColor = inv.getAll().values().stream().toList().get(packet.partyIndex()).getPartyColor();
//                    ctx.reply(new PartyColorResponsePacket(partyColor));
//                })
//        );

        registrarClient.playToServer(
                RequestAllPartiesPacket.TYPE,
                RequestAllPartiesPacket.CODEC,
                (reqPacket, ctx) -> ctx.enqueueWork(() -> {
                    ServerPlayer player = (ServerPlayer) ctx.player();
                    if (player == null) return;

                    // Формируем пакет
                    List<SyncPartiesPacket.PartyData> packetData = new ArrayList<>();
                    // Здесь нужно **заполнить packetData из storage**, иначе клиент получит пустой список
                    GlobalStorageData storage = StorageManager.getGlobalStorage(player.server, player.getUUID());
                    if (storage != null) {
                        var provider = player.server.registryAccess();
                        for (var entry : storage.getAll().entrySet()) {
                            UUID partyId = entry.getKey();
                            PartyInventoryData inv = entry.getValue();
                            List<ItemStack> items = new ArrayList<>();
                            for (int i = 0; i < inv.getContainer().getContainerSize(); i++) {
                                items.add(inv.getContainer().getItem(i).copy());
                            }
                            packetData.add(new SyncPartiesPacket.PartyData(
                                    partyId,
                                    inv.getPartyName(),
                                    inv.getPartyColor(),
                                    items,
                                    provider
                            ));
                        }
                    }

                    SyncPartiesPacket syncPacket = new SyncPartiesPacket(packetData);

                    // Отправляем клиенту
                    ctx.reply(syncPacket);
                })
        );






//        registrar.playToServer(QuestButtonClickPacket.TYPE, QuestButtonClickPacket.CODEC, (packet, ctx) -> {
//            ctx.enqueueWork(() -> {
//                ServerPlayer player = (ServerPlayer) ctx.player();
//                if (player != null) {
//                    player.server.getCommands().performPrefixedCommand(
//                            player.createCommandSourceStack(),
//                            packet.command()
//                    );
//                    sendCommand(player, packet.command());
//                }
//            });
//        });
    }

    public static void sendAllParties(ServerPlayer player) {
        GlobalStorageData storage = StorageManager.getGlobalStorage(player.server, player.getUUID());
        if (storage == null) return;

        List<SyncPartiesPacket.PartyData> packetData = new ArrayList<>();
        var provider = player.server.registryAccess();

        for (var entry : storage.getAll().entrySet()) {
            UUID partyId = entry.getKey();
            PartyInventoryData inv = entry.getValue();
            List<ItemStack> items = new ArrayList<>();
            for (int i = 0; i < SyncPartiesPacket.TRADE_SLOTS; i++) {
                items.add(inv.getContainer().getItem(i).copy());
            }
            packetData.add(new SyncPartiesPacket.PartyData(
                    partyId,
                    inv.getPartyName(),
                    inv.getPartyColor(),
                    items,
                    provider
            ));
        }

        // Отправка клиенту
        SyncPartiesPacket packet = new SyncPartiesPacket(packetData);
        player.connection.send(packet); // <--- не регистрируем, а **отправляем**
    }



    // Отправка GUI (сервер -> клиент)
    public static void sendOpenGui(ServerPlayer player) {
        player.connection.send(new OpenGuiPacket());
    }

    // Отправка команды (сервер -> клиент)
    public static void sendCommand(ServerPlayer player, String command) {
        //player.connection.send(new RunCommandPacket(command));
        player.server.getCommands().performPrefixedCommand(
                player.createCommandSourceStack(),
                command
        );
    }
    // Отправка пати (сервер -> клиент)
//    public static void sendParty(IClientPartyAPI partyAPI, ServerPlayer player) {
//        player.connection.send(new ResponseDataPacket(partyAPI));
//    }


    // Отправка нажатия кнопки (клиент -> сервер)
    public static void sendButtonClick(String command) {
        Minecraft.getInstance().getConnection().send(new ButtonClickPacket(command));
    }


    public static void buyItem(int page, int requiredSlot, int resultSlot) {
        Minecraft.getInstance().getConnection().send(new BuyItemPacket(page, requiredSlot, resultSlot));
    }





//    private static String cachedPartyName = "";
//    private static int cachedPartyColor = 0xFFFFFF;
//
//    // Вызывается при старте GUI, чтобы запросить имя
//    public static void requestPartyName(int partyIndex) {
//        Minecraft.getInstance().getConnection().send(new GetPartyNameClientPacket(partyIndex));
//    }
//
//    // Вызывается на клиенте, когда пришёл ответ
//    public static void setPartyName(String name) {
//        cachedPartyName = name;
//    }
//
//    // Просто возвращает последнюю известную строку
//
//
//    public static void requestPartyColor(int partyIndex) {
//        Minecraft.getInstance().getConnection().send(new GetPartyColorClientPacket(partyIndex));
//    }
//
//    // Вызывается на клиенте, когда пришёл ответ
//    public static void setPartyColor(int color) {
//        cachedPartyColor = color;
//    }

    // Просто возвращает последнюю известную строку

    // Отправка пати (клиент -> сервер)
    //public static void getPartyByUUID(UUID uuid) {
      //  Minecraft.getInstance().getConnection().send(new RequestDataPacket(uuid));
    //}
}
