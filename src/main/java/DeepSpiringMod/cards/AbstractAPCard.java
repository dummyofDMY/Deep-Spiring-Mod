package DeepSpiringMod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;

import basemod.abstracts.CustomCard;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AbstractAPCard extends CustomCard{
    public static final Logger logger = LogManager.getLogger(AbstractAPCard.class);

    public AbstractAPCard(String id, String name, String img, int cost, String rawDescription, AbstractCard.CardType type, AbstractCard.CardColor color, AbstractCard.CardRarity rarity, AbstractCard.CardTarget target) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
        logger.info("Start to init AbstractAPCard.\n");
        logger.info("AbstractAPCard initialization completed.\n");
    }

    public AbstractAPCard(String id, String name, RegionName img, int cost, String rawDescription, AbstractCard.CardType type, AbstractCard.CardColor color, AbstractCard.CardRarity rarity, AbstractCard.CardTarget target) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
        logger.info("Start to init AbstractAPCard.\n");
        logger.info("AbstractAPCard initialization completed.\n");
    }

    public abstract void update_with_AP(int AP, int Overfitting);
}
