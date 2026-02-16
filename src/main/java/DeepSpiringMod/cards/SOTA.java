package DeepSpiringMod.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;

import basemod.abstracts.CustomCard;

import DeepSpiringMod.patches.PlayerColorEnum;
import DeepSpiringMod.actions.SOTAAction;
import DeepSpiringMod.helpers.ModHelper;

import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SOTA extends CustomCard {
    public static final String ID = ModHelper.makePath("SOTA");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源
    // private static final String NAME = "打击";
    private static final String NAME = CARD_STRINGS.NAME; // 读取本地化的名字
    private static final String IMG_PATH = "DeepSpiringModResources/img/cards/SOTA.png";
    private static final int COST = 1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION; // 读取本地化的描述
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = PlayerColorEnum.DEEP_BLUE;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    public static final Logger logger = LogManager.getLogger(SOTA.class);

    public SOTA() {
        // 为了命名规范修改了变量名。这些参数具体的作用见下方
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        // logger.info("Start to init SOTA.\n");
        this.magicNumber = 20;
        this.baseMagicNumber = this.magicNumber;
        this.misc = 0;
        
        if (AbstractDungeon.player != null) {
            Iterator var1 = AbstractDungeon.player.masterDeck.group.iterator();
            AbstractCard c;
            while(var1.hasNext()) {
                c = (AbstractCard)var1.next();
                if (c instanceof SOTA) {
                    logger.info("Found SOTA in master deck during initialization. Setting misc to " + c.misc + ".\n");
                    this.misc = c.misc;
                    break;
                }
            }
        } else {
            logger.warn("Player is null during SOTA initialization. This might happen during game initialization. Setting misc to 0.\n");
        }

        this.initializeDescription();
        // logger.info("SOTA initialization completed.\n");
    }

    @Override
    public void upgrade() {
        logger.info("Upgrading SOTA. Current misc: " + this.misc + ".\n");
        if (!this.upgraded) {
            this.upgradeName(); // 卡牌名字变为绿色并添加“+”，且标为升级过的卡牌，之后不能再升级。
            this.upgradeMagicNumber(20);

            // 加上以下两行就能使用UPGRADE_DESCRIPTION了（如果你写了的话）
            // this.rawDescription = String.format(CARD_STRINGS.UPGRADE_DESCRIPTION, this.misc);
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // if (upgraded) {
        //     this.addToBot(new ApplyPowerAction(p, p, new SOTAPower(p)));
        // }

        // this.addToBot(new ApplyPowerAction(p, p, new APPower(p, 2)));
        // this.addToBot(new ApplyPowerAction(p, p, new OverfittingPower(p, 1)));
        
        int now_AP = ModHelper.get_AP();
        logger.info("Current AP: " + now_AP + ", Previous SOTA_AP: " + this.misc);
        if (now_AP > this.misc) {
            this.misc = now_AP;
            this.addToBot(new SOTAAction(this.magicNumber * this.misc, this, this.misc));
        }
        this.addToBot(new DrawCardAction(p, 1));
        // if (!upgraded) {
        //     this.rawDescription = String.format(CARD_STRINGS.DESCRIPTION, this.misc);
        // } else {
        //     this.rawDescription = String.format(CARD_STRINGS.UPGRADE_DESCRIPTION, this.misc);
        // }
        // logger.info("use: " + this.rawDescription + ", misc = " + this.misc);
        this.initializeDescription();
    }

    // @Override
    // public void applyPowers() {
    //     super.applyPowers();
    //     this.initializeDescription();
    // }
    
    @Override
    public void initializeDescription() {
        if (!upgraded) {
            this.rawDescription = String.format(CARD_STRINGS.DESCRIPTION, this.misc);
        } else {
            this.rawDescription = String.format(CARD_STRINGS.UPGRADE_DESCRIPTION, this.misc);
        }
        logger.info("initializeDescription: " + this.rawDescription + ", misc = " + this.misc);
        super.initializeDescription();
    }

    @Override
    public AbstractCard makeCopy() {
        logger.info("Making a copy of SOTA. Current misc: " + this.misc + ".\n");
        SOTA c =  new SOTA();
        c.misc = this.misc;
        if (AbstractDungeon.player != null) {
            Iterator var1 = AbstractDungeon.player.masterDeck.group.iterator();
            AbstractCard tem;
            while(var1.hasNext()) {
                tem = (AbstractCard)var1.next();
                if (tem instanceof SOTA) {
                    logger.info("Found SOTA in master deck during initialization. Setting misc to " + c.misc + ".\n");
                    c.misc = tem.misc;
                    break;
                }
            }
        } else {
            logger.warn("Player is null during SOTA initialization. This might happen during game initialization. Setting misc to 0.\n");
        }
        c.initializeDescription();
        return c;
    }
}
