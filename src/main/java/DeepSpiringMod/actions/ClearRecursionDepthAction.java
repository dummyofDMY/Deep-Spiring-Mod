package DeepSpiringMod.actions;

import java.util.Iterator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

import DeepSpiringMod.powers.RecursionDepth;

public class ClearRecursionDepthAction extends AbstractGameAction {
    public ClearRecursionDepthAction() {
    }

    @Override
    public void update() {
        Iterator power_it = AbstractDungeon.player.powers.iterator();
        AbstractPower power;
        int target_power_stack = 0;
        while (power_it.hasNext()) {
            power = (AbstractPower)power_it.next();
            if (power instanceof RecursionDepth) {
                target_power_stack = power.amount;
                break;
            }
        }
        this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new RecursionDepth(AbstractDungeon.player, -target_power_stack), -target_power_stack));
        this.isDone = true;
    }

}
