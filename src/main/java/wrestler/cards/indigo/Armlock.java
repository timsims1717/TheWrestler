package wrestler.cards.indigo;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import wrestler.cards.AbstractWrestlerCard;
import wrestler.characters.TheWrestler;
import wrestler.powers.GrapplePower;
import wrestler.powers.SubmissionPower;

import static wrestler.Wrestler.makeCardPath;


public class Armlock extends AbstractWrestlerCard {

    // TEXT DECLARATION

    public static final String ID = wrestler.Wrestler.makeID(Armlock.class.getSimpleName());
    public static final String IMG = makeCardPath("Skill.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheWrestler.Enums.COLOR_INDIGO;

    private static final int COST = 1;

    private static final int SUBMISSION = 4;
    private static final int UPGRADE_SUBMISSION = 2;

    public Armlock() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = SUBMISSION;
        requiresTargetGrapple = true;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(m, p, new SubmissionPower(m, p, magicNumber), magicNumber));
        int mSubmission = magicNumber;
        if (m.hasPower(SubmissionPower.POWER_ID)) {
            mSubmission += m.getPower(SubmissionPower.POWER_ID).amount;
        }
        tempStrDown(p, m, mSubmission);
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_SUBMISSION);
            initializeDescription();
        }
    }
}
