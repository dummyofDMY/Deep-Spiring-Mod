package DeepSpiringMod.cards;

import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;

import DeepSpiringMod.patches.PlayerColorEnum;
import DeepSpiringMod.actions.DeepfectStrikeAction;
import DeepSpiringMod.helpers.ModHelper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DeepfectStrike extends AbstractAPCard {
    public static final String ID = ModHelper.makePath("DeepfectStrike");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源
    // private static final String NAME = "打击";
    private static final String NAME = CARD_STRINGS.NAME; // 读取本地化的名字
    // private static final String IMG_PATH = "DeepSpiringModResources/img/cards/Strike_DeepBlue.png";
    private static final String IMG_PATH = "DeepSpiringModResources/img/cards/DeepfectStrike.png";
    private static final int COST = -1;
    // private static final String DESCRIPTION = "造成 !D! 点伤害。";
    private static final String DESCRIPTION = String.format(CARD_STRINGS.DESCRIPTION, 4, 2); // 读取本地化的描述
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = PlayerColorEnum.DEEP_BLUE;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ALL;
    // public static final int damge_without_AP = 16;
    private int selfDamage;
    private int AP_factor = 4;
    private int Overfitting_factor = 2;

    public static final Logger logger = LogManager.getLogger(DeepfectStrike.class);

    public DeepfectStrike() {
        // 为了命名规范修改了变量名。这些参数具体的作用见下方
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        // logger.info("Start to init DeepfectStrike.\n");
        this.baseDamage = this.damage = 3;
        this.selfDamage = 0;
        this.isMultiDamage = true;
        int[] AP_list = get_AP();
        update_with_AP(AP_list[0], AP_list[1]);
        rawDescription = String.format(CARD_STRINGS.DESCRIPTION, AP_factor, Overfitting_factor);
        // logger.info("DeepfectStrike initialization completed.\n");
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName(); // 卡牌名字变为绿色并添加“+”，且标为升级过的卡牌，之后不能再升级。
            this.AP_factor += 2;
            this.Overfitting_factor += 1;

            // 加上以下两行就能使用UPGRADE_DESCRIPTION了（如果你写了的话）
            this.rawDescription = String.format(CARD_STRINGS.UPGRADE_DESCRIPTION, this.AP_factor, this.Overfitting_factor);
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new TalkAction(true, CARD_STRINGS.EXTENDED_DESCRIPTION[0], 1.2F, 1.2F));
        this.addToBot(new DeepfectStrikeAction(p, this.multiDamage, this.selfDamage, this.damageTypeForTurn, this.freeToPlayOnce, this.energyOnUse));
    }

    @Override
    public void update_with_AP(int AP, int Overfitting) {

        this.selfDamage = this.Overfitting_factor * Overfitting;
        int new_damage = this.AP_factor * AP + this.Overfitting_factor * Overfitting;
        this.baseDamage = new_damage;
        this.damage = new_damage;
        this.upgradedDamage = true;
        this.initializeDescription();
    }
}

