package DeepSpiringMod.cards;

import basemod.abstracts.CustomCard;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.powers.EquilibriumPower;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;

import DeepSpiringMod.patches.PlayerColorEnum;
import DeepSpiringMod.helpers.ModHelper;

public class BlockStrike extends CustomCard {
    public static final String ID = ModHelper.makePath("BlockStrike");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源
    // private static final String NAME = "打击";
    private static final String NAME = CARD_STRINGS.NAME; // 读取本地化的名字
    private static final String IMG_PATH = "DeepSpiringModResources/img/cards/BlockStrike.png";
    private static final int COST = 3;
    // private static final String DESCRIPTION = "造成 !D! 点伤害。";
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION; // 读取本地化的描述
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = PlayerColorEnum.DEEP_BLUE;
    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public BlockStrike() {
        // 为了命名规范修改了变量名。这些参数具体的作用见下方
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseDamage = this.damage = 15;
        this.baseBlock = 15;
        this.baseMagicNumber = 1;
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName(); // 卡牌名字变为绿色并添加“+”，且标为升级过的卡牌，之后不能再升级。
            this.upgradeDamage(6); // 将该卡牌的伤害提高3点。
            this.upgradeBlock(6);

            // // 加上以下两行就能使用UPGRADE_DESCRIPTION了（如果你写了的话）
            // this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            // this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new GainBlockAction(p, p, this.block));
        this.addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageType.NORMAL), AttackEffect.BLUNT_LIGHT));
        this.addToBot(new ApplyPowerAction(p, p, new EquilibriumPower(p, this.magicNumber), this.magicNumber));
    }
}
