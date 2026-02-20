package DeepSpiringMod.actions;

import java.util.Iterator;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
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
    public boolean isExhaust = false;

    public PlayCardsInDiscardPileAction(int stackAmount, boolean isExhaust) {
        this.stackAmount = stackAmount;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.isExhaust = isExhaust;
    }

    @Override
    public void update() {
        if (AbstractDungeon.player.discardPile.size() == 0) {
            this.isDone = true;
            return;
        }
        if (this.duration == this.startDuration) {
            logger.info("Start to select cards in discard pile. stackAmount = " + stackAmount);
            AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.discardPile, stackAmount, true, String.format(UI_TITLE[0], stackAmount));
            this.tickDuration();
        } else {
            // System.out.println("2\n");
            if (AbstractDungeon.gridSelectScreen.selectedCards.size() > 0) {
                logger.info("Selected " + AbstractDungeon.gridSelectScreen.selectedCards.size() + " cards in discard pile to play.\n");
                Iterator card_it = AbstractDungeon.gridSelectScreen.selectedCards.iterator();
                int play_count = 0;
                while (card_it.hasNext()) {
                    if (play_count >= stackAmount) {
                        logger.error("Warning: play more cards than expected: " + AbstractDungeon.gridSelectScreen.selectedCards.size());
                        break;
                    }
                    
                    // AbstractCard card = (AbstractCard)card_it.next();
                    // AbstractCard tmp = card.makeStatEquivalentCopy();
                    // tmp.exhaust = true;
                    // tmp.purgeOnUse = true;
                    // tmp.energyOnUse = tmp.costForTurn;

                    // AbstractDungeon.player.limbo.addToBottom(tmp);
                    // tmp.current_x = card.current_x;
                    // tmp.current_y = card.current_y;
                    // // 这里方向要随机加个偏置，防止牌叠一块了
                    // tmp.target_x = (Settings.WIDTH / 2.0F * Settings.scale) * MathUtils.random(0.8F, 1.2F);
                    // tmp.target_y = Settings.HEIGHT / 2.0F * MathUtils.random(0.8F, 1.2F);

                    // this.addToBot(new NewQueueCardAction(tmp, true, false, true));

                    // AbstractCard card = (AbstractCard)card_it.next();
                    // AbstractDungeon.player.discardPile.moveToExhaustPile(card);
                    // AbstractDungeon.getCurrRoom().souls.remove(card);
                    // card.exhaustOnUseOnce = this.isExhaust;
                    // AbstractDungeon.player.limbo.group.add(card);
                    // card.current_y = -200.0F * Settings.scale;
                    // card.target_x = (float)Settings.WIDTH / 2.0F + 200.0F * Settings.xScale;
                    // card.target_y = (float)Settings.HEIGHT / 2.0F;
                    // card.targetAngle = 0.0F;
                    // card.lighten(false);
                    // card.drawScale = 0.12F;
                    // card.targetDrawScale = 0.75F;
                    // card.applyPowers();
                    // this.addToTop(new NewQueueCardAction(card, this.target, false, true));
                    // this.addToTop(new UnlimboAction(card));
                    // this.addToTop(new WaitAction(Settings.ACTION_DUR_FASTER));

                    AbstractCard card = (AbstractCard)card_it.next();
                    AbstractCard tmp = card.makeSameInstanceOf();
                    AbstractDungeon.player.discardPile.moveToExhaustPile(card);
                    tmp.exhaust = true;
                    tmp.purgeOnUse = true;
                    tmp.energyOnUse = tmp.costForTurn;
                    tmp.freeToPlayOnce = false;

                    AbstractDungeon.player.limbo.addToBottom(tmp);
                    // 这里方向要随机加个偏置，防止牌叠一块了
                    tmp.current_x = card.current_x;
                    tmp.current_y = card.current_y;
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
