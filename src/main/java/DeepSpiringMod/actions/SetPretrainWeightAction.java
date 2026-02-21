package DeepSpiringMod.actions;

import java.util.Iterator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import DeepSpiringMod.cards.PreTrainedModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SetPretrainWeightAction extends AbstractGameAction {
    public static final Logger logger = LogManager.getLogger(SetPretrainWeightAction.class);
    int AP_amount;
    int Overfitting_amount;

    public SetPretrainWeightAction(int AP_amount, int Overfitting_amount) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.AP_amount = AP_amount;
        this.Overfitting_amount = Overfitting_amount;
    }

    @Override
    public void update() {
        AbstractCard c;
        Iterator card_it = AbstractDungeon.player.masterDeck.group.iterator();
        while(card_it.hasNext()) {
            c = (AbstractCard)card_it.next();
            if (c instanceof PreTrainedModel) {
                ((PreTrainedModel)c).storage_AP = AP_amount;
                ((PreTrainedModel)c).storage_Overfitting = Overfitting_amount;
                ((PreTrainedModel)c).misc = AP_amount + Overfitting_amount * 100; // 将AP和Overfitting的值编码到misc中，以便在卡牌初始化时恢复
                c.initializeDescription();
            }
        }

        card_it = AbstractDungeon.player.exhaustPile.group.iterator();
        while(card_it.hasNext()) {
            c = (AbstractCard)card_it.next();
            if (c instanceof PreTrainedModel) {
                ((PreTrainedModel)c).storage_AP = AP_amount;
                ((PreTrainedModel)c).storage_Overfitting = Overfitting_amount;
                ((PreTrainedModel)c).misc = AP_amount + Overfitting_amount * 100; // 将AP和Overfitting的值编码到misc中，以便在卡牌初始化时恢复
                c.initializeDescription();
                break;
            }
        }

        card_it = AbstractDungeon.player.discardPile.group.iterator();
        while(card_it.hasNext()) {
            c = (AbstractCard)card_it.next();
            if (c instanceof PreTrainedModel) {
                ((PreTrainedModel)c).storage_AP = AP_amount;
                ((PreTrainedModel)c).storage_Overfitting = Overfitting_amount;
                ((PreTrainedModel)c).misc = AP_amount + Overfitting_amount * 100;
                c.initializeDescription();
                break;
            }
        }

        card_it = AbstractDungeon.player.drawPile.group.iterator();
        while(card_it.hasNext()) {
            c = (AbstractCard)card_it.next();
            if (c instanceof PreTrainedModel) {
                ((PreTrainedModel)c).storage_AP = AP_amount;
                ((PreTrainedModel)c).storage_Overfitting = Overfitting_amount;
                ((PreTrainedModel)c).misc = AP_amount + Overfitting_amount * 100;
                c.initializeDescription();
            }
        }

        card_it = AbstractDungeon.player.hand.group.iterator();
        while(card_it.hasNext()) {
            c = (AbstractCard)card_it.next();
            if (c instanceof PreTrainedModel) {
                ((PreTrainedModel)c).storage_AP = AP_amount;
                ((PreTrainedModel)c).storage_Overfitting = Overfitting_amount;
                ((PreTrainedModel)c).misc = AP_amount + Overfitting_amount * 100;
                c.initializeDescription();
            }
        }
        this.isDone = true;
    }
}
