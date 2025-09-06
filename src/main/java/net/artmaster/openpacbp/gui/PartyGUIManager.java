package net.artmaster.openpacbp.gui;

import net.artmaster.openpacbp.network.Network;
import xaero.pac.client.parties.party.api.IClientPartyAPI;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PartyGUIManager {
    private static final Map<UUID, Boolean> playersWithPartyGUI = new ConcurrentHashMap<>();

    public static void setOpen(UUID playerUUID, boolean isOpen) {
        if (isOpen) {
            playersWithPartyGUI.put(playerUUID, true);
        } else {
            playersWithPartyGUI.remove(playerUUID);
        }
    }

}
