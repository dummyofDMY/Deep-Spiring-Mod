package DeepSpiringMod.actions;

import java.util.Iterator;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.core.Settings;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import DeepSpiringMod.helpers.ModHelper;

public class PlayCardsInDiscardPileAction extends AbstractGameAction {
    public static final String ID = ModHelper.makePath("PlayCardsInDiscardPileAction");
    private static final UIStrings UI_STRINGS = CardCrawlGame.languagePack.getUIString(ID); // 从游戏系统读取本地化资源
    private static final String[] UI_TITLE = UI_STRINGS.TEXT;
    private int stackAmount = 0;
    public static final Logger logger = LogManager.getLogger(PlayCardsInDiscardPileAction.class);

    public PlayCardsInDiscardPileAction(int stackAmount) {
        this.stackAmount = stackAmount;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (AbstractDungeon.player.discardPile.size() < stackAmount) {
            Iterator var6 = AbstractDungeon.player.discardPile.group.iterator();
            AbstractCard c;
            while(var6.hasNext()) {
                c = (AbstractCard)var6.next();
                AbstractDungeon.player.hand.addToHand(c);
            }
            this.isDone = true;
            return;
        } else {
            if (this.duration == this.startDuration) {
                logger.info("Start to select cards in discard pile. stackAmount = " + stackAmount);
                AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.discardPile, stackAmount, true, String.format(UI_TITLE[0], stackAmount));
                this.tickDuration();
            } else {
                // System.out.println("2\n");
                if (AbstractDungeon.gridSelectScreen.selectedCards.size() > 0) {
                    Iterator card_it = AbstractDungeon.gridSelectScreen.selectedCards.iterator();
                    int play_count = 0;
                    while (card_it.hasNext()) {
                        if (play_count >= stackAmount) {
                            logger.error("Warning: play more cards than expected: " + AbstractDungeon.gridSelectScreen.selectedCards.size());
                            break;
                        }
                        AbstractCard card = (AbstractCard)card_it.next();
                        AbstractCard tmp = card.makeStatEquivalentCopy();
                        tmp.exhaust = true;
                        tmp.purgeOnUse = true;
                        tmp.energyOnUse = tmp.costForTurn;

                        AbstractDungeon.player.limbo.addToBottom(tmp);
                        tmp.current_x = card.current_x;
                        tmp.current_y = card.current_y;
                        // 这里方向要随机加个偏置，防止牌叠一块了
                        tmp.target_x = (Settings.WIDTH / 2.0F * Settings.scale) * MathUtils.random(0.8F, 1.2F);
                        tmp.target_y = Settings.HEIGHT / 2.0F * MathUtils.random(0.8F, 1.2F);

                        this.addToBot(new NewQueueCardAction(tmp, true, false, true));
                        play_count++;
                    }
                }
                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                this.tickDuration();
            }
        }
    }

}
