//package net.artmaster.openpacbp.api.quests;
//
//import net.minecraft.core.HolderLookup;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.world.SimpleContainer;
//import xaero.pac.common.claims.player.api.IPlayerClaimInfoAPI;
//import xaero.pac.common.server.api.OpenPACServerAPI;
//
//
//public class PlayerInventoryData {
//    private final SimpleContainer container;
//    private String ownerName = "";
//    private String partyName = "";
//
//    public PlayerInventoryData(int size) {
//        this.container = new SimpleContainer(size);
//    }
//
//    public SimpleContainer getContainer() {
//        return container;
//    }
//
//    public String getOwnerName() {
//        return ownerName;
//    }
//
//    public void setOwnerName(String name) {
//        this.ownerName = name;
//    }
//
//    public String getPartyName() {
//        return partyName;
//    }
//
//    public void setPartyName(String name) {
//        this.partyName = name;
//    }
//
//    public static CompoundTag save(PlayerInventoryData data, HolderLookup.Provider registries) {
//        CompoundTag tag = new CompoundTag();
//        tag.put("Items", data.container.createTag(registries));
//        tag.putString("Owner", data.ownerName);
//        tag.putString("Party", data.partyName);
//        return tag;
//    }
//
//    public static void load(PlayerInventoryData data, CompoundTag tag, HolderLookup.Provider registries) {
//        data.container.fromTag(tag.getList("Items", 10), registries);
//        if (tag.contains("Owner")) {
//            data.ownerName = tag.getString("Owner");
//        }
//        if (tag.contains("Party")) {
//            data.partyName = tag.getString("Party");
//        }
//    }
//}