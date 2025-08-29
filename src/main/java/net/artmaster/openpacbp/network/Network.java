package net.artmaster.openpacbp.network;

import net.artmaster.openpacbp.gui.PartyGUIRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.codec.StreamCodec;
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
                        player.server.getCommands().performPrefixedCommand(
                                player.createCommandSourceStack(),
                                packet.command()
                        );
                        sendCommand(player, packet.command()); // если нужно ещё и обратно на клиент
                    }
                })
        );
    }

    // Отправка GUI (сервер -> клиент)
    public static void sendOpenGui(ServerPlayer player) {
        player.connection.send(new OpenGuiPacket());
    }

    // Отправка команды (сервер -> клиент)
    public static void sendCommand(ServerPlayer player, String command) {
        player.connection.send(new RunCommandPacket(command));
    }

    // Отправка нажатия кнопки (клиент -> сервер)
    public static void sendButtonClick(String command) {
        Minecraft.getInstance().getConnection().send(new ButtonClickPacket(command));
    }
}
