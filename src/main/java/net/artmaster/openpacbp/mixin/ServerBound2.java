package net.artmaster.openpacbp.mixin;

import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xaero.pac.common.claims.player.request.ClaimActionRequest;
import xaero.pac.common.claims.result.api.AreaClaimResult;
import xaero.pac.common.claims.result.api.ClaimResult;
import xaero.pac.common.server.api.OpenPACServerAPI;
import xaero.pac.common.server.claims.ServerClaimsManager;
import xaero.pac.common.server.claims.player.request.PlayerClaimActionRequestHandler;
import xaero.pac.common.server.parties.party.api.IPartyManagerAPI;
import xaero.pac.common.server.parties.party.api.IServerPartyAPI;
import xaero.pac.common.server.player.config.api.IPlayerConfigAPI;
import xaero.pac.common.server.player.config.api.IPlayerConfigManagerAPI;
import xaero.pac.common.server.player.data.ServerPlayerData;
import xaero.pac.common.server.player.data.api.ServerPlayerDataAPI;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;


@Mixin(PlayerClaimActionRequestHandler.class)
public class ServerBound2 {

    @Shadow
    @Final
    private ServerClaimsManager manager;


    @Inject(method = "onReceive", at = @At("HEAD"), cancellable = true)
    private void beforeReceive(ServerPlayer player, ClaimActionRequest request, CallbackInfo ci) {
        // Получаем Shadow-поле

        ServerClaimsManager mgr = this.manager;

        // Определяем effective UUID через пати




        OpenPACServerAPI api = OpenPACServerAPI.get(Objects.requireNonNull(player.getServer()));
        IPartyManagerAPI partyManager = api.getPartyManager();
        IServerPartyAPI party = partyManager.getPartyByMember(player.getUUID());


        UUID effectiveId = player.getUUID();
        if (party != null && party.getOwner() != null) {
            effectiveId = party.getOwner().getUUID();
        }

//        MinecraftServer server = player.server;
//        int ownerLimit = OpenPacUtils.getEffectiveChunkLimit(server, effectiveId);
//        int totalClaims = manager.getPlayerInfo(effectiveId).getClaimCount();
//
//
//        System.out.println(totalClaims+" >= "+ownerLimit);
//
//        if (totalClaims >= ownerLimit) {
//            player.displayClientMessage(
//                    Component.translatable("text.openpacbp.party_limit_reached"),
//                    false
//            );
//            ci.cancel();
//            return;
//        }


        // Вся логика на основе manager
        ServerPlayerData playerData = (ServerPlayerData) ServerPlayerDataAPI.from(player);
        boolean shouldServerClaim = request.isByServer();
        if (playerData.isClaimsServerMode()) shouldServerClaim = true;



        if (shouldServerClaim && mgr.getPermissionHandler().shouldPreventServerClaim(player, playerData, player.getServer())) {
            mgr.getClaimsManagerSynchronizer().syncToPlayerClaimActionResult(
                    new AreaClaimResult(Set.of(ClaimResult.Type.NO_SERVER_PERMISSION),
                            request.getLeft(), request.getTop(), request.getRight(), request.getBottom()),
                    player
            );
            ci.cancel();
            return;
        }

        mgr.getPermissionHandler().ensureAdminModeStatusPermission(player, playerData);

        IPlayerConfigManagerAPI cfgManager = api.getPlayerConfigs();
        IPlayerConfigAPI playerCfg = cfgManager.getLoadedConfig(effectiveId);
        IPlayerConfigAPI usedSubConfig = shouldServerClaim ?
                playerCfg.getUsedServerSubConfig() :
                playerCfg.getUsedSubConfig();

        int subConfigIndex = usedSubConfig.getSubIndex();
        int fromX = player.chunkPosition().x;
        int fromZ = player.chunkPosition().z;

        AreaClaimResult result = mgr.tryClaimActionOverArea(
                player.level().dimension().location(),
                effectiveId,
                subConfigIndex,
                fromX,
                fromZ,
                request.getLeft(),
                request.getTop(),
                request.getRight(),
                request.getBottom(),
                request.getAction(),
                playerData.isClaimsAdminMode()
        );

        mgr.getClaimsManagerSynchronizer().syncToPlayerClaimActionResult(result, player);

        ci.cancel();
    }

//    private PlayerChunkClaim redirectClaimForParty(ClaimsManager<?, ?, ?, ?, ?> manager,
//                                                   ResourceLocation dimension,
//                                                   UUID id, // оригинальный UUID (члена, который шлёт пакет)
//                                                   int subConfigIndex,
//                                                   int x, int z,
//                                                   boolean forceload) {
//        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
//        if (server == null) {
//            // если сервера нет — fallback к оригиналу
//            return manager.claim(dimension, id, subConfigIndex, x, z, forceload);
//        }
//
//        ServerPlayer sender = server.getPlayerList().getPlayer(id);
//        if (sender == null) {
//            return manager.claim(dimension, id, subConfigIndex, x, z, forceload);
//        }
//
//        // Ищем пати и заменяем id на ownerId если нужно
//        OpenPACServerAPI api = OpenPACServerAPI.get(server);
//        IPartyManagerAPI partyManager = api.getPartyManager();
//        IServerPartyAPI party = partyManager.getPartyByMember(id);
//
//        UUID effectiveId = id;
//        if (party != null && party.getOwner() != null) {
//            effectiveId = party.getOwner().getUUID();
//        }
//
//        // Вызываем оригинальный метод уже с effectiveId
//        return manager.claim(dimension, effectiveId, subConfigIndex, x, z, forceload);
//    }
}
