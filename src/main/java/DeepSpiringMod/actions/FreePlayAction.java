package DeepSpiringMod.actions;

import java.util.Iterator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.core.Settings;

public class FreePlayAction extends AbstractGameAction {
    private AbstractPlayer p;

    public FreePlayAction() {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.p = AbstractDungeon.player;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            Iterator var1 = this.p.hand.group.iterator();
            // System.out.print("Free play!\n");

            while(var1.hasNext()) {
                AbstractCard c = (AbstractCard)var1.next();
                c.freeToPlayOnce = true;
                // if (c.costForTurn > 0) {
                // c.costForTurn = 0;
                // c.isCostModifiedForTurn = true;
                // }

                // if (this.forCombat && c.cost > 1) {
                // c.cost = 1;
                // c.isCostModified = true;
                // }
            }

            var1 = this.p.discardPile.group.iterator();
            while(var1.hasNext()) {
                AbstractCard c = (AbstractCard)var1.next();
                c.freeToPlayOnce = true;
            }

            var1 = this.p.drawPile.group.iterator();
            while(var1.hasNext()) {
                AbstractCard c = (AbstractCard)var1.next();
                c.freeToPlayOnce = true;
            }
        }

        this.tickDuration();
    }

}
