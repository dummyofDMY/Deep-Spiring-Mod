package DeepSpiringMod.cards;

import basemod.abstracts.CustomCard;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;

import DeepSpiringMod.patches.PlayerColorEnum;
import DeepSpiringMod.helpers.ModHelper;
import DeepSpiringMod.actions.ForwardPropagationAction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Overflow extends CustomCard {
    public static final String ID = ModHelper.makePath("Overflow");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源
    // private static final String NAME = "打击";
    private static final String NAME = CARD_STRINGS.NAME; // 读取本地化的名字
    // private static final String IMG_PATH = "DeepSpiringModResources/img/cards/Strike_DeepBlue.png";
    private static final String IMG_PATH = null;
    private static final int COST = -2;
    // private static final String DESCRIPTION = "造成 !D! 点伤害。";
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION; // 读取本地化的描述
    private static final String[] EXTENDED_DESCRIPTION = CARD_STRINGS.EXTENDED_DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = PlayerColorEnum.DEEP_BLUE;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;

    public static final Logger logger = LogManager.getLogger(Overflow.class);

    public Overflow() {
        // 为了命名规范修改了变量名。这些参数具体的作用见下方
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        logger.debug("Start to init Overflow.\n");
        this.exhaust = true;
        this.isEthereal = true;
        logger.debug("Overflow initialization completed.\n");
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName(); // 卡牌名字变为绿色并添加“+”，且标为升级过的卡牌，之后不能再升级。
            this.upgradeBaseCost(0);

            // 加上以下两行就能使用UPGRADE_DESCRIPTION了（如果你写了的话）
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // this.addToBot(new DrawCardAction(p, this.magicNumber));
        // this.addToBot(new GainEnergyAction(this.magicNumber));

        if (this.upgraded) {
            // System.out.print("use overflow\n");
            // this.addToBot(new ClearRecursionDepthAction());
            this.addToBot(new RemoveSpecificPowerAction(p, p, ModHelper.makePath("RecursionDepth")));
            this.addToBot(new ForwardPropagationAction(1));
        }
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (this.upgraded) {
            return true;
        }
        this.cantUseMessage = EXTENDED_DESCRIPTION[0];
        return false;
   }
}
