package DeepSpiringMod.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;

import DeepSpiringMod.patches.PlayerColorEnum;
import DeepSpiringMod.powers.HiddenStatePower;
import basemod.abstracts.CustomCard;
import DeepSpiringMod.helpers.ModHelper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RecurrentNeuralNetwork extends CustomCard {
    public static final String ID = ModHelper.makePath("RecurrentNeuralNetwork");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源
    // private static final String NAME = "打击";
    private static final String NAME = CARD_STRINGS.NAME; // 读取本地化的名字
    private static final String IMG_PATH = "DeepSpiringModResources/img/cards/RecurrentNeuralNetwork.png";
    private static final int COST = 1;
    // private static final String DESCRIPTION = "造成 !D! 点伤害。";
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION; // 读取本地化的描述
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = PlayerColorEnum.DEEP_BLUE;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    public static final Logger logger = LogManager.getLogger(RecurrentNeuralNetwork.class);

    public RecurrentNeuralNetwork() {
        // 为了命名规范修改了变量名。这些参数具体的作用见下方
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        // logger.info("Start to init RecurrentNeuralNetwork.\n");
        // this.baseDamage = this.damage = 0;
        // this.baseBlock = this.block = 0;
        // int[] AP_Overfitting = this.get_AP();
        // int AP = AP_Overfitting[0];
        // int Overfitting = AP_Overfitting[1];
        // if (!this.upgraded) {
        //     this.baseMagicNumber = Math.max(AP - Overfitting, 0);
        //     this.magicNumber = Math.max(AP - Overfitting, 0);
        // } else {
        //     this.baseMagicNumber = Math.max(AP - Overfitting, 0) + 1;
        //     this.magicNumber = Math.max(AP - Overfitting, 0) + 1;
        // }
        // this.baseMagicNumber = Math.max(AP - Overfitting, 0);
        // this.magicNumber = Math.max(AP - Overfitting, 0);
        // this.magicNumber = this.baseMagicNumber = 1;
        // logger.info("RecurrentNeuralNetwork initialization completed.\n");
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName(); // 卡牌名字变为绿色并添加“+”，且标为升级过的卡牌，之后不能再升级。
            // this.upgradeMagicNumber(1);
            // this.upgradeDamage(3);

            // 加上以下两行就能使用UPGRADE_DESCRIPTION了（如果你写了的话）
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // logger.info("now magicNumber is " + this.magicNumber + ".");
        // logger.info("cards has played: " + AbstractDungeon.actionManager.cardsPlayedThisTurn.size());
        // if (!AbstractDungeon.actionManager.cardsPlayedThisTurn.isEmpty()) {
        //     Iterator var2 = AbstractDungeon.actionManager.cardsPlayedThisTurn.iterator();
        //     while(var2.hasNext()) {
        //         AbstractCard c = (AbstractCard)var2.next();
        //         logger.info("card played: " + c.name);
        //     }
        // }
        // if (AbstractDungeon.actionManager.cardsPlayedThisTurn.size() >= 2) {
        //     Iterator var2 = AbstractDungeon.actionManager.cardsPlayedThisTurn.iterator();
        //     AbstractCard c = (AbstractCard)var2.next();
        //     AbstractCard c_buf = (AbstractCard)var2.next();
        //     while(var2.hasNext()) {
        //         c = c_buf;
        //         c_buf = (AbstractCard)var2.next();
        //     }
        //     for (int i = 0; i < this.magicNumber; ++i) {
        //         AbstractCard tmp = c.makeStatEquivalentCopy();
        //         tmp.exhaust = true;
        //         tmp.purgeOnUse = true;
        //         tmp.energyOnUse = tmp.costForTurn;
        //         tmp.freeToPlayOnce = true;

        //         AbstractDungeon.player.limbo.addToBottom(tmp);
        //         // 这里方向要随机加个偏置，防止牌叠一块了
        //         tmp.current_x = c.current_x;
        //         tmp.current_y = c.current_y;
        //         tmp.target_x = (Settings.WIDTH / 2.0F * Settings.scale) * MathUtils.random(0.8F, 1.2F);
        //         tmp.target_y = Settings.HEIGHT / 2.0F * MathUtils.random(0.8F, 1.2F);

        //         logger.info("RecurrentNeuralNetwork use(): adding NewQueueCardAction.");
        //         this.addToBot(new NewQueueCardAction(tmp, true, false, true));
        //     }
        // } else {
        //     logger.info("RecurrentNeuralNetwork use(): No cards played this turn.");
        // }

        int AP = ModHelper.get_AP();
        int final_add = AP;
        if (this.upgraded) {
            final_add = AP + 1;
        }
        int now_RNN = 0;
        if (p.hasPower(HiddenStatePower.POWER_ID)) {
            now_RNN = p.getPower(HiddenStatePower.POWER_ID).amount;
        }
        int to_decrease = Math.max(final_add + now_RNN - 10, 0);
        final_add -= to_decrease;
        
        logger.info("AP: " + AP + ", now_RNN: " + now_RNN + ", to_decrease: " + to_decrease + ", final_add: " + final_add + ".");
        this.addToBot(new ApplyPowerAction(p, p, new HiddenStatePower(p, final_add), final_add));
    }

    // @Override
    // public void update_with_AP(int AP, int Overfitting) {
    //     if (!this.upgraded) {
    //         this.baseMagicNumber = Math.max(AP - Overfitting, 0);
    //         this.magicNumber = Math.max(AP - Overfitting, 0);
    //     } else {
    //         this.baseMagicNumber = Math.max(AP - Overfitting, 0) + 1;
    //         this.magicNumber = Math.max(AP - Overfitting, 0) + 1;
    //     }
        
    //     this.initializeDescription();
    // }

    // public void triggerOnEndOfPlayerTurn() {
    //     super.triggerOnEndOfPlayerTurn();
    //     baseBlock = baseDamage = 0;
    // }
}
