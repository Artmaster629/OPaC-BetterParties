package net.artmaster.openpacbp.mixin;

import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xaero.pac.common.claims.ClaimsManager;
import xaero.pac.common.claims.IClaimsManager;
import xaero.pac.common.claims.api.IClaimsManagerAPI;
import xaero.pac.common.claims.player.PlayerChunkClaim;
import xaero.pac.common.server.api.OpenPACServerAPI;
import xaero.pac.common.server.parties.party.api.IPartyManagerAPI;
import xaero.pac.common.server.parties.party.api.IServerPartyAPI;

import java.util.UUID;


@Mixin(ClaimsManager.class) // заменишь на настоящий класс
public abstract class ClaimCreationPartyOwnerMixin {

    @Inject(method = "claim", at = @At("HEAD"), cancellable = true)
    private void onClaim(ResourceLocation dimension, UUID id, int subConfigIndex, int x, int z, boolean forceload, CallbackInfoReturnable<PlayerChunkClaim> cir) {

        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) return;

        ServerPlayer player = server.getPlayerList().getPlayer(id);
        if (player == null) {
            return;
        }

        // Получаем PartyManager через OpenPAC API
        IPartyManagerAPI partyManager = OpenPACServerAPI.get(server).getPartyManager();
        IServerPartyAPI party = partyManager.getPartyByMember(id);
        var partyAll =  OpenPACServerAPI.get(server).getServerClaimsManager().getPlayerInfo(id);



        if (party != null) {
            UUID ownerId = party.getOwner().getUUID();
            var claimManager = OpenPACServerAPI.get(server).getServerClaimsManager();
            int claimedChunks = claimManager.getPlayerInfo(ownerId).getClaimCount();

            System.out.println(claimManager.getPlayerBaseClaimLimit(id));



            LogUtils.getLogger().info("Party "+claimManager.getPlayerInfo(ownerId).getClaimsName()+" now has "+claimedChunks+"/"+claimManager.getPlayerBaseClaimLimit(ownerId)+" chunks");
            if (claimManager.getPlayerBaseClaimLimit(ownerId) < claimedChunks) {
                cir.setReturnValue(null);
                player.displayClientMessage(Component.translatable("text.openpacbp.party_limit_reached"), false);
                player.closeContainer();
                cir.cancel();
            }
        }

    }
}




