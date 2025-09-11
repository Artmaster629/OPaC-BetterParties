package net.artmaster.openpacbp.client;

import net.artmaster.openpacbp.ModMain;
import net.artmaster.openpacbp.api.quests.menu.ModMenus;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@Mod("openpacbp")
@EventBusSubscriber(modid = ModMain.MODID, value = Dist.CLIENT)
public class ClientModEvents {
    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenus.GLOBAL_STORAGE.get(), GlobalStorageScreen::new);
    }
}
