package wrestler.cards.indigo;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import wrestler.actions.PsychicDamageAction;
import wrestler.cards.AbstractWrestlerCard;
import wrestler.characters.TheWrestler;

import static wrestler.Wrestler.makeCardPath;


public class Migraine_Wrestler extends AbstractWrestlerCard {

    // TEXT DECLARATION

    public static final String ID = wrestler.Wrestler.makeID(Migraine_Wrestler.class.getSimpleName());
    public static final String IMG = makeCardPath("Skill.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheWrestler.Enums.COLOR_INDIGO;

    private static final int COST = 1;
    private static final int LOSEHP = 8;
    private static final int UPGRADE_LOSEHP = 4;
    private static final int INCREASE = 4;
    private static final int UPGRADE_INCREASE = 2;

    public Migraine_Wrestler() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        psychic = basePsychic = LOSEHP;
        magicNumber = baseMagicNumber = INCREASE;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new PsychicDamageAction(m, p, psychic));
        basePsychic += magicNumber;
        psychic += magicNumber;
        baseMagicNumber += magicNumber / 2;
        magicNumber += magicNumber / 2;
        isPsychicModified = true;
        isMagicNumberModified = true;
        initializeDescription();
        super.use(p,m);
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradePsychicDamageNumber(UPGRADE_LOSEHP);
            upgradeMagicNumber(UPGRADE_INCREASE);
            initializeDescription();
        }
    }
}
