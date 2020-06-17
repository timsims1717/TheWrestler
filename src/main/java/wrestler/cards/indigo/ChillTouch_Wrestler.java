package wrestler.cards.indigo;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import wrestler.cards.AbstractWrestlerCard;
import wrestler.characters.TheWrestler;
import wrestler.powers.ChillTouchPower;

import static wrestler.Wrestler.makeCardPath;

public class ChillTouch_Wrestler extends AbstractWrestlerCard {

    // TEXT DECLARATION

    public static final String ID = wrestler.Wrestler.makeID(ChillTouch_Wrestler.class.getSimpleName());
    public static final String IMG = makeCardPath("Power.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = TheWrestler.Enums.COLOR_INDIGO;

    private static final int COST = 2;
    private static final int LOSEHP = 2;
    private static final int UPGRADE_LOSEHP = 1;

    // /STAT DECLARATION/

    public ChillTouch_Wrestler() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        loseHP = baseLoseHP = LOSEHP;
    }
    
    // Actions the card should do.
    @Override
    public void use(final AbstractPlayer p, final AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new ChillTouchPower(p, p, loseHP), loseHP));
        super.use(p,m);
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeLoseHPNumber(UPGRADE_LOSEHP);
            initializeDescription();
        }
    }
}