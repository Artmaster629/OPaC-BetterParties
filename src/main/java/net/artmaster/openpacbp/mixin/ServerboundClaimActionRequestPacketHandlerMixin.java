package net.artmaster.openpacbp.mixin;

import net.artmaster.openpacbp.utils.OpenPacUtils;
import net.minecraft.server.MinecraftServer;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xaero.pac.common.server.api.OpenPACServerAPI;
import xaero.pac.common.server.claims.ServerClaimsManager;
import xaero.pac.common.server.parties.party.api.IPartyManagerAPI;
import xaero.pac.common.server.parties.party.api.IServerPartyAPI;
import java.util.UUID;

@Mixin(ServerClaimsManager.class)
public class ServerboundClaimActionRequestPacketHandlerMixin {



    @Inject(
            method = "getPlayerBaseClaimLimit",
            at = @At("HEAD"),
            cancellable = true
    )
    private void redirectBaseClaimLimit(UUID playerId, CallbackInfoReturnable<Integer> ci) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        UUID effectiveId = playerId;

        // подменяем UUID на владельца гильдии
        OpenPACServerAPI api = OpenPACServerAPI.get(server);
        IPartyManagerAPI partyManager = api.getPartyManager();
        IServerPartyAPI party = partyManager.getPartyByMember(playerId);
        if (party != null && party.getOwner() != null) {
            effectiveId = party.getOwner().getUUID();
        }

        int limit = OpenPacUtils.getEffectiveChunkLimit(server, effectiveId);

        ci.setReturnValue(limit); // полностью заменяем базовое значение
    }



}
