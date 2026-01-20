package DeepSpiringMod.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;

import basemod.abstracts.CustomCard;

import DeepSpiringMod.patches.PlayerColorEnum;
import DeepSpiringMod.helpers.ModHelper;
import DeepSpiringMod.powers.APPower;
import DeepSpiringMod.powers.OverfittingPower;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ScalingLaw extends CustomCard {
    public static final String ID = ModHelper.makePath("ScalingLaw");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源
    // private static final String NAME = "打击";
    private static final String NAME = CARD_STRINGS.NAME; // 读取本地化的名字
    private static final String IMG_PATH = "DeepSpiringModResources/img/cards/scaling_law.png";
    private static final int COST = 1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION; // 读取本地化的描述
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = PlayerColorEnum.DEEP_BLUE;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    public static final Logger logger = LogManager.getLogger(ScalingLaw.class);

    public ScalingLaw() {
        // 为了命名规范修改了变量名。这些参数具体的作用见下方
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        // logger.info("Start to init ScalingLaw.\n");
        this.exhaust = true;
        // logger.info("ScalingLaw initialization completed.\n");
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName(); // 卡牌名字变为绿色并添加“+”，且标为升级过的卡牌，之后不能再升级。
            this.exhaust = false;
            // 加上以下两行就能使用UPGRADE_DESCRIPTION了（如果你写了的话）
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // if (AbstractDungeon.player.hasPower(ModHelper.makePath("AP"))) {
        //     int AP_stacks = AbstractDungeon.player.getPower(ModHelper.makePath("AP")).amount;
        //     this.addToBot(new ApplyPowerAction(p, p, new APPower(p, AP_stacks), AP_stacks));
        // }
        // if (AbstractDungeon.player.hasPower(ModHelper.makePath("Overfitting"))) {
        //     int Overfitting_stacks = AbstractDungeon.player.getPower(ModHelper.makePath("Overfitting")).amount;
        //     Overfitting_stacks = (int)Math.ceil(Overfitting_stacks / 2);
        //     this.addToBot(new ApplyPowerAction(p, p, new OverfittingPower(p, -Overfitting_stacks), -Overfitting_stacks));
        // }
        this.addToBot(new ApplyPowerAction(p, p, new APPower(p, 1), 1));
        this.addToBot(new ApplyPowerAction(p, p, new OverfittingPower(p, -1), -1));
    }
    
}
