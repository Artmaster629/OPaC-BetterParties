package net.artmaster.openpacbp.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record OpenGuiPacket() implements CustomPacketPayload {

    public static final Type<OpenGuiPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("openpacbp", "open_gui"));

    public static OpenGuiPacket decode(FriendlyByteBuf buf) {
        return new OpenGuiPacket();
    }

    public void encode(FriendlyByteBuf buf) {
        // пусто, пакет не содержит данных
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
