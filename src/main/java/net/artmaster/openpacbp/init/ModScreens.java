package net.artmaster.openpacbp.init;

import net.artmaster.openpacbp.ModMain;
import net.artmaster.openpacbp.client.GlobalTradesScreen;
import net.artmaster.openpacbp.client.PartyTradesScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@Mod("openpacbp")
@EventBusSubscriber(modid = ModMain.MODID, value = Dist.CLIENT)
public class ModScreens {
    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenus.PARTY_STORAGE_TRADES.get(), PartyTradesScreen::new);
        event.register(ModMenus.GLOBAL_STORAGE_TRADES.get(), GlobalTradesScreen::new);
    }
}
