package net.artmaster.openpacbp.command;

import net.artmaster.openpacbp.ModMain;
import net.artmaster.openpacbp.api.trades.GlobalStorageData;
import net.artmaster.openpacbp.api.trades.MyAttachments;
import net.artmaster.openpacbp.api.trades.PartyInventoryData;
import net.artmaster.openpacbp.api.trades.StorageManager;
import net.artmaster.openpacbp.gui.GlobalTradesMenu;
import net.artmaster.openpacbp.gui.PartyTradesMenu;
import net.artmaster.openpacbp.network.Network;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import xaero.pac.common.server.api.OpenPACServerAPI;
import xaero.pac.common.server.parties.party.api.IPartyManagerAPI;
import xaero.pac.common.server.parties.party.api.IServerPartyAPI;

import java.util.*;

@Mod("openpacbp")
@EventBusSubscriber(modid = ModMain.MODID)
public class ModCommands {

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                Commands.literal("openpacbp")
                        //// *** /openpacbp market ***
                        .then(Commands.literal("market")
                                .then(Commands.literal("global")
                                        .executes(ctx -> {
                                            CommandSourceStack source = ctx.getSource();
                                            if (source.getEntity() instanceof ServerPlayer player) {
                                                GlobalStorageData storage = StorageManager.getGlobalStorage(player.server, player.getUUID());
                                                if (storage != null) {
                                                    List<PartyInventoryData> all = new ArrayList<>(storage.getAll().values());
                                                    if (!all.isEmpty()) {
                                                        Network.sendAllParties(player);
                                                        player.openMenu(new SimpleMenuProvider(
                                                                (id, inv, ply) -> {
                                                                    ServerLevel level = player.server.overworld();
                                                                    GlobalStorageData global_storage = level.getData(MyAttachments.GLOBAL_STORAGE);
                                                                    List<PartyInventoryData> allParties = new ArrayList<>(global_storage.getAll().values());
                                                                    return new GlobalTradesMenu(id, inv, allParties);
                                                                },
                                                                Component.translatable("gui.openpacbp.market_title")
                                                        ));
                                                    } else {
                                                        player.sendSystemMessage(Component.translatable("text.openpacbp.no_party_in_market"));
                                                    }
                                                }
                                            }
                                            return 1;
                                        })
                                )
                                //// *** /openpacbp market settings ***
                                .then(Commands.literal("settings")
                                        .executes(ctx -> {
                                            CommandSourceStack source = ctx.getSource();
                                            if (source.getEntity() instanceof ServerPlayer player) {
                                                PartyInventoryData storage = StorageManager.getPartyInventory(player.server, player.getUUID());
                                                MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
                                                if (server == null) return 0;

                                                IPartyManagerAPI partyManager = OpenPACServerAPI.get(server).getPartyManager();
                                                IServerPartyAPI party = partyManager.getPartyByMember(player.getUUID());
                                                if (party == null) {
                                                    player.displayClientMessage(Component.translatable("text.openpacbp.no_party"), false);
                                                    return 0;
                                                }
                                                if (storage != null) {
                                                    player.openMenu(new SimpleMenuProvider(
                                                            (id, inv, ply) -> new PartyTradesMenu(id, inv, storage.getContainer()),
                                                            Component.translatable("gui.openpacbp.party_market_title")
                                                    ));
                                                }
                                            }
                                            return 1;
                                        })
                                )
                        )
                        //// *** /openpacbp manage ***
                        .then(Commands.literal("manage")
                                .executes(ctx -> {
                                    CommandSourceStack source = ctx.getSource();
                                    if (source.getEntity() instanceof ServerPlayer player) {
                                        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
                                        if (server == null) return 0;

                                        IPartyManagerAPI partyManager = OpenPACServerAPI.get(server).getPartyManager();
                                        IServerPartyAPI party = partyManager.getPartyByMember(player.getUUID());
                                        if (party == null) {
                                            player.displayClientMessage(Component.translatable("text.openpacbp.no_party_pm"), false);
                                            return 0;
                                        }
                                        Network.sendOpenGui(player);
                                    }
                                    return 1;
                                })
                        )
        );
    }
}

