package DeepSpiringMod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MoveToExhaustAction extends AbstractGameAction {
    public static final Logger logger = LogManager.getLogger(MoveToExhaustAction.class);
    private CardGroup group;
    private AbstractCard card;

    public MoveToExhaustAction(CardGroup group, AbstractCard card) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.group = group;
        this.card = card;
    }

    @Override
    public void update() {
        group.moveToExhaustPile(card);
        this.isDone = true;
    }

}
