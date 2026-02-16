package DeepSpiringMod.actions;

import java.util.Iterator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardGroup.CardGroupType;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.core.Settings;

import DeepSpiringMod.helpers.ModHelper;

public class DiffusionAction extends AbstractGameAction {
    public static final String ID = ModHelper.makePath("DiffusionAction");
    private static final UIStrings UI_STRINGS = CardCrawlGame.languagePack.getUIString(ID); // 从游戏系统读取本地化资源
    private static final String[] UI_TITLE = UI_STRINGS.TEXT;
    private int stackAmount = 0;
    int new_cost;

    public DiffusionAction(int stackAmount, int new_cost) {
        this.stackAmount = stackAmount;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.new_cost = new_cost;
    }

    @Override
    public void update() {
        if (AbstractDungeon.player.drawPile.size() < stackAmount) {
            Iterator var6 = AbstractDungeon.player.drawPile.group.iterator();
            AbstractCard c;
            while(var6.hasNext()) {
                c = (AbstractCard)var6.next();
                AbstractCard tmp = c.makeStatEquivalentCopy();
                if (this.new_cost != -1) {
                    int delta = this.new_cost - tmp.cost;
                    tmp.updateCost(delta);
                }
                AbstractDungeon.player.hand.addToHand(tmp);
            }
            this.isDone = true;
            return;
        } else {
            if (this.duration == this.startDuration) {
                String title;
                // System.err.print("Difusion stacks: " + this.stackAmount + "\n");
                if (this.stackAmount > 1) {
                    title = String.format(UI_TITLE[1], this.stackAmount);
                } else {
                    title = String.format(UI_TITLE[1], this.stackAmount);
                }
                CardGroup temp = new CardGroup(CardGroupType.UNSPECIFIED);
                Iterator var6 = AbstractDungeon.player.drawPile.group.iterator();
                AbstractCard c;

                while(var6.hasNext()) {
                    c = (AbstractCard)var6.next();
                    temp.addToTop(c);
                }

                temp.sortAlphabetically(true);
                temp.sortByRarityPlusStatusCardType(false);
                AbstractDungeon.gridSelectScreen.open(temp, this.stackAmount, title, false);
                
                this.tickDuration();
            } else {
                // Iterator card_it = AbstractDungeon.gridSelectScreen.selectedCards.iterator();
                // System.err.print("Selected: ");
                // while (card_it.hasNext()) {
                //     AbstractCard card = (AbstractCard)card_it.next();
                //     try {
                //         Class<?> clazz = card.getClass();
                //         java.lang.reflect.Field field = clazz.getField("ID");
                //         String ID = (String) field.get(null); // null 因为访问静态字段
                //         System.err.print(ID + ' ');
                //     } catch (Exception e) {
                //         e.printStackTrace();
                //     }
                // }
                // System.err.print('\n');
                // System.err.print("null\n");
                if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                    Iterator c_it = AbstractDungeon.gridSelectScreen.selectedCards.iterator();
                    while (c_it.hasNext()) {
                        AbstractCard card = ((AbstractCard)c_it.next()).makeStatEquivalentCopy();
                        if (this.new_cost != -1) {
                            int delta = this.new_cost - card.cost;
                            card.updateCost(delta);
                        }
                        AbstractDungeon.player.hand.addToHand(card);
                    }
                }
                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                this.tickDuration();
            }
        }
    }

}
