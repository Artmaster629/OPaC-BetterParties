package net.artmaster.openpacbp.mixin;

import net.artmaster.openpacbp.gui.PartyGUIRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xaero.pac.client.ClientTickHandler;
import xaero.pac.client.api.OpenPACClientAPI;
import xaero.pac.client.event.ClientEvents;
import xaero.pac.client.parties.party.IClientParty;
import xaero.pac.client.parties.party.api.IClientPartyAPI;
import xaero.pac.client.parties.party.api.IClientPartyStorageAPI;
import xaero.pac.common.parties.party.Party;

@Mixin(ClientTickHandler.class) // <- реальный класс, куда делегирует ClientEvents
public class MixinOPACClientTickHandler {

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Minecraft;setScreen(Lnet/minecraft/client/gui/screens/Screen;)V"
            ),
            cancellable = true
    )
    private void replaceMenu(CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        assert player != null;
        IClientPartyStorageAPI partyManager = OpenPACClientAPI.get().getClientPartyStorage();
        IClientPartyAPI party = partyManager.getParty();
        if (party == null) {
            player.displayClientMessage(Component.translatable("text.openpacbp.no_party_pm"), false);
            ci.cancel();
            return;
        }
        mc.setScreen(new PartyGUIRenderer());
        ci.cancel(); // отменить открытие оригинального меню OPaC
    }


}
