package me.hippy.prdsz.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.hippy.prdsz.model.Wish;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Сервис по генерации изображения с предсказанием
 *
 * @author hippy
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PictureService {

    private String path = "frogs/";

    public File generatePicture(Wish wish) {
        String content = wish.getContent();
        content = insert(content, System.lineSeparator(), 25);
        File createdFile;

        try {
            int y = 130;
            File file = Path.of(path, wish.getWishPicture().getPath()).toAbsolutePath().toFile();

            BufferedImage bufferedImage = ImageIO.read(file);
            Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();
            graphics.setColor(Color.BLACK);
            graphics.setFont(new Font("Default", Font.PLAIN, 14));
            graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            var strs = content.split(System.lineSeparator());

            for (String str : strs) {
                if (str.startsWith(" ")) {
                    str = str.substring(1);
                }

                graphics.drawString(str, 20, y);
                y += 25;
            }

            createdFile = new File(file.getAbsolutePath().replace(
                    wish.getWishPicture().getPath(),
                    "generated-wish-" + UUID.randomUUID() + ".jpg")
            );

            if (createdFile.createNewFile()) {
                boolean jpg = ImageIO.write(bufferedImage, "jpg", createdFile);
                if (jpg) {
                    return createdFile;
                } else {
                    log.error("Error while trying to create new file");
                }
            } else {
                log.error("Error while trying to create new file");
            }
        } catch (IOException e) {
            log.error("Error while trying to generate image: {}", e.getMessage());
        }

        return null;
    }

    public String insert(String text, String insert, int period) {
        char[] chars = text.toCharArray();
        StringBuilder result = new StringBuilder();
        boolean swap = false;
        int current = 0;

        for (int i = 0; i < chars.length; i++) {
            current++;

            if (i != 0 && i % period == 0 && chars[i] == ' ') {
                result.append(insert);
                continue;
            }

            if (swap && chars[i] == ' ') {
                swap = false;
                result.append(insert);
                current = 0;
                continue;
            }

            if (i != 0 && i % period == 0.0 && chars[i] != ' ') {
                result.append(chars[i]);
                swap = true;
                continue;
            }

            int periodToMuch = period + 3;
            if (current > periodToMuch && swap && Pattern.matches("[а-яА-Я]", String.valueOf(chars[i]))) {
                swap = false;
                result.append(chars[i])
                        .append("-")
                        .append(insert)
                        .append("-");
                current = 0;
                continue;
            }

            result.append(chars[i]);
        }

        return result.toString();
    }
}
