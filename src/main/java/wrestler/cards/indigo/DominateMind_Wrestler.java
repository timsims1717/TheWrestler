package wrestler.cards.indigo;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import wrestler.actions.LoseHPMindAction;
import wrestler.cards.AbstractWrestlerCard;
import wrestler.characters.TheWrestler;
import wrestler.deprecated.MindvicePower;
import wrestler.powers.CompelledPower;

import static wrestler.Wrestler.makeCardPath;


public class DominateMind_Wrestler extends AbstractWrestlerCard {

    // TEXT DECLARATION

    public static final String ID = wrestler.Wrestler.makeID(DominateMind_Wrestler.class.getSimpleName());
    public static final String IMG = makeCardPath("Skill.png");// "public static final String IMG = makeCardPath("${NAME}.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.


    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheWrestler.Enums.COLOR_INDIGO;

    private static final int COST = 4;
    private static final int LOSEHP = 15;
    private static final int UPGRADE_LOSEHP = 6;
    private static final int COMPULSION = 5;
    private static final int UPGRADE_COMPULSION = 2;

    public DominateMind_Wrestler() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        loseHP = baseLoseHP = LOSEHP;
        magicNumber = baseMagicNumber = COMPULSION;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(m, p, new CompelledPower(m, p, magicNumber), magicNumber));
        addToBot(new LoseHPMindAction(m, p, loseHP));
        super.use(p,m);
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeLoseHPNumber(UPGRADE_LOSEHP);
            upgradeMagicNumber(UPGRADE_COMPULSION);
            initializeDescription();
        }
    }
}
