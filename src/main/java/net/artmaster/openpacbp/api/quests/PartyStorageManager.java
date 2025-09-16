package net.artmaster.openpacbp.api.quests;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import xaero.pac.common.server.api.OpenPACServerAPI;
import xaero.pac.common.server.parties.party.api.IPartyManagerAPI;
import xaero.pac.common.server.parties.party.api.IServerPartyAPI;

import java.util.UUID;

public class PartyStorageManager {
    public static PartyInventoryData getPartyInventory(MinecraftServer server, UUID playerId) {
        IPartyManagerAPI partyManager = OpenPACServerAPI.get(server).getPartyManager();
        IServerPartyAPI party = partyManager.getPartyByMember(playerId);

        if (party == null) {
            return null; // игрок не состоит в пати
        }

        UUID partyId = party.getId();

        ServerLevel serverLevel = server.getPlayerList().getPlayer(playerId).serverLevel();

        GlobalStorageData globalStorage = serverLevel.getData(MyAttachments.GLOBAL_STORAGE);
        return globalStorage.getOrCreatePartyInv(partyId);
    }

    public static GlobalStorageData getGlobalStorage(MinecraftServer server, UUID playerId) {
        return server.getPlayerList().getPlayer(playerId).serverLevel().getData(MyAttachments.GLOBAL_STORAGE); //метода server.getLevel() не существует
    }
}
