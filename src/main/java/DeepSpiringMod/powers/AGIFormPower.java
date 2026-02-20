package DeepSpiringMod.powers;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

import DeepSpiringMod.actions.PlayCardsInDiscardPileAction;
import DeepSpiringMod.helpers.ModHelper;

public class AGIFormPower extends AbstractPower {
    // 能力的ID
    public static final String POWER_ID = ModHelper.makePath("AGIForm");
    // 能力的本地化字段
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    // 能力的名称
    private static final String NAME = powerStrings.NAME;
    // 能力的描述
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    int original_hand_size;

    public AGIFormPower(AbstractCreature owner, int Amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;

        // 如果需要不能叠加的能力，只需将上面的Amount参数删掉，并把下面的Amount改成-1就行
        this.amount = Amount;

        // // 添加一大一小两张能力图
        String path128 = ModHelper.makeImagePath("powers/AGIForm84.png");
        String path48 = ModHelper.makeImagePath("powers/AGIForm32.png");
        this.region128 = new AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 84, 84);
        this.region48 = new AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 32, 32);
        // this.img = new Texture(ModHelper.makeImagePath("powers/RuneIndexPower.png"));

        // 首次添加能力更新描述
        this.updateDescription();
        // this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DrawPower(AbstractDungeon.player, -5), -5));
        AbstractDungeon.player.gameHandSize = AbstractDungeon.player.gameHandSize = 0;
    }

    public void onInitialApplication() {
        original_hand_size = AbstractDungeon.player.gameHandSize;
        AbstractDungeon.player.gameHandSize = 0;
    }

    public void onRemove() {
        AbstractDungeon.player.gameHandSize = original_hand_size;
    }

    @Override
    public void atStartOfTurn() {
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            this.flash();
            for (int i = 0; i < this.amount; i++) {
                AbstractCard attact_card = AbstractDungeon.returnTrulyRandomCardInCombat(CardType.ATTACK).makeCopy();
                AbstractCard skill_card = AbstractDungeon.returnTrulyRandomCardInCombat(CardType.SKILL).makeCopy();
                AbstractCard power_card = AbstractDungeon.returnTrulyRandomCardInCombat(CardType.POWER).makeCopy();
                if (AbstractDungeon.player.hasPower(ModHelper.makePath("Hallucination"))) {
                    attact_card.freeToPlayOnce = true;
                    skill_card.freeToPlayOnce = true;
                    power_card.freeToPlayOnce = true;
                }
                this.addToBot(new MakeTempCardInHandAction(attact_card, false));
                this.addToBot(new MakeTempCardInHandAction(skill_card, false));
                this.addToBot(new MakeTempCardInHandAction(power_card, false));
            }
            this.addToBot(new PlayCardsInDiscardPileAction(2 * this.amount, true));
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
