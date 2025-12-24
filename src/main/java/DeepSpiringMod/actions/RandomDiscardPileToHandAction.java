package DeepSpiringMod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
// import com.megacrit.cardcrawl.core.Settings;

public class RandomDiscardPileToHandAction extends AbstractGameAction {
    private AbstractPlayer p;

    public RandomDiscardPileToHandAction() {
        this.p = AbstractDungeon.player;
        // this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (p.discardPile.isEmpty()) {
            this.isDone = true;
            return;
        }
        // if (this.duration == Settings.ACTION_DUR_FAST) {
        AbstractCard card = p.discardPile.getRandomCard(AbstractDungeon.cardRandomRng);
        AbstractCard card_copy = card.makeStatEquivalentCopy();
        this.p.hand.addToHand(card_copy);
        this.p.discardPile.removeCard(card);
        // }
        this.isDone = true;
    }

}
