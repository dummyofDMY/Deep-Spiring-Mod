package DeepSpiringMod.actions;

import java.util.Iterator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import DeepSpiringMod.cards.AbstractAPCard;
import DeepSpiringMod.helpers.ModHelper;
import DeepSpiringMod.powers.APPower;

public class UpdateAPAction extends AbstractGameAction {
    public UpdateAPAction() {
    }

    @Override
    public void update() {
        if (!AbstractDungeon.player.hasPower(ModHelper.makePath("AP"))) {
            AbstractDungeon.player.addPower(new APPower(AbstractDungeon.player));
        }
        APPower AP = (APPower)AbstractDungeon.player.getPower(ModHelper.makePath("AP"));
        AP.calculate_AP();
        AP.updateDescription();

        Iterator card_it = AbstractDungeon.player.hand.group.iterator();
        while (card_it.hasNext()) {
            AbstractCard card = (AbstractCard)card_it.next();
            if (card instanceof AbstractAPCard) {
                ((AbstractAPCard)card).update_with_AP(AP.AP);
            }
        }

        card_it = AbstractDungeon.player.drawPile.group.iterator();
        while (card_it.hasNext()) {
            AbstractCard card = (AbstractCard)card_it.next();
            if (card instanceof AbstractAPCard) {
                ((AbstractAPCard)card).update_with_AP(AP.AP);
            }
        }

        card_it = AbstractDungeon.player.discardPile.group.iterator();
        while (card_it.hasNext()) {
            AbstractCard card = (AbstractCard)card_it.next();
            if (card instanceof AbstractAPCard) {
                ((AbstractAPCard)card).update_with_AP(AP.AP);
            }
        }

        this.isDone = true;
    }
}
