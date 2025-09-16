package net.artmaster.openpacbp.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record RunCommandPacket(String command) implements CustomPacketPayload {
    public static final ResourceLocation TYPE_ID =
            ResourceLocation.fromNamespaceAndPath("openpacbp", "run_command");

    public static final CustomPacketPayload.Type<RunCommandPacket> TYPE =
            new CustomPacketPayload.Type<>(TYPE_ID);

    public static final StreamCodec<FriendlyByteBuf, RunCommandPacket> CODEC =
            StreamCodec.of(
                    (buf, pkt) -> buf.writeUtf(pkt.command),
                    buf -> new RunCommandPacket(buf.readUtf())
            );

    @Override
    public RunCommandPacket.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

