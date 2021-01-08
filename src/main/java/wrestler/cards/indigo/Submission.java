package wrestler.cards.indigo;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import wrestler.actions.SubmissionAction;
import wrestler.cards.AbstractWrestlerCard;
import wrestler.characters.TheWrestler;
import wrestler.powers.GrapplePower;

import static wrestler.Wrestler.makeCardPath;

public class Submission extends AbstractWrestlerCard {

    // TEXT DECLARATION

    public static final String ID = wrestler.Wrestler.makeID(Submission.class.getSimpleName());
    public static final String IMG = makeCardPath("Skill.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheWrestler.Enums.COLOR_INDIGO;

    private static final int COST = 2;

    private static final int GRAPPLE = 12;
    private static final int UPGRADE_GRP = 3;

    // /STAT DECLARATION/

    public Submission() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        grapple = baseGrapple = GRAPPLE;
        exhaust = true;
    }


    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null) {
            addToTop(new ApplyPowerAction(m, p, new GrapplePower(m, p, grapple), grapple));
            addToBot(new WaitAction(0.8F));
            addToBot(new SubmissionAction(m));
        }
    }


    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeGrappleNumber(UPGRADE_GRP);
            initializeDescription();
        }
    }
}
