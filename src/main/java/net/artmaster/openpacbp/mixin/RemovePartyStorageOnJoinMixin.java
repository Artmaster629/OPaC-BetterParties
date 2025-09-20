package net.artmaster.openpacbp.mixin;


import net.artmaster.openpacbp.api.trades.GlobalStorageData;
import net.artmaster.openpacbp.api.trades.MyAttachments;
import net.artmaster.openpacbp.api.trades.StorageManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xaero.pac.common.parties.party.member.PartyMember;
import xaero.pac.common.server.parties.party.PartyManager;
import xaero.pac.common.server.parties.party.ServerParty;

@Mixin(value = PartyManager.class, remap = false)
public class RemovePartyStorageOnJoinMixin {


    @Inject(method = "onMemberAdded", at = @At("HEAD"))
    public void removePartyInventoryDataOnJoin(ServerParty party, PartyMember m, CallbackInfo ci) {


        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) return;

        ServerLevel serverLevel = server.overworld(); // можно выбрать конкретный уровень, если не только Overworld
        if (serverLevel == null) return;


        if (StorageManager.getPartyInventory(server, m.getUUID()) == null) return;
        StorageManager.removeParty(server, party.getId());
    }


}
