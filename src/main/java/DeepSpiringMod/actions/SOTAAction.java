package DeepSpiringMod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;

public class SOTAAction extends AbstractGameAction {
    private AbstractPlayer p;
    private int increaseGold;
    private AbstractCard card;

    public SOTAAction(int increaseGold, AbstractCard card) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.p = AbstractDungeon.player;
        // this.duration = Settings.ACTION_DUR_FAST;
        this.duration = 0.1F;
        this.increaseGold = increaseGold;
        this.card = card;
    }

    @Override
    public void update() {
        if (this.duration == 0.1F) {
            AbstractDungeon.player.gainGold(this.increaseGold);

            for(int i = 0; i < this.increaseGold; ++i) {
                AbstractDungeon.effectList.add(new GainPennyEffect(this.p, this.card.hb.cX, this.card.hb.cY, this.p.hb.cX, this.p.hb.cY, true));
            }
        }

        this.tickDuration();
    }

}

