package DeepSpiringMod.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.watcher.EnergyDownPower;

import DeepSpiringMod.helpers.ModHelper;
import DeepSpiringMod.powers.EmbeddingPower;
import DeepSpiringMod.powers.HallucinationPower;

// 继承CustomRelic
public class GPU extends CustomRelic {
    // 遗物ID（此处的ModHelper在“04 - 本地化”中提到）
    public static final String ID = ModHelper.makePath("GPU");
    // 图片路径（大小128x128，可参考同目录的图片）
    private static final String IMG_PATH = ModHelper.makeImagePath("relics/GPU.png");
    // 遗物未解锁时的轮廓。可以不使用。如果要使用，取消注释
    // private static final String OUTLINE_PATH = "ExampleModResources/img/relics/MyRelic_Outline.png";
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.BOSS;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;
    // private int start_energy = 0;
    private int energy_decrease;

    public GPU() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
        // 如果你需要轮廓图，取消注释下面一行并注释上面一行，不需要就删除
        // super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(OUTLINE_PATH), RELIC_TIER, LANDING_SOUND);
    }

    // 获取遗物描述，但原版游戏只在初始化和获取遗物时调用，故该方法等于初始描述
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new GPU();
    }

    @Override
    public void atBattleStart() {
        // this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new EnergyDownPower(AbstractDungeon.player, 3, true), 3));
        this.flash();
        // this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new APPower(AbstractDungeon.player, 1), 1));
    }

    // @Override
    // public void onVictory() {

    // }

    @Override
    public void atTurnStartPostDraw() {
        // this.start_energy = AbstractDungeon.player.energy.energy;
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new EmbeddingPower(AbstractDungeon.player, 3), 3));
    }

    @Override
    public void onEquip() {
        int original_energy = AbstractDungeon.player.energy.energyMaster;
        energy_decrease = Math.min(3, original_energy);
        AbstractDungeon.player.energy.energyMaster -= energy_decrease;
    }

    @Override
    public void onUnequip() {
        AbstractDungeon.player.energy.energyMaster += energy_decrease;
    }

    // @Override
    // public void onUseCard(AbstractCard card, UseCardAction action) {
    //     if (!card.purgeOnUse && AbstractDungeon.actionManager.cardsPlayedThisTurn.size() <= 1) {
    //         if (card.cost == -2) {
    //             return;
    //         } else if (card.cost == -1) {
    //             this.addToBot(new GainEnergyAction(this.start_energy));
    //             this.flash();
    //         } else {
    //             this.addToBot(new GainEnergyAction(card.cost));
    //             this.flash();
    //         }
            
    //     }
    // }
}
