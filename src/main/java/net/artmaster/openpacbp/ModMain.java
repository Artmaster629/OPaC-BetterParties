package net.artmaster.openpacbp;


import com.mojang.logging.LogUtils;

import net.artmaster.openpacbp.api.quests.MyAttachments;
import net.artmaster.openpacbp.init.ModMenus;
import net.artmaster.openpacbp.network.Network;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(ModMain.MODID)
public class ModMain {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "openpacbp";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    private static final PayloadRegistrar REGISTRAR = new PayloadRegistrar("openpacbp");

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public ModMain(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        MyAttachments.register();
        ModMenus.register(modEventBus);
        modEventBus.addListener(this::commonSetup);

    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Some common setup code

        LOGGER.info("Готов служить Мастерии!");
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("Готов служить Мастерии на сервере!");
    }
}
