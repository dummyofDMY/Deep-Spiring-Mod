package DeepSpiringMod.helpers;

import com.megacrit.cardcrawl.cards.AbstractCard;

import DeepSpiringMod.cards.StrikeDataset;

public class ModHelper {
    public static String makePath(String id) {
        return "DeepSpiringMod:" + id;
    }

    public static String makeImagePath(String path) {
        return "DeepSpiringModResources/img/" + path;
    }

    public static String makeLocalizationPath(String path) {
        return "DeepSpiringModResources/localization/" + path;
    }

    public static boolean isMultipleHits(AbstractCard card) {
        if (card instanceof StrikeDataset) {
            return true;
        }
        else {
            return false;
        }
    }
}

