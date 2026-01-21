package DeepSpiringMod.helpers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import DeepSpiringMod.cards.StrikeDataset;
import DeepSpiringMod.powers.APPower;
import DeepSpiringMod.powers.OverfittingPower;
import DeepSpiringMod.powers.SOTAPower;

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

    public static int get_delta_AP() {
        int AP = 0, Overfitting = 0;
        // 这里在游戏初始化的时候有可能player还没有创建好，所以要加个判断
        if (AbstractDungeon.player == null) {
            return 0;
        }
        if (AbstractDungeon.player.hasPower(APPower.POWER_ID)) {
            AP = AbstractDungeon.player.getPower(APPower.POWER_ID).amount;
        }
        if (AbstractDungeon.player.hasPower(OverfittingPower.POWER_ID) && !AbstractDungeon.player.hasPower(SOTAPower.POWER_ID)) {
            Overfitting = AbstractDungeon.player.getPower(OverfittingPower.POWER_ID).amount;
        }
        Overfitting = Math.max(0, Overfitting);
        return AP - Overfitting;
    }
}

