package DeepSpiringMod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import DeepSpiringMod.helpers.ModHelper;

public class RegularizationPower extends AbstractPower {
    // 能力的ID
    public static final String POWER_ID = ModHelper.makePath("Regularization");
    // 能力的本地化字段
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    // 能力的名称
    private static final String NAME = powerStrings.NAME;
    // 能力的描述
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public static final Logger logger = LogManager.getLogger(RegularizationPower.class);

    public RegularizationPower(AbstractCreature owner, int Amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;

        // 如果需要不能叠加的能力，只需将上面的Amount参数删掉，并把下面的Amount改成-1就行
        this.amount = Amount;

        // // 添加一大一小两张能力图
        String path128 = ModHelper.makeImagePath("powers/Regularization84.png");
        String path48 = ModHelper.makeImagePath("powers/Regularization32.png");
        this.region128 = new AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 84, 84);
        this.region48 = new AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 32, 32);
        // this.img = new Texture(ModHelper.makeImagePath("powers/RuneIndexPower.png"));
        // 首次添加能力更新描述
        this.updateDescription();
    }

    // 能力在更新时如何修改描述
    public void updateDescription() {
        // this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
        this.description = String.format(DESCRIPTIONS[0]); // 这样，%d就被替换成能力的层数
    }

    @Override public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (power.ID.equals(ModHelper.makePath("Overfitting"))) {
            int remove_amount = power.amount;
            if (remove_amount > 0) {
                logger.info("remove amount: " + remove_amount);
                power.amount = 0;
                // int overfitting_amount = AbstractDungeon.player.getPower(OverfittingPower.POWER_ID).amount;
                AbstractCreature p = this.owner;
                this.addToBot(new ApplyPowerAction(p, p, new OverfittingPower(p, -remove_amount), -remove_amount));
                // if (overfitting_amount == remove_amount) {
                //     logger.info("remove all overfitting");
                //     // this.addToBot(new RemoveSpecificPowerAction(p, p, OverfittingPower.POWER_ID));`
                //     this.addToBot(new ApplyPowerAction(p, p, new OverfittingPower(p, -remove_amount), -remove_amount));
                // } else if (remove_amount < overfitting_amount) {
                //     this.addToBot(new ApplyPowerAction(p, p, new OverfittingPower(p, -remove_amount), -remove_amount));
                // } else {
                //     logger.error("overfitting amount: " + overfitting_amount + ", remove amount: " + remove_amount);
                // }
                if (AbstractDungeon.player.getPower(POWER_ID).amount == 1) {
                    logger.info("remove all Regularization");
                    this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
                } else {
                    this.addToBot(new ApplyPowerAction(p, p, new RegularizationPower(p, -1), -1));
                }
            }
        }
    }

    public void reducePower(int reduceAmount) {
        logger.info("Start to reduce RegularizationPower.\n");
		this.fontScale = 8.0F;
		this.amount -= reduceAmount;
		if (this.amount <= 0) {
			this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
		}

		if (this.amount >= 99) {
			this.amount = 99;
		}

		// if (this.amount <= -99) {
		// 	this.amount = -99;
		// }
    }

    // @Override public void atEndOfTurn(boolean isPlayer) {
    //     if (isPlayer) {
    //         this.addToBot(new com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction(this.owner, this.owner, ModHelper.makePath("Regularization")));
    //     }
    // }
}