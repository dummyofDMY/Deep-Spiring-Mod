package DeepSpiringMod.actions;

import java.util.Iterator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;

import DeepSpiringMod.cards.SOTA;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SOTAAction extends AbstractGameAction {
    private AbstractPlayer p;
    private int increaseGold;
    private AbstractCard card;
    // private UUID uuid;
    private int SOTA_num;
    public static final Logger logger = LogManager.getLogger(SOTAAction.class);

    public SOTAAction(int increaseGold, AbstractCard card, int SOTA_num) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.p = AbstractDungeon.player;
        // this.duration = Settings.ACTION_DUR_FAST;
        this.duration = 0.1F;
        this.increaseGold = increaseGold;
        // this.uuid = targetUUID;
        this.SOTA_num = SOTA_num;
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

        Iterator var1 = AbstractDungeon.player.masterDeck.group.iterator();

        AbstractCard c;
        boolean found = false;
        while(var1.hasNext()) {
            c = (AbstractCard)var1.next();
            if (c instanceof SOTA) {
                c.misc = this.SOTA_num;
                c.initializeDescription();
                found = true;
            }
        }
        logger.info("found SOTA in master desk: " + found);

        if (AbstractDungeon.player.cardInUse instanceof SOTA) {
            AbstractDungeon.player.cardInUse.misc = this.SOTA_num;
            AbstractDungeon.player.cardInUse.initializeDescription();
        }

        Iterator var2 = AbstractDungeon.player.drawPile.group.iterator();

        while(var2.hasNext()) {
            c = (AbstractCard)var2.next();
            if (c instanceof SOTA) {
                c.misc = this.SOTA_num;
                c.initializeDescription();
            }
        }

        var2 = AbstractDungeon.player.discardPile.group.iterator();

        while(var2.hasNext()) {
            c = (AbstractCard)var2.next();
            if (c instanceof SOTA) {
                c.misc = this.SOTA_num;
                c.initializeDescription();
            }
        }

        var2 = AbstractDungeon.player.exhaustPile.group.iterator();

        while(var2.hasNext()) {
            c = (AbstractCard)var2.next();
            if (c instanceof SOTA) {
                c.misc = this.SOTA_num;
                c.initializeDescription();
            }
        }

        var2 = AbstractDungeon.player.limbo.group.iterator();

        while(var2.hasNext()) {
            c = (AbstractCard)var2.next();
            if (c instanceof SOTA) {
                c.misc = this.SOTA_num;
                c.initializeDescription();
            }
        }

        var2 = AbstractDungeon.player.hand.group.iterator();

        while(var2.hasNext()) {
            c = (AbstractCard)var2.next();
            if (c instanceof SOTA) {
                c.misc = this.SOTA_num;
                c.initializeDescription();
            }
        }

        this.tickDuration();
    }

}

