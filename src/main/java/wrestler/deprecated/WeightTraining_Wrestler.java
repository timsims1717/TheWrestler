package wrestler.deprecated;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import wrestler.cards.AbstractWrestlerCard;
import wrestler.characters.TheWrestler;

import static wrestler.Wrestler.makeCardPath;


public class WeightTraining_Wrestler extends AbstractWrestlerCard {

    // TEXT DECLARATION

    public static final String ID = wrestler.Wrestler.makeID(WeightTraining_Wrestler.class.getSimpleName());
    public static final String IMG = makeCardPath("WeightTraining_Wrestler.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;


    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = TheWrestler.Enums.COLOR_INDIGO;

    private static final int COST = 1;

    private static final int TURNS = 4;
    private static final int UPGRADE_TURNS = 1;

    public WeightTraining_Wrestler() {
        this(0);
    }

    public WeightTraining_Wrestler(int upgrades) {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = TURNS;
        isInnate = true;
        timesUpgraded = upgrades;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new WeightTrainingPower(p, p, magicNumber), magicNumber));
        super.use(p,m);
    }

    @Override
    public boolean canUpgrade() {
        return true;
    }

    @Override
    public void upgrade() {
        timesUpgraded++;
        upgradeMagicNumber(UPGRADE_TURNS);
        upgraded = true;
        name = NAME + "+" + timesUpgraded;
        initializeTitle();
        initializeDescription();
    }

    @Override
    public AbstractCard makeCopy() {
        return new WeightTraining_Wrestler(timesUpgraded);
    }
}
