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

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheWrestler.Enums.COLOR_INDIGO;

    private static final int COST = 1;

    private static final int GRAPPLE = 2;
    private static final int UPGRADE_GRAPPLE = 1;

    public Armlock() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseGrapple = grapple = GRAPPLE;
        exhaust = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (AbstractMonster mon : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!mon.isDeadOrEscaped()) {
                addToTop(new ApplyPowerAction(mon, p, new GrapplePower(mon, p, grapple), grapple));
            }
        }
        for (AbstractMonster mon : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!isTargetGrappled(mon)) {
                int mGrapple = mon.getPower(GrapplePower.POWER_ID).amount;
                tempStrDown(p, mon, mGrapple + grapple);
            }
        }
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeGrappleNumber(UPGRADE_GRAPPLE);
            initializeDescription();
        }
    }
}
