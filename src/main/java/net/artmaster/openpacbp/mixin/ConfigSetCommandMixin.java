package net.artmaster.openpacbp.mixin;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import xaero.pac.common.server.command.ConfigSetCommand;

@Mixin(ConfigSetCommand.class)
public abstract class ConfigSetCommandMixin {

    // Перехватываем вызов StringArgumentType.string() и подсовываем greedyString()
    @Redirect(
            method = "addValueArgumentIfNeeded",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/brigadier/arguments/StringArgumentType;string()Lcom/mojang/brigadier/arguments/StringArgumentType;"
            )
    )
    private StringArgumentType replaceStringArg() {
        return StringArgumentType.greedyString();
    }
}

