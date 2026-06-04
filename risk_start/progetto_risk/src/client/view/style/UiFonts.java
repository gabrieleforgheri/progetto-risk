package client.view.style;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Caricamento font Gunship dalla bozza {@code Documenti/projetto finisetre/gunship/}.
 */
public final class UiFonts {
    private static final String GUNSHIP_PATH = "/client/assets/ui/fonts/gunship.ttf";
    private static Font gunshipFamily;

    private UiFonts() {
    }

    public static Font gunship(double size) {
        ensureLoaded();
        return Font.font(gunshipFamily.getFamily(), size);
    }

    public static Font gunshipBold(double size) {
        ensureLoaded();
        return Font.font(gunshipFamily.getFamily(), FontWeight.BOLD, size);
    }

    private static void ensureLoaded() {
        if (gunshipFamily != null) {
            return;
        }
        gunshipFamily = Font.loadFont(UiFonts.class.getResourceAsStream(GUNSHIP_PATH), 12);
        if (gunshipFamily == null) {
            gunshipFamily = Font.font("Arial", 12);
        }
    }
}
