package net.artmaster.openpacbp.network.parties;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.UUID;

public record SyncAllyPartiesPacket(List<AllyData> allies) implements CustomPacketPayload {

    public static final ResourceLocation TYPE_ID =
            ResourceLocation.fromNamespaceAndPath("openpacbp", "sync_ally_parties");

    public static final Type<SyncAllyPartiesPacket> TYPE = new Type<>(TYPE_ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncAllyPartiesPacket> CODEC =
            StreamCodec.of(
                    // Сериализация (сервер -> клиент)
                    (buf, pkt) -> {
                        buf.writeInt(pkt.allies.size());
                        for (AllyData ally : pkt.allies) {
                            buf.writeUUID(ally.partyId());
                            buf.writeUtf(ally.ownerName());
                        }
                    },
                    // Десериализация (клиент)
                    buf -> {
                        int size = buf.readInt();
                        List<AllyData> allies = new java.util.ArrayList<>();
                        for (int i = 0; i < size; i++) {
                            UUID partyId = buf.readUUID();
                            String ownerName = buf.readUtf(32767);
                            allies.add(new AllyData(partyId, ownerName));
                        }
                        return new SyncAllyPartiesPacket(allies);
                    }
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    // Вложенный класс для данных одного союзника
    public record AllyData(UUID partyId, String ownerName) {}
}