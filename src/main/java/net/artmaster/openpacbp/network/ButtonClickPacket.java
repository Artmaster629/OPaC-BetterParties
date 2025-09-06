package net.artmaster.openpacbp.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import java.util.UUID;

public record ButtonClickPacket(String command) implements CustomPacketPayload {

    public static final ResourceLocation TYPE_ID =
            ResourceLocation.fromNamespaceAndPath("openpacbp", "button_click");

    public static final Type<ButtonClickPacket> TYPE =
            new Type<>(TYPE_ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, ButtonClickPacket> CODEC =
            StreamCodec.of(
                    (buf, pkt) -> buf.writeUtf(pkt.command),
                    buf -> new ButtonClickPacket(buf.readUtf())
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
