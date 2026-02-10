package DeepSpiringMod.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;

import DeepSpiringMod.patches.PlayerColorEnum;
import DeepSpiringMod.helpers.ModHelper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StrikeDataset extends AbstractAPCard {
    public static final String ID = ModHelper.makePath("StrikeDataset");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源
    // private static final String NAME = "打击";
    private static final String NAME = CARD_STRINGS.NAME; // 读取本地化的名字
    // private static final String IMG_PATH = "DeepSpiringModResources/img/cards/Strike_DeepBlue.png";
    private static final String IMG_PATH = "DeepSpiringModResources/img/cards/StrikeDataset.png";
    private static final int COST = 1;
    // private static final String DESCRIPTION = "造成 !D! 点伤害。";
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION; // 读取本地化的描述
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = PlayerColorEnum.DEEP_BLUE;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    // public static final int damge_without_AP = 16;

    public static final Logger logger = LogManager.getLogger(StrikeDataset.class);

    public StrikeDataset() {
        // 为了命名规范修改了变量名。这些参数具体的作用见下方
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        // logger.info("Start to init StrikeDataset.\n");
        this.baseDamage = this.damage = 3;
        this.baseMagicNumber = 5;
        this.magicNumber = this.baseMagicNumber;
        int[] AP_list = get_AP();
        update_with_AP(AP_list[0], AP_list[1]);
        // logger.info("StrikeDataset initialization completed.\n");
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName(); // 卡牌名字变为绿色并添加“+”，且标为升级过的卡牌，之后不能再升级。
            // this.upgradeDamage(3);

            // 加上以下两行就能使用UPGRADE_DESCRIPTION了（如果你写了的话）
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < this.magicNumber; ++i) {
            this.addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageType.NORMAL), AttackEffect.BLUNT_HEAVY));
        }
    }

    @Override
    public void update_with_AP(int AP, int Overfitting) {
        int magic_floor = this.upgraded ? 2 : 0;
        int new_magic = Math.max(5 - Overfitting, magic_floor);
        this.baseMagicNumber = new_magic;
        this.magicNumber = new_magic;
        this.upgradedMagicNumber = true;
        int new_damage = 3 + AP * 3;
        this.baseDamage = new_damage;
        this.damage = new_damage;
        this.upgradedDamage = true;
        this.initializeDescription();
    }
}
