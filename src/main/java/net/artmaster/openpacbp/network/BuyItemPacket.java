package net.artmaster.openpacbp.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record BuyItemPacket(int page, int requiredSlot, int resultSlot) implements CustomPacketPayload {

    public static final ResourceLocation TYPE_ID =
            ResourceLocation.fromNamespaceAndPath("openpacbp", "buy_item_packet");

    public static final Type<BuyItemPacket> TYPE = new Type<>(TYPE_ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, BuyItemPacket> CODEC =
            StreamCodec.of(
                    // Сериализация (сервер -> клиент)
                    (buf, pkt) -> {
                        buf.writeVarInt(pkt.page);
                        buf.writeVarInt(pkt.requiredSlot);
                        buf.writeVarInt(pkt.resultSlot);
                    },
                    // Десериализация (клиент)
                    buf -> {
                        int page = buf.readVarInt();
                        int requiredSlot = buf.readVarInt();
                        int resultSlot = buf.readVarInt();

                        return new BuyItemPacket(page, requiredSlot, resultSlot);
                    }
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

}
