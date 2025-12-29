package DeepSpiringMod.cards;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import DeepSpiringMod.helpers.ModHelper;
import DeepSpiringMod.patches.PlayerColorEnum;
import basemod.abstracts.CustomCard;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChargeDataset extends CustomCard {
    public static final String ID = ModHelper.makePath("ChargeDataset");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源
    // private static final String NAME = "打击";
    private static final String NAME = CARD_STRINGS.NAME; // 读取本地化的名字
    // private static final String IMG_PATH = "DeepSpiringModResources/img/cards/Defend_DeepBlue.png";
    private static final String IMG_PATH = "DeepSpiringModResources/img/cards/ChargeDataset.png";
    // private static final String IMG_PATH = "blue/attack/strike";
    private static final int COST = 5;
    // private static final String DESCRIPTION = "造成 !D! 点伤害。";
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION; // 读取本地化的描述
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = PlayerColorEnum.DEEP_BLUE;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public static final Logger logger = LogManager.getLogger(ChargeDataset.class);

    public ChargeDataset() {
        // 为了命名规范修改了变量名。这些参数具体的作用见下方
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        logger.debug("Start to init ChargeDataset.\n");
        this.baseMagicNumber = 4;
        this.magicNumber = this.baseMagicNumber;
        logger.debug("ChargeDataset initialization completed.\n");
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
            new GainEnergyAction(this.magicNumber)
        );
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName(); // 卡牌名字变为绿色并添加“+”，且标为升级过的卡牌，之后不能再升级。
            this.upgradeBaseCost(6);
            this.upgradeMagicNumber(2);

            // // 加上以下两行就能使用UPGRADE_DESCRIPTION了（如果你写了的话）
            // this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            // this.initializeDescription();
        }
    }
}
