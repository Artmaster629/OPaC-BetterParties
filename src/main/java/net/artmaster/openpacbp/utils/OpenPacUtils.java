package net.artmaster.openpacbp.utils;

import net.luckperms.api.model.user.User;
import net.minecraft.server.MinecraftServer;

import java.util.UUID;

public class OpenPacUtils {

    public static int getEffectiveChunkLimit(MinecraftServer server, UUID playerId) {


        // бонус напрямую из LuckPerms
        int bonus = 0;



        try {
            net.luckperms.api.LuckPerms lp = net.luckperms.api.LuckPermsProvider.get();
            User user = lp.getUserManager().loadUser(playerId).join();
            if (user != null) {
                // предполагаем, что мета: meta.xaero.pac_max_claims
                String metaValue = user.getCachedData().getMetaData().getMetaValue("xaero.pac_max_claims");
                if (metaValue != null) {
                    bonus = Integer.parseInt(metaValue);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bonus;
    }
}
