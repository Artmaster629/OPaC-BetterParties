//package net.artmaster.openpacbp.network;
//
//import net.minecraft.network.RegistryFriendlyByteBuf;
//import net.minecraft.network.codec.StreamCodec;
//import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
//import net.minecraft.resources.ResourceLocation;
//
//public record QuestButtonClickPacket(String command) implements CustomPacketPayload {
//
//    public static final ResourceLocation TYPE_ID =
//            ResourceLocation.fromNamespaceAndPath("openpacbp", "quest_button_click");
//
//    public static final Type<QuestButtonClickPacket> TYPE =
//            new Type<>(TYPE_ID);
//
//    public static final StreamCodec<RegistryFriendlyByteBuf, QuestButtonClickPacket> CODEC =
//            StreamCodec.of(
//                    (buf, pkt) -> buf.writeUtf(pkt.command),
//                    buf -> new QuestButtonClickPacket(buf.readUtf())
//            );
//
//    @Override
//    public Type<? extends CustomPacketPayload> type() {
//        return TYPE;
//    }
//}
