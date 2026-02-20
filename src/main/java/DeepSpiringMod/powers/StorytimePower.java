package DeepSpiringMod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import DeepSpiringMod.helpers.ModHelper;

public class StorytimePower extends AbstractPower {
    // 能力的ID
    public static final String POWER_ID = ModHelper.makePath("Storytime");
    // 能力的本地化字段
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    // 能力的名称
    private static final String NAME = powerStrings.NAME;
    // 能力的描述
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public StorytimePower(AbstractCreature owner, int Amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;

        // 如果需要不能叠加的能力，只需将上面的Amount参数删掉，并把下面的Amount改成-1就行
        this.amount = Amount;

        // // 添加一大一小两张能力图
        String path128 = ModHelper.makeImagePath("powers/Storytime84.png");
        String path48 = ModHelper.makeImagePath("powers/Storytime32.png");
        this.region128 = new AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 84, 84);
        this.region48 = new AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 32, 32);
        // this.img = new Texture(ModHelper.makeImagePath("powers/RuneIndexPower.png"));
        // 首次添加能力更新描述
        this.updateDescription();
    }

    // 能力在更新时如何修改描述
    public void updateDescription() {
        // this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
        this.description = String.format(DESCRIPTIONS[0], this.amount); // 这样，%d就被替换成能力的层数
    }

    // @Override
    // public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
    //     if (power instanceof EmbeddingPower && power.amount > 0 && target == this.owner) {
    //         this.flash();
    //         this.addToBot(new DrawCardAction(this.owner, this.amount));
    //     }
    // }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            this.flash();
            int Overfitting_amount = 0;
            if (this.owner.hasPower(ModHelper.makePath("Overfitting"))) {
                Overfitting_amount = this.owner.getPower(ModHelper.makePath("Overfitting")).amount;
            }
            for (int i = 0; i < Overfitting_amount; i++) {
                this.addToBot(new DamageAction(this.owner, new DamageInfo(this.owner, 5 * this.amount, DamageType.NORMAL), AttackEffect.BLUNT_LIGHT));
            }
        }
    }
}