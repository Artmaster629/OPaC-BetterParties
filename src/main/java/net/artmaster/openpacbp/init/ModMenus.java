package net.artmaster.openpacbp.init;

import net.artmaster.openpacbp.ModMain;
import net.artmaster.openpacbp.gui.GlobalTradesMenu;
import net.artmaster.openpacbp.gui.PartyTradesMenu;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.Supplier;

public class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(BuiltInRegistries.MENU, ModMain.MODID);

    public static final Supplier<MenuType<PartyTradesMenu>> PARTY_STORAGE_TRADES =
            MENUS.register("party_trades_storage",
                    () -> new MenuType<>(
                            PartyTradesMenu::create,
                            FeatureFlags.DEFAULT_FLAGS
                    ));
    public static final Supplier<MenuType<GlobalTradesMenu>> GLOBAL_STORAGE_TRADES =
            MENUS.register("global_trades_storage",
                    () -> new MenuType<>(
                            (id, inv) -> new GlobalTradesMenu(id, inv, List.of()),
                            FeatureFlags.DEFAULT_FLAGS
                    ));


    public static void register(IEventBus bus) {
        MENUS.register(bus);
    }
}