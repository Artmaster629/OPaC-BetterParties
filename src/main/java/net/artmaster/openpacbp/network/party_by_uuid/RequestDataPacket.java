//package net.artmaster.openpacbp.network.party_by_uuid;
//
//import net.minecraft.network.RegistryFriendlyByteBuf;
//import net.minecraft.network.codec.StreamCodec;
//import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
//import net.minecraft.resources.ResourceLocation;
//
//import java.util.UUID;
//
//public record RequestDataPacket() implements CustomPacketPayload {
//
//    public static final ResourceLocation TYPE_ID =
//            ResourceLocation.fromNamespaceAndPath("openpacbp", "rdp_party_by_uuid");
//
//    public static final Type<RequestDataPacket> TYPE =
//            new Type<>(TYPE_ID);
//
//    public static final StreamCodec<RegistryFriendlyByteBuf, RequestDataPacket> CODEC =
//            StreamCodec.of(
//                    (buf, pkt) -> buf.writeUUID(pkt.uuid),
//                    buf -> new RequestDataPacket(buf.readUUID())
//            );
//
//    @Override
//    public Type<? extends CustomPacketPayload> type() {
//        return TYPE;
//    }
//}
