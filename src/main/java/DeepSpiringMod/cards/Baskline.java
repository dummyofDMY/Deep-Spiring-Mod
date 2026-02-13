package DeepSpiringMod.cards;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import DeepSpiringMod.helpers.ModHelper;
import DeepSpiringMod.patches.PlayerColorEnum;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Baskline extends AbstractAPCard {
    public static final String ID = ModHelper.makePath("Baskline");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源
    // private static final String NAME = "打击";
    private static final String NAME = CARD_STRINGS.NAME; // 读取本地化的名字
    // private static final String IMG_PATH = "DeepSpiringModResources/img/cards/Strike_DeepBlue.png";
    private static final String IMG_PATH = "DeepSpiringModResources/img/cards/Baskline.png";
    private static final int COST = 2;
    // private static final String DESCRIPTION = "造成 !D! 点伤害。";
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION; // 读取本地化的描述
	private static final String[] EXTENDED_DESCRIPTION = CARD_STRINGS.EXTENDED_DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = PlayerColorEnum.DEEP_BLUE;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    // public static final int damge_without_AP = 16;

    public static final Logger logger = LogManager.getLogger(Baskline.class);

    public Baskline() {
        // 为了命名规范修改了变量名。这些参数具体的作用见下方
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        // logger.info("Start to init StrikeDataset.\n");
        this.block = 30;
		this.baseBlock = this.block;
        this.magicNumber = 10;
        this.baseMagicNumber = this.magicNumber;
        int[] AP_list = get_AP();
        update_with_AP(AP_list[0], AP_list[1]);
        // logger.info("StrikeDataset initialization completed.\n");
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName(); // 卡牌名字变为绿色并添加“+”，且标为升级过的卡牌，之后不能再升级。
            this.upgradeMagicNumber(-5);
            // this.upgradeBlock(10); // 将该卡牌的伤害提高3点。

            // 加上以下两行就能使用UPGRADE_DESCRIPTION了（如果你写了的话）
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
			new GainBlockAction(p, p, this.block)
		);
    }

    @Override
    public void update_with_AP(int AP, int Overfitting) {
		int new_block = 0;
		if (!this.upgraded) {
			new_block = Math.max(30 - Overfitting * 10, 0);
		} else {
			new_block = Math.max(30 - Overfitting * 5, 0);
		}
        this.baseBlock = new_block;
        this.block = new_block;
        this.upgradedBlock = true;
        this.initializeDescription();
    }

	@Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (p.hasPower(ModHelper.makePath("AP"))) {
            return true;
        } else {
            this.cantUseMessage = EXTENDED_DESCRIPTION[0];
            return false;
        }
    }
}
