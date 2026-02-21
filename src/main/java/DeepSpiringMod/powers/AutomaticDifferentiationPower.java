package DeepSpiringMod.powers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.core.Settings;

import DeepSpiringMod.cards.BackPropagation;
import DeepSpiringMod.cards.ForwardPropagation;
import DeepSpiringMod.helpers.ModHelper;

public class AutomaticDifferentiationPower extends AbstractPower {
    // 能力的ID
    public static final String POWER_ID = ModHelper.makePath("AutomaticDifferentiation");
    // 能力的本地化字段
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    // 能力的名称
    private static final String NAME = powerStrings.NAME;
    // 能力的描述
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public static final Logger logger = LogManager.getLogger(AutomaticDifferentiationPower.class);

    public AutomaticDifferentiationPower(AbstractCreature owner, int Amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;

        // 如果需要不能叠加的能力，只需将上面的Amount参数删掉，并把下面的Amount改成-1就行
        this.amount = Amount;

        // // 添加一大一小两张能力图
        String path128 = ModHelper.makeImagePath("powers/AutomaticDifferentiation84.png");
        String path48 = ModHelper.makeImagePath("powers/AutomaticDifferentiation32.png");
        this.region128 = new AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 84, 84);
        this.region48 = new AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 32, 32);
        // this.img = new Texture(ModHelper.makeImagePath("powers/RuneIndexPower.png"));

        // 首次添加能力更新描述
        this.updateDescription();
    }

    @Override
    public void atStartOfTurnPostDraw() {
        for (int i = 0; i < this.amount; i++) {
            ForwardPropagation forward_card = new ForwardPropagation();
            forward_card.exhaust = true;
            forward_card.purgeOnUse = true;
            forward_card.energyOnUse = forward_card.costForTurn;
            forward_card.freeToPlayOnce = false;

            AbstractDungeon.player.limbo.addToBottom(forward_card);
            // 这里方向要随机加个偏置，防止牌叠一块了
            forward_card.current_x = forward_card.current_x;
            forward_card.current_y = forward_card.current_y;
            forward_card.target_x = (Settings.WIDTH / 2.0F * Settings.scale) * MathUtils.random(0.8F, 1.2F);
            forward_card.target_y = Settings.HEIGHT / 2.0F * MathUtils.random(0.8F, 1.2F);

            this.addToBot(new NewQueueCardAction(forward_card, true, false, true));

            BackPropagation back_card = new BackPropagation();
            back_card.exhaust = true;
            back_card.purgeOnUse = true;
            back_card.energyOnUse = back_card.costForTurn;
            back_card.freeToPlayOnce = false;

            AbstractDungeon.player.limbo.addToBottom(back_card);
            // 这里方向要随机加个偏置，防止牌叠一块了
            back_card.current_x = back_card.current_x;
            back_card.current_y = back_card.current_y;
            back_card.target_x = (Settings.WIDTH / 2.0F * Settings.scale) * MathUtils.random(0.8F, 1.2F);
            back_card.target_y = Settings.HEIGHT / 2.0F * MathUtils.random(0.8F, 1.2F);

            this.addToBot(new NewQueueCardAction(back_card, true, false, true));

        }
    }

    // 能力在更新时如何修改描述
    public void updateDescription() {
        // this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
        this.description = String.format(DESCRIPTIONS[0], this.amount); // 这样，%d就被替换成能力的层数
    }
}