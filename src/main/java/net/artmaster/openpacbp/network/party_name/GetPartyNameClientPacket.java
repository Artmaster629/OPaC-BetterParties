package net.artmaster.openpacbp.network.party_name;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record GetPartyNameClientPacket() implements CustomPacketPayload {

    public static final Type<net.artmaster.openpacbp.network.party_name.GetPartyNameClientPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("openpacbp", "get_party_name_client_packet"));

    public static GetPartyNameClientPacket decode(FriendlyByteBuf buf) {
        return new GetPartyNameClientPacket();
    }

    public void encode(FriendlyByteBuf buf) {
        // пусто, пакет не содержит данных
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

