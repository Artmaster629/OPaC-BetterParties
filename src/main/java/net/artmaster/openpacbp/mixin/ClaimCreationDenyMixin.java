package net.artmaster.openpacbp.mixin;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xaero.pac.common.claims.ClaimsManager;
import xaero.pac.common.claims.DimensionClaimsManager;
import xaero.pac.common.claims.player.PlayerChunkClaim;
import xaero.pac.common.claims.player.PlayerClaimInfoManager;
import xaero.pac.common.server.api.OpenPACServerAPI;
import xaero.pac.common.server.parties.party.api.IPartyManagerAPI;
import xaero.pac.common.server.parties.party.api.IServerPartyAPI;
import xaero.pac.common.server.player.config.IPlayerConfigManager;

import java.util.Map;
import java.util.UUID;


//@Mixin(ClaimsManager.class) // заменишь на настоящий класс
//public abstract class ClaimCreationDenyMixin {
//
//    @Inject(method = "claim", at = @At("RETURN"), cancellable = true)
//    private void onClaim(ResourceLocation dimension, UUID id, int subConfigIndex, int x, int z, boolean forceload, CallbackInfoReturnable<PlayerChunkClaim> cir) {
//
//        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
//        if (server == null) return;
//
//        ServerPlayer player = server.getPlayerList().getPlayer(id);
//        //Если метод вызвался не игроком:
//        if (player == null) return;
//
//        // Получаем PartyManager
//        IPartyManagerAPI partyManager = OpenPACServerAPI.get(server).getPartyManager();
//        IServerPartyAPI party = partyManager.getPartyByMember(id);
//        //Проверка на null
//        if (party != null) {
//            // Переназначаем приват на владельца пати
//            UUID leaderId = party.getOwner().getUUID();
//            // Здесь можно заменить playerId у claim
//            PlayerChunkClaim originalClaim = cir.getReturnValue();
//            PlayerChunkClaim newClaim = new PlayerChunkClaim(leaderId, subConfigIndex, forceload, originalClaim.getSyncIndex());
//            cir.setReturnValue(newClaim);
//        }
//    }
//}
//@Mixin(ClaimsManager.class)
//public abstract class ClaimCreationDenyMixin {
//
//    @Redirect(
//            method = "claim",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lxaero/pac/common/claims/ClaimsManager;getClaimState(Ljava/util/UUID;IZ)Lxaero/pac/common/claims/player/PlayerChunkClaim;"
//            )
//    )
//    private PlayerChunkClaim redirectGetClaimState(ClaimsManager instance, UUID originalId, int subConfigIndex, boolean forceload) {
//        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
//        if (server != null) {
//            IPartyManagerAPI partyManager = OpenPACServerAPI.get(server).getPartyManager();
//            IServerPartyAPI party = partyManager.getPartyByMember(originalId);
//            if (party != null) {
//                UUID leaderId = party.getOwner().getUUID();
//
//                return instance.getClaimState(leaderId, subConfigIndex, forceload);
//            }
//        }
//        return instance.getClaimState(originalId, subConfigIndex, forceload);
//    }
//}
@Mixin(value = ClaimsManager.class, remap = false)
public abstract class ClaimCreationDenyMixin {

    @Shadow protected IPlayerConfigManager configManager;

    @Shadow protected abstract PlayerChunkClaim getClaimState(UUID id, int subConfigIndex, boolean forceload);

    @Shadow protected abstract PlayerClaimInfoManager<?, ?> getPlayerClaimInfoManager();

    @Shadow public abstract DimensionClaimsManager<?, ?> getDimension(ResourceLocation dimension);

    @Inject(
            method = "claim",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onClaimInject(ResourceLocation dimension, UUID id, int subConfigIndex, int x, int z, boolean forceload, CallbackInfoReturnable<PlayerChunkClaim> cir) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) return;

        ServerPlayer player = server.getPlayerList().getPlayer(id);
        if (player == null) return;

        // Получаем лидера пати (если есть)
        IPartyManagerAPI partyManager = OpenPACServerAPI.get(server).getPartyManager();
        IServerPartyAPI party = partyManager.getPartyByMember(id);

        UUID effectiveId = (party != null) ? party.getOwner().getUUID() : id;

        PlayerChunkClaim claim = getClaimState(effectiveId, subConfigIndex, forceload);


        // Получаем менеджер приватов по измерению
        DimensionClaimsManager<?, ?> dimensionClaims = getDimension(dimension);
        if (dimensionClaims == null) {
            // Нельзя вызвать ensureDimension напрямую, поэтому вызываем через API или ничего не делаем
            return;
        }

        // Получаем менеджер игрока
        PlayerClaimInfoManager<?, ?> infoManager = getPlayerClaimInfoManager();

        // Явно приводим к "сырым" типам — безопасно, так как ты контролируешь весь контекст
        @SuppressWarnings("unchecked")
        PlayerChunkClaim result = ((DimensionClaimsManager) dimensionClaims).claim(
                x, z, claim, infoManager, configManager
        );

        cir.setReturnValue(result);
        cir.cancel();
    }
}






