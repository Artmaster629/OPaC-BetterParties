package net.artmaster.openpacbp.command;

import net.artmaster.openpacbp.ModMain;
import net.artmaster.openpacbp.api.quests.GlobalStorageData;
import net.artmaster.openpacbp.api.quests.PartyInventoryData;
import net.artmaster.openpacbp.api.quests.PartyStorageManager;
import net.artmaster.openpacbp.gui.GlobalTradesMenu;
import net.artmaster.openpacbp.gui.PartyTradesMenu;
import net.artmaster.openpacbp.network.Network;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
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
                Commands.literal("openpac-quests")
                        .executes(ctx -> {
                            CommandSourceStack source = ctx.getSource();
                            if (source.getEntity() instanceof net.minecraft.server.level.ServerPlayer player) {
                                if (player != null) {
                                    PartyInventoryData storage = PartyStorageManager.getPartyInventory(player.server, player.getUUID());
                                    if (storage != null) {
                                        player.openMenu(new SimpleMenuProvider(
                                                (id, inv, ply) -> new PartyTradesMenu(id, inv, storage.getContainer()),
                                                Component.literal("Хранилище гильдии")
                                        ));
                                    }
                                }
                            }
                            return 1;
                        })
        );
        event.getDispatcher().register(
                Commands.literal("openpac-trades")
                        .executes(ctx -> {
                            CommandSourceStack source = ctx.getSource();
                            if (source.getEntity() instanceof net.minecraft.server.level.ServerPlayer player) {
                                GlobalStorageData storage = PartyStorageManager.getGlobalStorage(player.server, player.getUUID());
                                if (storage != null) {
                                    List<PartyInventoryData> all = new ArrayList<>(storage.getAll().values());
                                    System.out.println("DEBUG: partyData.size() = " + all.size());
                                    if (!all.isEmpty()) {
                                        Network.sendAllParties(player);
                                        player.openMenu(new SimpleMenuProvider(
                                                (id, inv, ply) -> new GlobalTradesMenu(id, inv, new ArrayList<>()), // пустой список пока
                                                Component.literal("Все пати")
                                        ));

                                    } else {
                                        player.sendSystemMessage(Component.literal("Нет пати для отображения!"));
                                    }
                                }
                            }
                            return 1;
                        })
        );
        event.getDispatcher().register(
                Commands.literal("openpac-partymanage")
                        .executes(ctx -> {
                            CommandSourceStack source = ctx.getSource();
                            if (source.getEntity() instanceof net.minecraft.server.level.ServerPlayer player) {
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
        );

    }
}

