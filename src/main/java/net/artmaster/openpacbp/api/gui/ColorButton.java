package net.artmaster.openpacbp.api.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public class ColorButton extends AbstractWidget {
    private final ResourceLocation texture;
    private final Consumer<ColorButton> onPress;

    public ColorButton(int x, int y, int width, int height,
                               ResourceLocation texture,
                               Component message,
                               Consumer<ColorButton> onPress) {
        super(x, y, width, height, message);
        this.texture = texture;
        this.onPress = onPress;
    }


    @Override
    public void onClick(double mouseX, double mouseY) {
        onPress.accept(this);
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        guiGraphics.blit(texture, getX(), getY(), 0, 0, width, height, width, height);
        int textY = getY() + height - 45;
        guiGraphics.drawCenteredString(
                Minecraft.getInstance().font,
                getMessage(),
                getX() + width / 2,
                textY,
                0xFFFFFF
        );

    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narration) {
    }
}
