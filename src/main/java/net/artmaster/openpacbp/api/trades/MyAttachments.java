package net.artmaster.openpacbp.api.trades;

import net.artmaster.openpacbp.ModMain;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class MyAttachments {
    public static final AttachmentType<GlobalStorageData> GLOBAL_STORAGE =
            AttachmentType.builder(GlobalStorageData::new)
                    .serialize(new IAttachmentSerializer<CompoundTag, GlobalStorageData>() {
                        @Override
                        public CompoundTag write(GlobalStorageData attachment, HolderLookup.Provider provider) {
                            return attachment.save(provider);
                        }

                        @Override
                        public GlobalStorageData read(IAttachmentHolder holder, CompoundTag tag, HolderLookup.Provider provider) {
                            GlobalStorageData data = new GlobalStorageData();
                            data.load(tag, provider);
                            return data;
                        }
                    })
                    .build();

    public static void register() {
        Registry.register(
                NeoForgeRegistries.ATTACHMENT_TYPES,
                ResourceLocation.fromNamespaceAndPath(ModMain.MODID, "global_storage"),
                GLOBAL_STORAGE
        );
    }
}