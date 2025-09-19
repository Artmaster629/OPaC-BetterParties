package net.artmaster.openpacbp.network.parties.party_name;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record GetPartyNameClientPacket(int partyIndex) implements CustomPacketPayload {

    public static final Type<net.artmaster.openpacbp.network.parties.party_name.GetPartyNameClientPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("openpacbp", "get_party_name_client_packet"));

    public static final StreamCodec<FriendlyByteBuf, GetPartyNameClientPacket> CODEC =
            StreamCodec.of((buf, pkt) -> buf.writeInt(pkt.partyIndex),
                    buf -> new GetPartyNameClientPacket(buf.readInt()));


    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

