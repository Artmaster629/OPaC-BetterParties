package net.artmaster.openpacbp.network.party_name;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record PartyNameResponsePacket(String partyName) implements CustomPacketPayload {
    public static final Type<PartyNameResponsePacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("openpacbp", "party_name_response"));

    public static final StreamCodec<FriendlyByteBuf, PartyNameResponsePacket> CODEC =
            StreamCodec.of((buf, pkt) -> buf.writeUtf(pkt.partyName),
                    buf -> new PartyNameResponsePacket(buf.readUtf()));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

