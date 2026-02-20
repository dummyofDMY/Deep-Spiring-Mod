package DeepSpiringMod.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;

import DeepSpiringMod.helpers.ModHelper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HiddenStatePower extends AbstractPower {
    // 能力的ID
    public static final String POWER_ID = ModHelper.makePath("HiddenState");
    // 能力的本地化字段
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    // 能力的名称
    private static final String NAME = powerStrings.NAME;
    // 能力的描述
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public static final Logger logger = LogManager.getLogger(HiddenStatePower.class);

    public HiddenStatePower(AbstractCreature owner, int Amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;

        // 如果需要不能叠加的能力，只需将上面的Amount参数删掉，并把下面的Amount改成-1就行
        this.amount = Amount;

        // // 添加一大一小两张能力图
        String path128 = ModHelper.makeImagePath("powers/HiddenState84.png");
        String path48 = ModHelper.makeImagePath("powers/HiddenState32.png");
        this.region128 = new AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 84, 84);
        this.region48 = new AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 32, 32);
        // this.img = new Texture(ModHelper.makeImagePath("powers/RuneIndexPower.png"));

        // 首次添加能力更新描述
        this.updateDescription();
    }

    // // 回合结束时
    // @Override
    // public void atStartOfTurnPostDraw() {
    //     this.addToBot(new FreePlayAction());
    // }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.rawDescription.endsWith(" (Copy)")) {
            logger.info("Card is already a copy, skip copying.");
            return;
        }
        for (int i = 0; i < this.amount; ++i) {
            AbstractCard tmp = card.makeStatEquivalentCopy();
            tmp.exhaust = true;
            tmp.purgeOnUse = true;
            tmp.energyOnUse = tmp.costForTurn;
            tmp.freeToPlayOnce = true;
            tmp.rawDescription = tmp.rawDescription + " (Copy)";

            AbstractDungeon.player.limbo.addToBottom(tmp);
            // 这里方向要随机加个偏置，防止牌叠一块了
            tmp.current_x = card.current_x;
            tmp.current_y = card.current_y;
            tmp.target_x = (Settings.WIDTH / 2.0F * Settings.scale) * MathUtils.random(0.8F, 1.2F);
            tmp.target_y = Settings.HEIGHT / 2.0F * MathUtils.random(0.8F, 1.2F);

            this.addToBot(new NewQueueCardAction(tmp, true, false, true));
        }
        int new_amount = this.amount;
        int Overfitting = ModHelper.get_Overfitting();
        for (int i = 0; i < Overfitting + 1; ++i) {
            new_amount =  (int) Math.floor(new_amount / 2.0);
        }
        logger.info("amount: " + this.amount + ", Overfitting: " + Overfitting + ", new_amount: " + new_amount);
        
        if (new_amount <= 0) {
            this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        } else {
            int decrease_amount = this.amount - new_amount;
            logger.info("decrease_amount: " + decrease_amount);
            this.addToTop(new ApplyPowerAction(owner, owner, new HiddenStatePower(owner, -decrease_amount), -decrease_amount));
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
