package wrestler.cards.indigo;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import wrestler.cards.AbstractWrestlerCard;
import wrestler.characters.TheWrestler;

import static wrestler.Wrestler.makeCardPath;

public class Vanish extends AbstractWrestlerCard {

    // TEXT DECLARATION

    public static final String ID = wrestler.Wrestler.makeID(Vanish.class.getSimpleName());
    public static final String IMG = makeCardPath("Skill.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheWrestler.Enums.COLOR_INDIGO;

    private static final int COST = 2;

    private static final int BLOCK = 20;
    private static final int UPGRADE_BLOCK = 5;

    // /STAT DECLARATION/


    public Vanish() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        block = baseBlock = BLOCK;
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        devoid();
        addToBot(new GainBlockAction(p, p, block));
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_BLOCK);
            initializeDescription();
        }
    }
}
