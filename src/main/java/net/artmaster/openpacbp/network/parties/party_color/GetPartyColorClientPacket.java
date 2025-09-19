package net.artmaster.openpacbp.network.parties.party_color;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record GetPartyColorClientPacket(int partyIndex) implements CustomPacketPayload {

    public static final Type<GetPartyColorClientPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("openpacbp", "get_party_color_client_packet"));


    public static final StreamCodec<FriendlyByteBuf, GetPartyColorClientPacket> CODEC =
            StreamCodec.of((buf, pkt) -> buf.writeInt(pkt.partyIndex),
                    buf -> new GetPartyColorClientPacket(buf.readInt()));


    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

