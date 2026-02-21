package DeepSpiringMod.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;

import basemod.abstracts.CustomCard;

import DeepSpiringMod.patches.PlayerColorEnum;
import DeepSpiringMod.powers.APPower;
import DeepSpiringMod.powers.OverfittingPower;
import DeepSpiringMod.actions.SetPretrainWeightAction;
import DeepSpiringMod.helpers.ModHelper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PreTrainedModel extends CustomCard {
    public static final String ID = ModHelper.makePath("PreTrainedModel");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源
    // private static final String NAME = "打击";
    private static final String NAME = CARD_STRINGS.NAME; // 读取本地化的名字
    private static final String IMG_PATH = "DeepSpiringModResources/img/cards/PreTrainedModel.png";
    // private static final String IMG_PATH = null;
    private static final int COST = 1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION; // 读取本地化的描述
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = PlayerColorEnum.DEEP_BLUE;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    public int storage_AP;
    public int storage_Overfitting;

    public static final Logger logger = LogManager.getLogger(PreTrainedModel.class);

    public PreTrainedModel() {
        // 为了命名规范修改了变量名。这些参数具体的作用见下方
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        logger.info("Start to init PreTrainedModel.\n");
        // this.storage_AP = 0;
        // this.storage_Overfitting = 0;
        // this.misc = 0;
        if (upgraded) {
            WeightSaving weightSaving_card = new WeightSaving();
            weightSaving_card.upgrade();
            this.cardsToPreview = weightSaving_card;
        } else {
             this.cardsToPreview = new WeightSaving();
        }
        this.exhaust = true;
        this.initializeDescription();
        // logger.info("PreTrainedModel initialization completed. uuid: " + this.uuid + ", storage_AP: " + this.storage_AP + ", storage_Overfitting: " + this.storage_Overfitting + ".\n");
    }

    @Override
    public void upgrade() {
        logger.info("Upgrading PreTrainedModel. Current storage_AP: " + this.storage_AP + ", storage_Overfitting: " + this.storage_Overfitting + ", uuid: " + this.uuid + ".");
        if (!this.upgraded) {
            this.upgradeName(); // 卡牌名字变为绿色并添加“+”，且标为升级过的卡牌，之后不能再升级。
                if (this.cardsToPreview != null && this.cardsToPreview instanceof WeightSaving) {
                    ((WeightSaving)this.cardsToPreview).upgrade();
                }

            // 加上以下两行就能使用UPGRADE_DESCRIPTION了（如果你写了的话）
            // this.rawDescription = String.format(CARD_STRINGS.UPGRADE_DESCRIPTION, this.misc);
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // logger.info("Using PreTrainedModel. uuid: " + this.uuid);
        if (this.storage_AP > 0) {
            this.addToBot(new ApplyPowerAction(p, p, new APPower(p, this.storage_AP)));
        }
        if (this.storage_Overfitting > 0) {
            this.addToBot(new ApplyPowerAction(p, p, new OverfittingPower(p, this.storage_Overfitting)));
        }
        WeightSaving weightSaving_card = new WeightSaving();
        if (this.upgraded) {
            weightSaving_card.upgrade();
        }
        AbstractDungeon.player.hand.addToTop(weightSaving_card);
        this.addToBot(new SetPretrainWeightAction(0, 0));
        this.initializeDescription();
    }

    // @Override
    // public void applyPowers() {
    //     super.applyPowers();
    //     this.initializeDescription();
    // }
    
    @Override
    public void initializeDescription() {
        get_AP_Overfitting_from_misc();
        if (!upgraded) {
            this.rawDescription = String.format(CARD_STRINGS.DESCRIPTION, this.storage_AP, this.storage_Overfitting);
        } else {
            this.rawDescription = String.format(CARD_STRINGS.UPGRADE_DESCRIPTION, this.storage_AP, this.storage_Overfitting);
        }
        logger.info("initializeDescription: " + this.rawDescription + ", storage_AP = " + this.storage_AP + ", storage_Overfitting = " + this.storage_Overfitting + ", uuid: " + this.uuid + ".");
        super.initializeDescription();
    }

    public void get_AP_Overfitting_from_misc() {
        if (this.misc > 0) {
            this.storage_AP = this.misc % 100;
            this.storage_Overfitting = (int) Math.floor(this.misc / 100.0);
        } else {
            this.storage_AP = 0;
            this.storage_Overfitting = 0;
        }
    }

    @Override
    public AbstractCard makeCopy() {
        logger.info("Making a copy of PreTrainedModel. Current storage_AP: " + this.storage_AP + ", storage_Overfitting: " + this.storage_Overfitting + ".");
        PreTrainedModel c =  new PreTrainedModel();
        c.storage_AP = this.storage_AP;
        c.storage_Overfitting = this.storage_Overfitting;
        // c.uuid = this.uuid; // 复制uuid以保持与原卡牌的关联
        c.misc = this.misc;
        c.initializeDescription();
        return c;
    }
}
