package net.artmaster.openpacbp.api.quests;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GlobalStorageData {
    private final Map<UUID, PlayerInventoryData> playerInventories = new HashMap<>();

    public PlayerInventoryData getOrCreatePlayerInv(UUID playerId) {
        return playerInventories.computeIfAbsent(playerId, id -> new PlayerInventoryData(9));
    }

    public Map<UUID, PlayerInventoryData> getAll() {
        return playerInventories;
    }

    public CompoundTag save(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        ListTag list = new ListTag();

        for (var entry : playerInventories.entrySet()) {
            CompoundTag playerTag = new CompoundTag();
            playerTag.putUUID("Player", entry.getKey());
            playerTag.put("Items", entry.getValue().getContainer().createTag(provider));
            playerTag.putString("Owner", entry.getValue().getOwnerName());
            playerTag.putString("Party", entry.getValue().getPartyName());
            list.add(playerTag);
        }

        tag.put("Players", list);
        return tag;
    }


    public void load(CompoundTag tag, HolderLookup.Provider provider) {
        playerInventories.clear();
        ListTag list = tag.getList("Players", 10);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag playerTag = list.getCompound(i);
            UUID id = playerTag.getUUID("Player");
            PlayerInventoryData data = new PlayerInventoryData(9);
            data.getContainer().fromTag(playerTag.getList("Items", 10), provider);
            if (playerTag.contains("Owner", 8)) {
                data.setOwnerName(playerTag.getString("Owner"));
            }
            if (playerTag.contains("Party", 8)) {
                data.setOwnerName(playerTag.getString("Party"));
            }
            playerInventories.put(id, data);
        }
    }
}
