package DeepSpiringMod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import DeepSpiringMod.helpers.ModHelper;

public class LossPower extends AbstractPower {
    // 能力的ID
    public static final String POWER_ID = ModHelper.makePath("Loss");
    // 能力的本地化字段
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    // 能力的名称
    private static final String NAME = powerStrings.NAME;
    // 能力的描述
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public LossPower(AbstractCreature owner, int Amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;
        this.canGoNegative = true;

        // 如果需要不能叠加的能力，只需将上面的Amount参数删掉，并把下面的Amount改成-1就行
        this.amount = Amount;

        // // 添加一大一小两张能力图
        String path128 = ModHelper.makeImagePath("powers/Loss84.png");
        String path48 = ModHelper.makeImagePath("powers/Loss32.png");
        this.region128 = new AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 84, 84);
        this.region48 = new AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 32, 32);
        // this.img = new Texture(ModHelper.makeImagePath("powers/RuneIndexPower.png"));

        // 首次添加能力更新描述
        this.updateDescription();
    }

    // @Override
    // public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
    //     this.addToBot(new MakeTempCardInHandAction(new Overflow(), 1));
    // }

    // 能力在更新时如何修改描述
    public void updateDescription() {
        // this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
        this.description = DESCRIPTIONS[0]; // 这样，%d就被替换成能力的层数
        if (this.amount < 0) {
            this.type = PowerType.BUFF;
        } else {
            this.type = PowerType.DEBUFF;
        }
    }

    public void stackPower(int stackAmount) {
		this.fontScale = 8.0F;
		this.amount += stackAmount;
		if (this.amount == 0) {
			this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
		}

		if (this.amount >= 999) {
			this.amount = 999;
		}

		if (this.amount <= -999) {
			this.amount = -999;
		}

   }

   public void reducePower(int reduceAmount) {
		this.fontScale = 8.0F;
		this.amount -= reduceAmount;
		if (this.amount == 0) {
			this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
		}

		if (this.amount >= 999) {
			this.amount = 999;
		}

		if (this.amount <= -999) {
			this.amount = -999;
		}
   }
}