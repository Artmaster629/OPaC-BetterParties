package net.artmaster.openpacbp.api.quests.menu;

import net.artmaster.openpacbp.ModMain;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(BuiltInRegistries.MENU, ModMain.MODID);

    public static final Supplier<MenuType<GlobalStorageMenu>> GLOBAL_STORAGE =
            MENUS.register("global_storage",
                    () -> new MenuType<>(
                            GlobalStorageMenu::create,
                            FeatureFlags.DEFAULT_FLAGS
                    ));

    public static void register(IEventBus bus) {
        MENUS.register(bus);
    }
}