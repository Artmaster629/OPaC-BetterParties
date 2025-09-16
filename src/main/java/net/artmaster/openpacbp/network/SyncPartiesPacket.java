package net.artmaster.openpacbp.network;

import net.artmaster.openpacbp.api.quests.PartyInventoryData;
import net.artmaster.openpacbp.utils.SafeItemStackCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record SyncPartiesPacket(List<PartyData> parties) implements CustomPacketPayload {

    public static final ResourceLocation TYPE_ID =
            ResourceLocation.fromNamespaceAndPath("openpacbp", "sync_parties");

    public static final Type<SyncPartiesPacket> TYPE = new Type<>(TYPE_ID);





    public static final StreamCodec<RegistryFriendlyByteBuf, SyncPartiesPacket> CODEC =
            StreamCodec.of(
                    // Сериализация (сервер -> клиент)
                    (buf, pkt) -> {
                        buf.writeInt(pkt.parties().size());
                        for (var p : pkt.parties()) {
                            buf.writeUUID(p.partyId());
                            buf.writeUtf(p.partyName());
                            buf.writeInt(p.color());
                            buf.writeInt(p.items().size());

                            // Получаем provider сервера
                            for (ItemStack stack : p.items()) {
                                SafeItemStackCodec.CODEC.encode(buf, stack);
                            }
                        }
                    },
                    // Десериализация (клиент)
                    buf -> {
                        int size = buf.readInt();
                        List<PartyData> list = new ArrayList<>();
                        HolderLookup.Provider provider = Minecraft.getInstance().level.registryAccess();

                        for (int i = 0; i < size; i++) {
                            UUID id = buf.readUUID();
                            String name = buf.readUtf();
                            int color = buf.readInt();
                            int itemCount = buf.readInt();
                            List<ItemStack> items = new ArrayList<>();

                            for (int j = 0; j < itemCount; j++) {
                                items.add(SafeItemStackCodec.CODEC.decode(buf));
                            }

                            list.add(new PartyData(id, name, color, items, provider));
                        }

                        return new SyncPartiesPacket(list);
                    }
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    /** Данные одной пати */
    public record PartyData(UUID partyId, String partyName, int color, List<ItemStack> items, HolderLookup.Provider provider) {
        public PartyInventoryData toPartyInventoryData() {
            PartyInventoryData inv = new PartyInventoryData(items.size()); // создаём контейнер нужного размера
            for (int i = 0; i < items.size(); i++) {
                inv.getContainer().setItem(i, items.get(i).copy());
            }
            inv.setPartyName(partyName());
            inv.setPartyColor(color());
            return inv;
        }
    }
}
