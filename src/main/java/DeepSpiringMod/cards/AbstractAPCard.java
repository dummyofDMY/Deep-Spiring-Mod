package DeepSpiringMod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom.RoomPhase;

import DeepSpiringMod.powers.APPower;
import DeepSpiringMod.powers.OverfittingPower;
import DeepSpiringMod.powers.SOTAPower;
import basemod.abstracts.CustomCard;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AbstractAPCard extends CustomCard{
    public static final Logger logger = LogManager.getLogger(AbstractAPCard.class);

    public AbstractAPCard(String id, String name, String img, int cost, String rawDescription, AbstractCard.CardType type, AbstractCard.CardColor color, AbstractCard.CardRarity rarity, AbstractCard.CardTarget target) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
        // logger.info("Start to init AbstractAPCard.\n");
        // logger.info("AbstractAPCard initialization completed.\n");
    }

    public AbstractAPCard(String id, String name, RegionName img, int cost, String rawDescription, AbstractCard.CardType type, AbstractCard.CardColor color, AbstractCard.CardRarity rarity, AbstractCard.CardTarget target) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
        // logger.info("Start to init AbstractAPCard.\n");
        // logger.info("AbstractAPCard initialization completed.\n");
    }

    public abstract void update_with_AP(int AP, int Overfitting);

    @Override
    public void applyPowers() {
        int AP = 0, Overfitting = 0;
        if (AbstractDungeon.player.hasPower(APPower.POWER_ID)) {
            AP = AbstractDungeon.player.getPower(APPower.POWER_ID).amount;
        }
        if (AbstractDungeon.player.hasPower(OverfittingPower.POWER_ID) && !AbstractDungeon.player.hasPower(SOTAPower.POWER_ID)) {
            Overfitting = AbstractDungeon.player.getPower(OverfittingPower.POWER_ID).amount;
        }
        Overfitting = Math.max(0, Overfitting);
        // logger.info("Applying powers with AP: " + AP + " and Overfitting: " + Overfitting + "\n");
        update_with_AP(AP, Overfitting);
        super.applyPowers();
    }

    public int[] get_AP() {
        int AP = 0, Overfitting = 0;
        // 这里在游戏初始化的时候有可能player还没有创建好，所以要加个判断
        if (AbstractDungeon.player == null) {
            return new int[]{AP, Overfitting};
        }
        try {
            if (AbstractDungeon.getCurrRoom().isBattleOver) {
                return new int[]{AP, Overfitting};
            }
        } catch (NullPointerException e) {
            logger.warn("Current room is null when trying to get AP. This might happen during game initialization. Returning AP as 0.\n");
            // return new int[]{AP, Overfitting};
        }
        if (AbstractDungeon.player.hasPower(APPower.POWER_ID)) {
            AP = AbstractDungeon.player.getPower(APPower.POWER_ID).amount;
        }
        if (AbstractDungeon.player.hasPower(OverfittingPower.POWER_ID) && !AbstractDungeon.player.hasPower(SOTAPower.POWER_ID)) {
            Overfitting = AbstractDungeon.player.getPower(OverfittingPower.POWER_ID).amount;
        }
        Overfitting = Math.max(0, Overfitting);
        return new int[]{AP, Overfitting};
    }
}
