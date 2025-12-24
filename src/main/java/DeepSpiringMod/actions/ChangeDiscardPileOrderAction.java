package DeepSpiringMod.actions;

import java.util.Iterator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.core.Settings;

import DeepSpiringMod.helpers.ModHelper;

public class ChangeDiscardPileOrderAction extends AbstractGameAction {
    public static final String ID = ModHelper.makePath("ChangeDiscardPileOrderAction");
    private static final UIStrings UI_STRINGS = CardCrawlGame.languagePack.getUIString(ID); // 从游戏系统读取本地化资源
    private static final String[] UI_TITLE = UI_STRINGS.TEXT;
    private int stackAmount = 0;

    public ChangeDiscardPileOrderAction(int stackAmount) {
        this.stackAmount = stackAmount;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (AbstractDungeon.player.discardPile.size() < 2) {
            this.isDone = true;
            return;
        } else {
            for (int i = 0; i < this.stackAmount; ++i) {
                if (this.duration == this.startDuration) {
                    AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.discardPile, 2, true, UI_TITLE[0]);
                    this.tickDuration();
                } else {
                    // System.out.println("2\n");
                    if (AbstractDungeon.gridSelectScreen.selectedCards.size() == 2) {
                        AbstractCard card1 = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
                        AbstractCard card2 = AbstractDungeon.gridSelectScreen.selectedCards.get(1);
                        
                        int index1 = AbstractDungeon.player.discardPile.group.indexOf(card1);
                        int index2 = AbstractDungeon.player.discardPile.group.indexOf(card2);
                        if (index1 == -1 || index2 == -1) {
                            System.err.println("警告：卡牌不在牌堆中");
                        }
                        AbstractDungeon.player.discardPile.group.set(index1, card2);
                        AbstractDungeon.player.discardPile.group.set(index2, card1);
                    } else {
                        System.err.println("Wrong select number: " + AbstractDungeon.gridSelectScreen.selectedCards.size() + '\n');
                        Iterator card_it = AbstractDungeon.gridSelectScreen.selectedCards.iterator();
                        System.err.print("Selected: ");
                        while (card_it.hasNext()) {
                            AbstractCard card = (AbstractCard)card_it.next();
                            try {
                                Class<?> clazz = card.getClass();
                                java.lang.reflect.Field field = clazz.getField("ID");
                                String ID = (String) field.get(null); // null 因为访问静态字段
                                System.err.print(ID + ' ');
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        System.err.print('\n');
                    }
                    AbstractDungeon.gridSelectScreen.selectedCards.clear();
                    this.tickDuration();
                }
            }
        }
    }

}
