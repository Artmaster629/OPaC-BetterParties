//package net.artmaster.openpacbp.mixin;
//
//import net.artmaster.openpacbp.utils.OpenPacUtils;
//import net.minecraft.network.chat.Component;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.server.MinecraftServer;
//import net.minecraft.server.level.ServerPlayer;
//import net.neoforged.neoforge.server.ServerLifecycleHooks;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Shadow;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//import xaero.pac.common.claims.ClaimsManager;
//import xaero.pac.common.claims.DimensionClaimsManager;
//import xaero.pac.common.claims.player.PlayerChunkClaim;
//import xaero.pac.common.claims.player.PlayerClaimInfoManager;
//import xaero.pac.common.server.api.OpenPACServerAPI;
//import xaero.pac.common.server.parties.party.api.IPartyManagerAPI;
//import xaero.pac.common.server.parties.party.api.IServerPartyAPI;
//import xaero.pac.common.server.player.config.IPlayerConfigManager;
//
//import java.util.UUID;
//
//
//@Mixin(value = ClaimsManager.class, remap = false)
//public abstract class ClaimChangeOwnerMixin {
//
//    @Shadow protected IPlayerConfigManager configManager;
//
//    @Shadow protected abstract PlayerChunkClaim getClaimState(UUID id, int subConfigIndex, boolean forceload);
//
//    @Shadow protected abstract PlayerClaimInfoManager<?, ?> getPlayerClaimInfoManager();
//
//    @Shadow public abstract DimensionClaimsManager<?, ?> getDimension(ResourceLocation dimension);
//
//    @Inject(
//            method = "claim",
//            at = @At("TAIL"),
//            cancellable = true
//    )
//    private void onClaimInject(ResourceLocation dimension, UUID id, int subConfigIndex, int x, int z, boolean forceload, CallbackInfoReturnable<PlayerChunkClaim> cir) {
//        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
//        if (server == null) return;
//
//        ServerPlayer player = server.getPlayerList().getPlayer(id);
//        if (player == null) return;
//
//        IPartyManagerAPI partyManager = OpenPACServerAPI.get(server).getPartyManager();
//        IServerPartyAPI party = partyManager.getPartyByMember(id);
//
//        UUID ownerId = id; // по умолчанию владелец = сам игрок
//        if (party != null) {
//            ownerId = party.getOwner().getUUID();
//        }
//
//        var claimManager = OpenPACServerAPI.get(server).getServerClaimsManager();
//
//
//
//        int ownerLimit = OpenPacUtils.getEffectiveChunkLimit(server, ownerId);
//        int totalClaims = claimManager.getPlayerInfo(ownerId).getClaimCount();
//
//        if (totalClaims >= ownerLimit) {
//            player.displayClientMessage(
//                    Component.translatable("text.openpacbp.party_limit_reached"),
//                    false
//            );
//            cir.setReturnValue(null);
//            cir.cancel();
//            return;
//        }
//
//        UUID effectiveId = (party != null) ? party.getOwner().getUUID() : id;
//
//        PlayerChunkClaim claim = getClaimState(effectiveId, subConfigIndex, forceload);
//
//
//        DimensionClaimsManager<?, ?> dimensionClaims = getDimension(dimension);
//        if (dimensionClaims == null) {
//            return;
//        }
//
//        PlayerClaimInfoManager<?, ?> infoManager = getPlayerClaimInfoManager();
//
//        @SuppressWarnings("unchecked")
//        PlayerChunkClaim result = ((DimensionClaimsManager) dimensionClaims).claim(
//                x, z, claim, infoManager, configManager
//        );
//
//        cir.setReturnValue(result);
//        cir.cancel();
//    }
//}
//
//
//
//
//
//
