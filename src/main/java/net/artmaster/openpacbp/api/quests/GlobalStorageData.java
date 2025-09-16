package net.artmaster.openpacbp.api.quests;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GlobalStorageData {
    private final Map<UUID, PartyInventoryData> partyInventories = new HashMap<>();

    public PartyInventoryData getOrCreatePartyInv(UUID partyId) {
        return partyInventories.computeIfAbsent(partyId, id -> new PartyInventoryData(27)); // например, 27 слотов
    }

    public Map<UUID, PartyInventoryData> getAll() {
        return partyInventories;
    }

    public CompoundTag save(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        ListTag list = new ListTag();

        for (var entry : partyInventories.entrySet()) {
            CompoundTag partyTag = entry.getValue().save(provider);
            partyTag.putUUID("PartyId", entry.getKey());
            list.add(partyTag);
        }

        tag.put("Parties", list);
        return tag;
    }

    public void load(CompoundTag tag, HolderLookup.Provider provider) {
        partyInventories.clear();
        ListTag list = tag.getList("Parties", 10);

        for (int i = 0; i < list.size(); i++) {
            CompoundTag partyTag = list.getCompound(i);
            UUID partyId = partyTag.getUUID("PartyId");

            PartyInventoryData inv = new PartyInventoryData(27);
            inv.load(partyTag, provider);

            partyInventories.put(partyId, inv);
        }
    }
}

