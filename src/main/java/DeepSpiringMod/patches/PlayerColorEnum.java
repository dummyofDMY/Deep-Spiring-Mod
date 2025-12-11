package DeepSpiringMod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
// import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

public class PlayerColorEnum {
    // 修改为你的颜色名称，确保不会与其他mod冲突
    @SpireEnum
    public static AbstractPlayer.PlayerClass Deepfect;

    // ***将CardColor和LibraryType的变量名改为你的角色的颜色名称，确保不会与其他mod冲突***
    // ***并且名称需要一致！***
    @SpireEnum
    public static AbstractCard.CardColor DEEP_BLUE;
}
