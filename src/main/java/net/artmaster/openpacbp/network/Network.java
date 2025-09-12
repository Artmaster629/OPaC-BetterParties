package net.artmaster.openpacbp.network;

import net.artmaster.openpacbp.api.quests.GlobalStorageData;
import net.artmaster.openpacbp.api.quests.MyAttachments;
import net.artmaster.openpacbp.api.quests.PlayerInventoryData;
import net.artmaster.openpacbp.api.quests.menu.GlobalStorageMenu;
import net.artmaster.openpacbp.client.PartyGUIRenderer;
import net.artmaster.openpacbp.network.party_name.GetPartyNameClientPacket;
import net.artmaster.openpacbp.network.party_name.PartyNameResponsePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

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
    public static final StreamCodec<RegistryFriendlyByteBuf, GetPartyNameClientPacket> CODEC_2 =
            new StreamCodec<>() {
                @Override
                public GetPartyNameClientPacket decode(RegistryFriendlyByteBuf buf) {
                    return new GetPartyNameClientPacket();
                }

                @Override
                public void encode(RegistryFriendlyByteBuf buf, GetPartyNameClientPacket packet) {
                    // пусто, пакет не содержит данных
                }
            };

    // StreamCodec для ClientCommandPacket
    public static final StreamCodec<RegistryFriendlyByteBuf, ButtonClickPacket> COMMAND_CODEC =
            StreamCodec.of(
                    (buf, packet) -> buf.writeUtf(packet.command()),
                    buf -> new ButtonClickPacket(buf.readUtf())
            );

    // Регистрация пакетов
    public static void register(PayloadRegistrar registrar) {
        // 1. OpenGuiPacket (сервер -> клиент)
        registrar.playToClient(OpenGuiPacket.TYPE, CODEC, (packet, ctx) ->
                ctx.enqueueWork(() -> Minecraft.getInstance().setScreen(new PartyGUIRenderer()))
        );
// 2. RunCommandPacket (сервер -> клиент)
        registrar.playToClient(RunCommandPacket.TYPE, RunCommandPacket.CODEC, (packet, ctx) ->
                ctx.enqueueWork(() -> {
                    LocalPlayer player = Minecraft.getInstance().player;
                    if (player != null) {
                        player.connection.sendCommand(packet.command());
                    }
                })
        );

// 3. ButtonClickPacket (клиент -> сервер)
        registrar.playToServer(ButtonClickPacket.TYPE, ButtonClickPacket.CODEC, (packet, ctx) ->
                ctx.enqueueWork(() -> {
                    ServerPlayer player = (ServerPlayer) ctx.player();
                    if (player != null) {
                        Container storageContainer = new SimpleContainer(9);
                        player.openMenu(new SimpleMenuProvider(
                                (id, inv, buf) -> new GlobalStorageMenu(id, inv, storageContainer),
                                Component.literal("Глобальное хранилище")
                        ));
                    }
                })
        );
        registrar.playToServer(GetPartyNameClientPacket.TYPE, CODEC_2,
                (packet, ctx) -> {
                    ServerPlayer player = (ServerPlayer) ctx.player();
                    GlobalStorageData storage = player.serverLevel().getData(MyAttachments.GLOBAL_STORAGE);
                    PlayerInventoryData inv = storage.getOrCreatePlayerInv(player.getUUID());
                    String partyName = inv.getPartyName();

                    // Отправляем обратно клиенту
                    ctx.reply(new PartyNameResponsePacket(partyName));
                });

        registrar.playToClient(PartyNameResponsePacket.TYPE, PartyNameResponsePacket.CODEC,
                (packet, ctx) -> {
                    ctx.enqueueWork(() -> {
                        Network.setPartyName(packet.partyName());
                    });
                });


        registrar.playToServer(QuestButtonClickPacket.TYPE, QuestButtonClickPacket.CODEC, (packet, ctx) ->
                ctx.enqueueWork(() -> {
                    ServerPlayer player = (ServerPlayer) ctx.player();
                    if (player != null) {
                        player.server.getCommands().performPrefixedCommand(
                                player.createCommandSourceStack(),
                                packet.command()
                        );
                        sendCommand(player, packet.command()); // если нужно ещё и обратно на клиент
                    }
                })
        );

//        registrar.playToServer(RequestDataPacket.TYPE, RequestDataPacket.CODEC, (packet, ctx) ->
//                ctx.enqueueWork(() -> {
//                    ServerPlayer player = (ServerPlayer) ctx.player();
//                    if (player != null) {
//                        IClientPartyAPI partyAPI = ((IClientPartyAPI) OpenPACServerAPI.get(player.server).getPartyManager().getPartyById(packet.uuid()));
//                        sendParty(partyAPI, player);
//                    }
//                })
//        );
    }

    // Отправка GUI (сервер -> клиент)
    public static void sendOpenGui(ServerPlayer player) {
        player.connection.send(new OpenGuiPacket());
    }

    // Отправка команды (сервер -> клиент)
    public static void sendCommand(ServerPlayer player, String command) {
        player.connection.send(new RunCommandPacket(command));
    }
    // Отправка пати (сервер -> клиент)
//    public static void sendParty(IClientPartyAPI partyAPI, ServerPlayer player) {
//        player.connection.send(new ResponseDataPacket(partyAPI));
//    }


    // Отправка нажатия кнопки (клиент -> сервер)
    public static void sendButtonClick(String command) {
        Minecraft.getInstance().getConnection().send(new ButtonClickPacket(command));
    }

    private static String cachedPartyName = "";

    // Вызывается при старте GUI, чтобы запросить имя
    public static void requestPartyName() {
        Minecraft.getInstance().getConnection().send(new GetPartyNameClientPacket());
    }

    // Вызывается на клиенте, когда пришёл ответ
    public static void setPartyName(String name) {
        cachedPartyName = name;
    }

    // Просто возвращает последнюю известную строку
    public static String getPartyName() {
        return cachedPartyName;
    }

    // Отправка пати (клиент -> сервер)
    //public static void getPartyByUUID(UUID uuid) {
      //  Minecraft.getInstance().getConnection().send(new RequestDataPacket(uuid));
    //}
}
