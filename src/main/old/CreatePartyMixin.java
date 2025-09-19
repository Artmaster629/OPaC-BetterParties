//package net.artmaster.openpacbp.mixin;
//
//
//import com.mojang.logging.LogUtils;
//import net.artmaster.openpacbp.api.quests.PartyStorageManager;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.server.MinecraftServer;
//import net.minecraft.server.level.ServerPlayer;
//import net.neoforged.neoforge.server.ServerLifecycleHooks;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//import xaero.pac.common.claims.player.PlayerChunkClaim;
//import xaero.pac.common.claims.player.api.IPlayerChunkClaimAPI;
//import xaero.pac.common.claims.player.api.IPlayerClaimPosListAPI;
//import net.minecraft.server.MinecraftServer;
//import net.minecraft.server.level.ServerPlayer;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.level.ChunkPos;
//import xaero.pac.common.server.api.OpenPACServerAPI;
//import xaero.pac.common.claims.player.api.IPlayerDimensionClaimsAPI;
//import xaero.pac.common.parties.party.Party;
//import xaero.pac.common.parties.party.member.PartyMemberRank;
//import xaero.pac.common.server.api.OpenPACServerAPI;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//@Mixin(value = Party.class, remap = false)
//public class CreatePartyMixin {
//
//    @Inject(method = "addMember*", at = @At("TAIL"))
//    public void rewriteChunkForPartyOwner(UUID memberUUID, PartyMemberRank rank, String playerUsername, CallbackInfo ci) {
//        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
//        if (server == null) return;
//
//        ServerPlayer player = server.getPlayerList().getPlayer(memberUUID);
//        if (player == null) return;
//
//        var claimManager = OpenPACServerAPI.get(server).getServerClaimsManager();
//        var partyManager = OpenPACServerAPI.get(server).getPartyManager();
//
//
//
//
//
//        var party = partyManager.getPartyByMember(memberUUID);
//        if (party == null) return;
//
//        var playerInfo = claimManager.getPlayerInfo(memberUUID);
//        if (playerInfo == null) return;
//
//// Получаем поток "измерение -> клеймы"
//        if (playerInfo == null) return;
//
//// Получаем все измерения игрока
//        playerInfo.getStream().forEach(dimEntry -> {
//            ResourceLocation dimensionId = dimEntry.getKey();
//            var dimClaims = dimEntry.getValue(); // IPlayerDimensionClaimsAPI или похожий
//
//            var dimensionClaims = playerInfo.getDimension(dimensionId);
//            if (dimensionClaims == null) return;
//
//
//
//
//
//            // перебираем все списки (pos lists) в измерении
//            dimClaims.getStream().forEach(posList -> {
//                // не обязательно знать тип ClaimState — используем var
//
//                // posList.getStream() — элементы могут быть Long или объект-позиция
//                posList.getStream().forEach(posObj -> {
//                    try {
//                        int chunkX;
//                        int chunkZ;
//
//                        if (posObj instanceof ChunkPos cp) {
//                            chunkX = cp.x;
//                            chunkZ = cp.z;
//                        } else if (posObj instanceof Long l) {
//                            long packed = l;
//                            chunkX = ChunkPos.getX(packed);
//                            chunkZ = ChunkPos.getZ(packed);
//                        } else {
//                            System.out.println("Unknown posObj type: " + posObj.getClass().getName());
//                            return;
//                        }
//
//                        var claimState = posList.getClaimState();
//                        System.out.printf("Claim at chunk %d, %d (state=%s)%n",
//                                chunkX, chunkZ, claimState == null ? "null" : claimState.toString());
//
//                        // Пример: переписать клейм на ownerId
//                        // claimManager.unclaim(dimensionId, memberUUID, chunkX, chunkZ);
//                        // claimManager.claim(dimensionId, ownerUUID, chunkX, chunkZ, false);
//
//                    } catch (Throwable t) {
//                        t.printStackTrace();
//                    }
//                });
//            });
//        });
//    }
//}
