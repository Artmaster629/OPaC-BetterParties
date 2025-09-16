package net.artmaster.openpacbp.network.party_color;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record PartyColorResponsePacket(int partyColor) implements CustomPacketPayload {
    public static final Type<PartyColorResponsePacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("openpacbp", "party_color_response"));
    public static final StreamCodec<FriendlyByteBuf, PartyColorResponsePacket> CODEC =
            StreamCodec.of((buf, pkt) -> buf.writeInt(pkt.partyColor),
                    buf -> new PartyColorResponsePacket(buf.readInt()));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

