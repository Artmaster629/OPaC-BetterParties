package net.artmaster.openpacbp.utils;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

public class SafeItemStackCodec {
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemStack> CODEC =
            StreamCodec.of(SafeItemStackCodec::encode, SafeItemStackCodec::decode);

    private static void encode(RegistryFriendlyByteBuf buf, ItemStack stack) {
        if (stack.isEmpty()) {
            buf.writeBoolean(false);
        } else {
            buf.writeBoolean(true);
            ItemStack.STREAM_CODEC.encode(buf, stack);
        }
    }

    private static ItemStack decode(RegistryFriendlyByteBuf buf) {
        boolean present = buf.readBoolean();
        if (!present) {
            return ItemStack.EMPTY;
        }
        return ItemStack.STREAM_CODEC.decode(buf);
    }
}
