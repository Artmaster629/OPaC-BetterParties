package net.artmaster.openpacbp.utils;

import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TooltipUtil {

    /**
     * Преобразует текст с переносами \n в список компонентов для renderTooltip.
     */
    public static List<Component> splitTooltip(String textKey) {
        // Получаем переведённый текст
        String translated = Component.translatable(textKey).getString();
        // Разбиваем по \n и превращаем в компоненты
        return Arrays.stream(translated.split("\\n"))
                .map(Component::literal)
                .collect(Collectors.toList());
    }
}
