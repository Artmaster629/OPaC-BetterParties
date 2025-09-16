package net.artmaster.openpacbp.api.quests;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.SimpleContainer;

public class PartyInventoryData {
    private final SimpleContainer container;
    private String partyName = "";
    private int partyColor = 0xFFFFFF;

    public PartyInventoryData(int size) {
        this.container = new SimpleContainer(size);
    }

    public SimpleContainer getContainer() {
        return container;
    }

    public String getPartyName() {
        return partyName;
    }
    public void setPartyName(String name) {
        this.partyName = name;
    }


    public int getPartyColor() {
        return partyColor;
    }
    public void setPartyColor(int color) {
        this.partyColor = color;
    }

    public CompoundTag save(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        tag.put("Items", container.createTag(registries));
        tag.putString("Party", partyName);
        tag.putInt("Color", partyColor);
        return tag;
    }

    public void load(CompoundTag tag, HolderLookup.Provider registries) {
        container.fromTag(tag.getList("Items", 10), registries);
        if (tag.contains("Party", 8)) {
            this.partyName = tag.getString("Party");
        }
        if (tag.contains("Color", 3)) {
            this.partyColor = tag.getInt("Color");
        }
    }

}

