package DeepSpiringMod.actions;

import java.util.Iterator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardGroup.CardGroupType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.core.Settings;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import DeepSpiringMod.helpers.ModHelper;

public class TakeDrawPileCardsTop extends AbstractGameAction {
    public static final String ID = ModHelper.makePath("TakeDrawPileCardsTop");
    private static final UIStrings UI_STRINGS = CardCrawlGame.languagePack.getUIString(ID); // 从游戏系统读取本地化资源
    private static final String[] UI_TITLE = UI_STRINGS.TEXT;
    private int stackAmount = 0;
    public static final Logger logger = LogManager.getLogger(TakeDrawPileCardsTop.class);

    public TakeDrawPileCardsTop(int stackAmount) {
        this.stackAmount = stackAmount;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (AbstractDungeon.player.drawPile.size() < stackAmount) {
            this.isDone = true;
            return;
        } else {
            if (this.duration == this.startDuration) {
                logger.info("Start to select cards in draw pile. stackAmount = " + stackAmount);
                CardGroup temp = new CardGroup(CardGroupType.UNSPECIFIED);
                Iterator var6 = AbstractDungeon.player.drawPile.group.iterator();
                AbstractCard c;

                while(var6.hasNext()) {
                    c = (AbstractCard)var6.next();
                    temp.addToTop(c);
                }

                temp.sortAlphabetically(true);
                temp.sortByRarityPlusStatusCardType(false);
                AbstractDungeon.gridSelectScreen.open(temp, stackAmount, true, String.format(UI_TITLE[0], stackAmount));
                this.tickDuration();
            } else {
                // System.out.println("2\n");
                if (AbstractDungeon.gridSelectScreen.selectedCards.size() > 0) {
                    Iterator card_it = AbstractDungeon.gridSelectScreen.selectedCards.iterator();
                    int take_count = 0;
                    while (card_it.hasNext()) {
                        if (take_count >= stackAmount) {
                            logger.error("Warning: take more cards than expected: " + AbstractDungeon.gridSelectScreen.selectedCards.size());
                            break;
                        }
                        AbstractCard card = (AbstractCard)card_it.next();
                        AbstractPlayer p = AbstractDungeon.player;
                        p.drawPile.moveToDeck(card, false);
                        take_count++;
                    }
                }
                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                this.tickDuration();
            }
        }
    }

}
