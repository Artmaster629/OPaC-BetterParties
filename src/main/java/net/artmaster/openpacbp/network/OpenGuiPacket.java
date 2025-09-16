package net.artmaster.openpacbp.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record OpenGuiPacket() implements CustomPacketPayload {

    public static final Type<OpenGuiPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("openpacbp", "open_gui"));

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


    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
