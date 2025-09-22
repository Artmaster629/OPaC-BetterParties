package net.artmaster.openpacbp.mixin;


import net.artmaster.openpacbp.network.Network;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xaero.pac.common.server.parties.party.PartyManager;

@Mixin(value = PartyManager.class, remap = false)
public class CreatePartyMixin {

    @Inject(method = "createPartyForOwner", at = @At("TAIL"))
    public void removePartyInventoryData(Player owner, CallbackInfoReturnable cir) {

        System.out.println("created");
        if (owner instanceof ServerPlayer serverPlayer) {
            Network.sendCommand(serverPlayer, "openpac player-config set parties.name "+"Гильдия "+owner.getName().getString());
        }



    }
}
