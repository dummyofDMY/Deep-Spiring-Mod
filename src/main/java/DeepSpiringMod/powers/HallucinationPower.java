package DeepSpiringMod.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

import DeepSpiringMod.helpers.ModHelper;
import DeepSpiringMod.actions.FreePlayAction;
import DeepSpiringMod.actions.StopFreePlayAction;

public class HallucinationPower extends AbstractPower {
    // 能力的ID
    public static final String POWER_ID = ModHelper.makePath("Hallucination");
    // 能力的本地化字段
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    // 能力的名称
    private static final String NAME = powerStrings.NAME;
    // 能力的描述
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public HallucinationPower(AbstractCreature owner, int Amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;

        // 如果需要不能叠加的能力，只需将上面的Amount参数删掉，并把下面的Amount改成-1就行
        this.amount = Amount;

        // // 添加一大一小两张能力图
        String path128 = ModHelper.makeImagePath("powers/Iteration84.png");
        String path48 = ModHelper.makeImagePath("powers/Iteration32.png");
        this.region128 = new AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 84, 84);
        this.region48 = new AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 32, 32);
        // this.img = new Texture(ModHelper.makeImagePath("powers/RuneIndexPower.png"));

        // 首次添加能力更新描述
        this.updateDescription();
        this.addToBot(new FreePlayAction());
    }

    // // 回合结束时
    // @Override
    // public void atStartOfTurnPostDraw() {
    //     this.addToBot(new FreePlayAction());
    // }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if (!card.freeToPlayOnce) {
            return;
        }
        if (this.amount > 0) {
            if (card.cost == -1) {
                this.addToBot(new GainEnergyAction(AbstractDungeon.player.energy.energy));
            }
            this.amount -= 1;
            updateDescription();
        }
        if (this.amount <= 0) {
            this.addToBot(new StopFreePlayAction());
            this.addToBot(new RemoveSpecificPowerAction(owner, owner, POWER_ID));
        }
    }

    // 能力在更新时如何修改描述
    public void updateDescription() {
        // this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
        if (this.amount > 1) {
            this.description = String.format(DESCRIPTIONS[1], this.amount); // 这样，%d就被替换成能力的层数
        } else {
            this.description = String.format(DESCRIPTIONS[0], this.amount); // 这样，%d就被替换成能力的层数
        }
    }
}
