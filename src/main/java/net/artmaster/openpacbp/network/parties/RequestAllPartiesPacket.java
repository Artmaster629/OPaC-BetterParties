package net.artmaster.openpacbp.network.parties;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record RequestAllPartiesPacket() implements CustomPacketPayload {
    public static final ResourceLocation TYPE_ID =
            ResourceLocation.fromNamespaceAndPath("openpacbp", "request_all_parties");
    public static final Type<RequestAllPartiesPacket> TYPE = new Type<>(TYPE_ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, RequestAllPartiesPacket> CODEC =
            new StreamCodec<>() {
                @Override
                public RequestAllPartiesPacket decode(RegistryFriendlyByteBuf buf) { return new RequestAllPartiesPacket(); }
                @Override
                public void encode(RegistryFriendlyByteBuf buf, RequestAllPartiesPacket pkt) {}
            };

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }
}