//package net.artmaster.openpacbp.network.party_by_uuid;
//
//import net.minecraft.network.FriendlyByteBuf;
//import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
//import net.minecraft.resources.ResourceLocation;
//import xaero.pac.client.parties.party.api.IClientPartyAPI;
//
//import java.util.UUID;
//
//public record ResponseDataPacket(IClientPartyAPI partyAPI) implements CustomPacketPayload {
//
//    public static final Type<ResponseDataPacket> TYPE =
//            new Type<>(ResourceLocation.fromNamespaceAndPath("openpacbp", "respdp_party_by_uuid"));
//
//    public static ResponseDataPacket decode(FriendlyByteBuf buf) {
//
//    }
//
//    public void encode(FriendlyByteBuf buf) {
//
//    }
//
//    @Override
//    public Type<? extends CustomPacketPayload> type() {
//        return TYPE;
//    }
//}
