package DeepSpiringMod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import DeepSpiringMod.helpers.ModHelper;
import DeepSpiringMod.powers.APPower;

public class UpdateAPAction extends AbstractGameAction {
    public UpdateAPAction() {
    }

    @Override
    public void update() {
        if (AbstractDungeon.player.hasPower(ModHelper.makePath("AP"))) {
            APPower AP = (APPower)AbstractDungeon.player.getPower(ModHelper.makePath("AP"));
            AP.calculate_AP();
            AP.updateDescription();
        }
        this.isDone = true;
    }

}
