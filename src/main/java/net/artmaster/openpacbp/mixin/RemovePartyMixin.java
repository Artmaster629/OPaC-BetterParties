package net.artmaster.openpacbp.mixin;


import net.artmaster.openpacbp.api.trades.StorageManager;
import net.minecraft.server.MinecraftServer;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xaero.pac.common.server.parties.party.PartyManager;
import xaero.pac.common.server.parties.party.ServerParty;

@Mixin(value = PartyManager.class, remap = false)
public class RemovePartyMixin {

    @Inject(method = "removeTypedParty", at = @At("TAIL"))
    public void removePartyInventoryData(ServerParty party, CallbackInfo ci) {

        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) return;


        StorageManager.removeParty(server, party.getId());
    }
}
