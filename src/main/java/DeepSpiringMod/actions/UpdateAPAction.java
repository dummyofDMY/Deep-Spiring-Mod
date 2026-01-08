package DeepSpiringMod.actions;

import java.util.Iterator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import DeepSpiringMod.cards.AbstractAPCard;
import DeepSpiringMod.helpers.ModHelper;

public class UpdateAPAction extends AbstractGameAction {
    public UpdateAPAction() {
    }

    @Override
    public void update() {
        int AP_stack = 0, overfitting_stack = 0;
        if (AbstractDungeon.player.hasPower(ModHelper.makePath("AP"))) {
            AP_stack = AbstractDungeon.player.getPower(ModHelper.makePath("AP")).amount;
        }
        if (AbstractDungeon.player.hasPower(ModHelper.makePath("Overfitting")) &&
            !AbstractDungeon.player.hasPower(ModHelper.makePath("SOTA"))) {
            overfitting_stack = AbstractDungeon.player.getPower(ModHelper.makePath("Overfitting")).amount;
        }

        Iterator card_it = AbstractDungeon.player.hand.group.iterator();
        while (card_it.hasNext()) {
            AbstractCard card = (AbstractCard)card_it.next();
            if (card instanceof AbstractAPCard) {
                ((AbstractAPCard)card).update_with_AP(AP_stack, overfitting_stack);
            }
        }

        card_it = AbstractDungeon.player.drawPile.group.iterator();
        while (card_it.hasNext()) {
            AbstractCard card = (AbstractCard)card_it.next();
            if (card instanceof AbstractAPCard) {
                ((AbstractAPCard)card).update_with_AP(AP_stack, overfitting_stack);
            }
        }

        card_it = AbstractDungeon.player.discardPile.group.iterator();
        while (card_it.hasNext()) {
            AbstractCard card = (AbstractCard)card_it.next();
            if (card instanceof AbstractAPCard) {
                ((AbstractAPCard)card).update_with_AP(AP_stack, overfitting_stack);
            }
        }

        this.isDone = true;
    }
}
