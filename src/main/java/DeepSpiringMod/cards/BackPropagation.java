package DeepSpiringMod.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;

import DeepSpiringMod.patches.PlayerColorEnum;
import DeepSpiringMod.powers.APPower;
import basemod.abstracts.CustomCard;
import DeepSpiringMod.helpers.ModHelper;

import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BackPropagation extends CustomCard {
    public static final String ID = ModHelper.makePath("BackPropagation");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源
    // private static final String NAME = "打击";
    private static final String NAME = CARD_STRINGS.NAME; // 读取本地化的名字
    // private static final String IMG_PATH = "DeepSpiringModResources/img/cards/Strike_DeepBlue.png";
    private static final String IMG_PATH = "DeepSpiringModResources/img/cards/BackPropagation.png";
    private static final int COST = 1;
    // private static final String DESCRIPTION = "造成 !D! 点伤害。";
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION; // 读取本地化的描述
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = PlayerColorEnum.DEEP_BLUE;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    // public static final int damge_without_AP = 16;

    public static final Logger logger = LogManager.getLogger(BackPropagation.class);

    public BackPropagation() {
        // 为了命名规范修改了变量名。这些参数具体的作用见下方
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        // logger.info("Start to init BackPropagation.\n");
        this.baseMagicNumber = 5;
        this.magicNumber = this.baseMagicNumber;
        // logger.info("BackPropagation initialization completed.\n");
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName(); // 卡牌名字变为绿色并添加“+”，且标为升级过的卡牌，之后不能再升级。
            this.upgradeMagicNumber(-1);

            // 加上以下两行就能使用UPGRADE_DESCRIPTION了（如果你写了的话）
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int power_amount = 0;
        Iterator var3 = p.powers.iterator();
        while(var3.hasNext()) {
            com.megacrit.cardcrawl.powers.AbstractPower pow = (com.megacrit.cardcrawl.powers.AbstractPower)var3.next();
            if (pow.amount == -1 && !pow.canGoNegative) {
                power_amount += 1;
            } else {
                power_amount += Math.abs(pow.amount);
            }
        }
        int increase = (int) Math.floor(power_amount / this.magicNumber);
        if (increase > 0) {
            this.addToBot(new ApplyPowerAction(p, p, new APPower(p, increase), increase));
        }
    }
}
