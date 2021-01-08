package wrestler.cards.indigo;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import wrestler.cards.AbstractWrestlerCard;
import wrestler.characters.TheWrestler;
import wrestler.powers.GrapplePower;

import static wrestler.Wrestler.makeCardPath;

public class PenetratingShot extends AbstractWrestlerCard {

    // TEXT DECLARATION

    public static final String ID = wrestler.Wrestler.makeID(PenetratingShot.class.getSimpleName());
    public static final String IMG = makeCardPath("Skill.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheWrestler.Enums.COLOR_INDIGO;

    private static final int COST = 0;

    private static final int GRAPPLE = 3;
    private static final int UPGRADE_GRP = 2;
    private static final int VULN = 1;
    private static final int UPGRADE_VULN = 1;

    // /STAT DECLARATION/

    public PenetratingShot() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        grapple = baseGrapple = GRAPPLE;
        magicNumber = baseMagicNumber = VULN;
    }

    @Override
    public void triggerOnGlowCheck() {
        glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();

        for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (m != null && !m.isDeadOrEscaped() && !m.hasPower(GrapplePower.POWER_ID)) {
                glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
                break;
            }
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        boolean hasGrapple = isTargetGrappled(m);
        addToBot(new ApplyPowerAction(m, p, new GrapplePower(m, p, grapple), grapple));
        if (!hasGrapple) {
            addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, magicNumber, false), magicNumber));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeGrappleNumber(UPGRADE_GRP);
            upgradeMagicNumber(UPGRADE_VULN);
            initializeDescription();
        }
    }
}
