package wrestler.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public abstract class AbstractTriggerOnDrawnCard extends AbstractWrestlerCard {

    public static String UPGRADE_DESCRIPTION = "";

    private static final int COST = -2;

    public AbstractTriggerOnDrawnCard(final String id,
                                      final String img,
                                      final CardType type,
                                      final CardColor color,
                                      final CardRarity rarity,
                                      final CardTarget target) {
        super(id, img, COST, type, color, rarity, target);
        isEthereal = true;
        UPGRADE_DESCRIPTION = CardCrawlGame.languagePack.getCardStrings(id).UPGRADE_DESCRIPTION;
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {}

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            isEthereal = false;
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}