//package net.artmaster.openpacbp.event;
//
//import net.artmaster.openpacbp.ModMain;
//import net.artmaster.openpacbp.api.quests.GlobalStorageData;
//import net.artmaster.openpacbp.api.quests.MyAttachments;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.server.level.ServerPlayer;
//import net.neoforged.bus.api.SubscribeEvent;
//import net.neoforged.fml.common.EventBusSubscriber;
//import net.neoforged.fml.common.Mod;
//import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
//import xaero.pac.common.server.api.OpenPACServerAPI;
//
//@Mod("openpacbp")
//@EventBusSubscriber(modid = ModMain.MODID)
//public class ModEventHandler {
//    @SubscribeEvent
//    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
//        if (event.getEntity() instanceof ServerPlayer player) {
//            ServerLevel level = player.serverLevel();
//            GlobalStorageData storage = level.getData(MyAttachments.GLOBAL_STORAGE);
//            PlayerInventoryData inv = storage.getOrCreatePlayerInv(player.getUUID());
//
//
//
//
//
//            for (int i = 0; i < inv.getContainer().getContainerSize(); i++) {
//                player.getInventory().add(i, inv.getContainer().getItem(i));
//            }
//
//            for (int i = 0; i < storage.getAll().size(); i++) {
//                System.out.println(level.getPlayerByUUID(storage.getAll().keySet().stream().toList().get(i)).getName().getString()+
//                        "'s party: "+inv.getPartyName()
//                        +". "+storage.getAll().values().stream().toList().get(i));
//            }
//            inv.getContainer().clearContent();



////            var savedContainer = player.getData(MyAttachments.GLOBAL_STORAGE).getOrCreatePlayerInv(player.getUUID()).getContainer();
////            for (int i=0; i<player.getInventory().getContainerSize(); i++) {
////                savedContainer.addItem(player.getInventory().getItem(i));
////            }
////            player.getData(MyAttachments.GLOBAL_STORAGE).getOrCreatePlayerInv(player.getUUID()).setOwnerName(player.getGameProfile().getName());
////            player.getInventory().clearContent();
//        }
//
//    }
//    @SubscribeEvent
//    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
//        if (event.getEntity() instanceof ServerPlayer player) {
//            for (int i=0; i < player.getInventory().getContainerSize()-5; i++) {
//                System.out.println("DEBUG: "+i+" слот: "+player.getInventory().getItem(i).getHoverName());
//            }
//            System.out.println("DEBUG: "+40+" слот(левая рука): "+player.getInventory().getItem(40).getHoverName());
//
//
//
//        }
//    }
//}
//            for (int i=0; i<savedContainer.getContainerSize(); i++) {
//                player.getInventory().add(savedContainer.getItem(i));
//            }
//            var savedData = player.getData(MyAttachments.GLOBAL_STORAGE).getOrCreatePlayerInv(player.getUUID());
//            savedContainer.clearContent();
//            System.out.println(savedData.getOwnerName());
//            for (int i=0; i<savedContainer.getContainerSize(); i++) {
//                System.out.println();
//            }

