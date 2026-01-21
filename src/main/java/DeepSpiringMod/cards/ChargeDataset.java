package DeepSpiringMod.cards;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import DeepSpiringMod.helpers.ModHelper;
import DeepSpiringMod.patches.PlayerColorEnum;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChargeDataset extends AbstractAPCard {
    public static final String ID = ModHelper.makePath("ChargeDataset");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源
    // private static final String NAME = "打击";
    private static final String NAME = CARD_STRINGS.NAME; // 读取本地化的名字
    // private static final String IMG_PATH = "DeepSpiringModResources/img/cards/Defend_DeepBlue.png";
    private static final String IMG_PATH = "DeepSpiringModResources/img/cards/ChargeDataset.png";
    // private static final String IMG_PATH = "blue/attack/strike";
    private static final int COST = 3;
    // private static final String DESCRIPTION = "造成 !D! 点伤害。";
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION; // 读取本地化的描述
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = PlayerColorEnum.DEEP_BLUE;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public static final Logger logger = LogManager.getLogger(ChargeDataset.class);
    public int now_AP = 0;
    public int now_Overfitting = 0;
    private int initial_cost = COST;

    public ChargeDataset() {
        // 为了命名规范修改了变量名。这些参数具体的作用见下方
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        // logger.info("Start to init ChargeDataset.\n");
        int[] AP_Overfitting = this.get_AP();
        now_AP = AP_Overfitting[0];
        now_Overfitting = AP_Overfitting[1];
        this.baseMagicNumber = 3;
        this.magicNumber = this.baseMagicNumber;
        update_cost_with_AP();
        // logger.info("ChargeDataset initialization completed.\n");
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
            this.initial_cost = 4;
            this.upgradeMagicNumber(1);
            update_cost_with_AP();

            // 加上以下两行就能使用UPGRADE_DESCRIPTION了（如果你写了的话）
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    public void update_cost_with_AP() {
        int cost_decrease = Math.max(0, (this.now_AP - this.now_Overfitting));
        int new_cost = Math.max(0, this.initial_cost - cost_decrease);
        logger.info("New cost: " + new_cost);
        this.upgradeBaseCost(new_cost);
    }

    public void update_with_AP(int AP, int Overfitting) {
        this.now_AP = AP;
        this.now_Overfitting = Overfitting;
        this.update_cost_with_AP();
    }
}
