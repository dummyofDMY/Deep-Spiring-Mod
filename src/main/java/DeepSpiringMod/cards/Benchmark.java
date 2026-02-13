package DeepSpiringMod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect.ShockWaveType;
import com.megacrit.cardcrawl.core.Settings;
import com.badlogic.gdx.graphics.Color;

import DeepSpiringMod.helpers.ModHelper;
import DeepSpiringMod.patches.PlayerColorEnum;
import basemod.abstracts.CustomCard;

import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Benchmark extends CustomCard {
	public static final String ID = ModHelper.makePath("Benchmark");
	private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源
	// private static final String NAME = "打击";
	private static final String NAME = CARD_STRINGS.NAME; // 读取本地化的名字
	private static final String IMG_PATH = "DeepSpiringModResources/img/cards/Benchmark.png";
	private static final int COST = 0;
	// private static final String DESCRIPTION = "造成 !D! 点伤害。";
	private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION; // 读取本地化的描述
	private static final CardType TYPE = CardType.SKILL;
	private static final CardColor COLOR = PlayerColorEnum.DEEP_BLUE;
	private static final CardRarity RARITY = CardRarity.COMMON;
	private static final CardTarget TARGET = CardTarget.SELF_AND_ENEMY;

	public static final Logger logger = LogManager.getLogger(Benchmark.class);

	public Benchmark() {
		// 为了命名规范修改了变量名。这些参数具体的作用见下方
		super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
		// logger.info("Start to init Benchmark.\n");
		this.baseMagicNumber = this.magicNumber = 1;
        this.exhaust = true;
		// logger.info("Benchmark initialization completed.\n");
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		if (p.hasPower(ModHelper.makePath("Overfitting"))) {
            int overfitting_amount = p.getPower(ModHelper.makePath("Overfitting")).amount;
            if (this.upgraded) {
                overfitting_amount = overfitting_amount / 2;
            }
			this.addToBot(new ApplyPowerAction(p, p, new WeakPower(p, overfitting_amount, false), overfitting_amount));
		}
        if (p.hasPower(ModHelper.makePath("AP"))) {
            this.addToBot(new SFXAction("ATTACK_PIERCING_WAIL"));
            if (Settings.FAST_MODE) {
                this.addToBot(new VFXAction(p, new ShockWaveEffect(p.hb.cX, p.hb.cY, Color.BLUE, ShockWaveType.CHAOTIC), 0.3F));
            } else {
                this.addToBot(new VFXAction(p, new ShockWaveEffect(p.hb.cX, p.hb.cY, Color.BLUE, ShockWaveType.CHAOTIC), 1.5F));
            }
            int AP_amount = p.getPower(ModHelper.makePath("AP")).amount;
            Iterator var3 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();

            AbstractMonster mo;
            while(var3.hasNext()) {
                mo = (AbstractMonster)var3.next();
                this.addToBot(new ApplyPowerAction(mo, p, new StrengthPower(mo, -AP_amount), -AP_amount, true, AttackEffect.NONE));
            }
        }
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName(); // 卡牌名字变为绿色并添加“+”，且标为升级过的卡牌，之后不能再升级。
            this.upgradeMagicNumber(1);
			// this.upgradeBlock(3);

			// 加上以下两行就能使用UPGRADE_DESCRIPTION了（如果你写了的话）
			this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
			this.initializeDescription();
		}
	}
}