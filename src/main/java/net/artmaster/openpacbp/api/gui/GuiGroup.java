package net.artmaster.openpacbp.api.gui;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.List;

public record GuiGroup(String titleKey, int count, int limit,  List<String> items) {

    public void render(Font font, GuiGraphics guiGraphics, int startX, MutableInt startY, int color) {
        // Заголовок + количество
        guiGraphics.drawString(font, Component.translatable(titleKey, count, limit), startX, startY.getValue(), color);
        startY.add(font.lineHeight + 2); // смещаем вниз

        // Список элементов
        for (String item : items) {
            guiGraphics.drawString(font, Component.literal(item), startX + 10, startY.getValue(), color); // +10 для отступа
            startY.add(font.lineHeight + 2);
        }

        startY.add(4); // небольшой отступ после блока
    }

    public static class MutableInt {
        private int value;
        public MutableInt(int initial) { this.value = initial; }
        public void add(int delta) { value += delta; }
        public int getValue() { return value; }
    }
}