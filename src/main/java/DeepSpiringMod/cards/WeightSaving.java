package DeepSpiringMod.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;

import basemod.abstracts.CustomCard;

import DeepSpiringMod.patches.PlayerColorEnum;
import DeepSpiringMod.helpers.ModHelper;

import java.util.Iterator;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WeightSaving extends CustomCard {
    public static final String ID = ModHelper.makePath("WeightSaving");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源
    // private static final String NAME = "打击";
    private static final String NAME = CARD_STRINGS.NAME; // 读取本地化的名字
    private static final String IMG_PATH = "DeepSpiringModResources/img/cards/WeightSaving.png";
    // private static final String IMG_PATH = null;
    private static final int COST = 1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION; // 读取本地化的描述
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = PlayerColorEnum.DEEP_BLUE;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private UUID pre_train_card_uuid;

    public static final Logger logger = LogManager.getLogger(WeightSaving.class);

    public WeightSaving(UUID pre_train_card_uuid) {
        // 为了命名规范修改了变量名。这些参数具体的作用见下方
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        logger.info("Start to init WeightSaving. uuid: " + pre_train_card_uuid);
        this.pre_train_card_uuid = pre_train_card_uuid;
        this.baseMagicNumber = this.magicNumber = 4;
        this.exhaust = true;
        this.initializeDescription();
        logger.info("WeightSaving initialization completed.\n");
    }

    public WeightSaving() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        logger.info("Start to init WeightSaving.\n");
        this.pre_train_card_uuid = UUID.randomUUID();
        this.baseMagicNumber = this.magicNumber = 4;
        this.exhaust = true;
        this.initializeDescription();
        logger.info("WeightSaving initialization completed.\n");
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName(); // 卡牌名字变为绿色并添加“+”，且标为升级过的卡牌，之后不能再升级。

            // 加上以下两行就能使用UPGRADE_DESCRIPTION了（如果你写了的话）
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int AP_amount = ModHelper.get_AP();
        int Overfitting_amount = ModHelper.get_Overfitting();
        logger.info("Using WeightSaving. Current AP: " + AP_amount + ", Current Overfitting: " + Overfitting_amount + ".\n");

        if (!this.upgraded) {
            AP_amount = (int)Math.floor(AP_amount * 0.5);
            Overfitting_amount = (int)Math.floor(Overfitting_amount * 0.5);
        } else {
            AP_amount = (int)Math.floor(AP_amount * 0.75);
            Overfitting_amount = (int)Math.floor(Overfitting_amount * 0.75);
        }
        logger.info("After applying WeightSaving reduction. AP: " + AP_amount + ", Overfitting: " + Overfitting_amount + ".\n");

        AP_amount = Math.max(0, AP_amount);
        AP_amount = Math.min(AP_amount, 5);
        Overfitting_amount = Math.max(0, Overfitting_amount);
        Overfitting_amount = Math.min(Overfitting_amount, 5);
        
        AbstractCard c;
        Iterator card_it = AbstractDungeon.player.masterDeck.group.iterator();
        while(card_it.hasNext()) {
            c = (AbstractCard)card_it.next();
            if (c.uuid.equals(this.pre_train_card_uuid)) {
                ((PreTrainedModel)c).storage_AP = AP_amount;
                ((PreTrainedModel)c).storage_Overfitting = Overfitting_amount;
                ((PreTrainedModel)c).misc = AP_amount + Overfitting_amount * 100; // 将AP和Overfitting的值编码到misc中，以便在卡牌初始化时恢复
                c.initializeDescription();
                logger.info("Updated the pre-train card in master deck during WeightSaving use. AP_amount: " + AP_amount + ", Overfitting_amount: " + Overfitting_amount + ".");
                break;
            } else {
                logger.info("uuid mismatch: expected " + this.pre_train_card_uuid + ", got " + c.uuid);
            }
        }

        card_it = AbstractDungeon.player.exhaustPile.group.iterator();
        while(card_it.hasNext()) {
            c = (AbstractCard)card_it.next();
            if (c.uuid.equals(this.pre_train_card_uuid)) {
                ((PreTrainedModel)c).storage_AP = AP_amount;
                ((PreTrainedModel)c).storage_Overfitting = Overfitting_amount;
                ((PreTrainedModel)c).misc = AP_amount + Overfitting_amount * 100; // 将AP和Overfitting的值编码到misc中，以便在卡牌初始化时恢复
                c.initializeDescription();
                logger.info("Updated the pre-train card in exhaust pile during WeightSaving use. AP_amount: " + AP_amount + ", Overfitting_amount: " + Overfitting_amount + ", misc: " + ((PreTrainedModel)c).misc + ".");
                break;
            }
        }

        card_it = AbstractDungeon.player.discardPile.group.iterator();
        while(card_it.hasNext()) {
            c = (AbstractCard)card_it.next();
            if (c.uuid.equals(this.pre_train_card_uuid)) {
                ((PreTrainedModel)c).storage_AP = AP_amount;
                ((PreTrainedModel)c).storage_Overfitting = Overfitting_amount;
                ((PreTrainedModel)c).misc = AP_amount + Overfitting_amount * 100;
                c.initializeDescription();
                logger.info("Updated the pre-train card in discard pile during WeightSaving use. AP_amount: " + AP_amount + ", Overfitting_amount: " + Overfitting_amount + ".");
                break;
            }
        }

        card_it = AbstractDungeon.player.drawPile.group.iterator();
        while(card_it.hasNext()) {
            c = (AbstractCard)card_it.next();
            if (c.uuid.equals(this.pre_train_card_uuid)) {
                ((PreTrainedModel)c).storage_AP = AP_amount;
                ((PreTrainedModel)c).storage_Overfitting = Overfitting_amount;
                ((PreTrainedModel)c).misc = AP_amount + Overfitting_amount * 100;
                c.initializeDescription();
                logger.info("Updated the pre-train card in draw pile during WeightSaving use. AP_amount: " + AP_amount + ", Overfitting_amount: " + Overfitting_amount + ".");
                break;
            }
        }

        card_it = AbstractDungeon.player.hand.group.iterator();
        while(card_it.hasNext()) {
            c = (AbstractCard)card_it.next();
            if (c.uuid.equals(this.pre_train_card_uuid)) {
                ((PreTrainedModel)c).storage_AP = AP_amount;
                ((PreTrainedModel)c).storage_Overfitting = Overfitting_amount;
                ((PreTrainedModel)c).misc = AP_amount + Overfitting_amount * 100;
                c.initializeDescription();
                logger.info("Updated the pre-train card in hand during WeightSaving use. AP_amount: " + AP_amount + ", Overfitting_amount: " + Overfitting_amount + ".");
                break;
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        AbstractCard c = super.makeCopy();
        if (c instanceof WeightSaving) {
            ((WeightSaving)c).pre_train_card_uuid = this.pre_train_card_uuid;
        }
        return c;
    }
}
