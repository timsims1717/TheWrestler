package wrestler.cards.indigo;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import wrestler.cards.AbstractWrestlerCard;
import wrestler.characters.TheWrestler;
import wrestler.powers.CompelledPower;

import static wrestler.Wrestler.makeCardPath;


public class Hypnosis extends AbstractWrestlerCard {

    // TEXT DECLARATION

    public static final String ID = wrestler.Wrestler.makeID(Hypnosis.class.getSimpleName());
    public static final String IMG = makeCardPath("Attack.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheWrestler.Enums.COLOR_INDIGO;

    private static final int COST = 1;

    private static final int PSYCHIC = 5;
    private static final int UPGRADE_PSYCHIC = 3;
    private static final int COMPELLED = 3;
    private static final int UPGRADE_COMPELLED = 1;

    public Hypnosis() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        psychic = basePsychic = PSYCHIC;
        magicNumber = baseMagicNumber = COMPELLED;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(m, p, new CompelledPower(m, p, magicNumber), magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradePsychicDamageNumber(UPGRADE_PSYCHIC);
            upgradeMagicNumber(UPGRADE_COMPELLED);
            initializeDescription();
        }
    }
}
