package DeepSpiringMod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import DeepSpiringMod.helpers.ModHelper;

public class SOTAPower extends AbstractPower {
    // 能力的ID
    public static final String POWER_ID = ModHelper.makePath("SOTA");
    // 能力的本地化字段
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    // 能力的名称
    private static final String NAME = powerStrings.NAME;
    // 能力的描述
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public SOTAPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;

        // 如果需要不能叠加的能力，只需将上面的Amount参数删掉，并把下面的Amount改成-1就行
        this.amount = -1;

        // // 添加一大一小两张能力图
        String path128 = ModHelper.makeImagePath("powers/SOTA84.png");
        String path48 = ModHelper.makeImagePath("powers/SOTAh32.png");
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
}