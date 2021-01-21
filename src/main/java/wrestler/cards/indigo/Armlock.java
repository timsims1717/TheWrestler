package wrestler.cards.indigo;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import wrestler.cards.AbstractWrestlerCard;
import wrestler.characters.TheWrestler;
import wrestler.powers.GrapplePower;

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

    private static final int GRAPPLE = 4;
    private static final int UPGRADE_GRAPPLE = 2;

    public Armlock() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseGrapple = grapple = GRAPPLE;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(m, p, new GrapplePower(m, p, grapple), grapple));
        int mGrapple = 0;
        if (m.hasPower(GrapplePower.POWER_ID)) {
            mGrapple += m.getPower(GrapplePower.POWER_ID).amount;
        }
        tempStrDown(p, m, mGrapple + grapple);
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeGrappleNumber(UPGRADE_GRAPPLE);
            initializeDescription();
        }
    }
}
