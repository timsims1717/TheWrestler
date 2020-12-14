package wrestler.cards.indigo;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import wrestler.actions.CrusherAction;
import wrestler.cards.AbstractWrestlerCard;
import wrestler.characters.TheWrestler;
import wrestler.powers.GrapplePower;

import static wrestler.Wrestler.makeCardPath;

public class Crusher_Wrestler extends AbstractWrestlerCard {

    // TEXT DECLARATION

    public static final String ID = wrestler.Wrestler.makeID(Crusher_Wrestler.class.getSimpleName());
    public static final String IMG = makeCardPath("Attack.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheWrestler.Enums.COLOR_INDIGO;

    private static final int COST = -1;

    private static final int GRAPPLE = 5;
    private static final int UPGRADE_GRAPPLE = 2;

    // /STAT DECLARATION/

    public Crusher_Wrestler() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        grapple = baseGrapple = GRAPPLE;
        damage = baseDamage = grapple;
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        calculateCardDamage(m);
        addToBot(new ApplyPowerAction(m, p, new GrapplePower(m, p, grapple), grapple));
        addToBot(new CrusherAction(m, p, damage, freeToPlayOnce, energyOnUse, upgraded));
        super.use(p,m);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        if (mo != null && mo.hasPower(GrapplePower.POWER_ID)) {
            damage = baseDamage = mo.getPower(GrapplePower.POWER_ID).amount + grapple;
        } else {
            damage = baseDamage = grapple;
        }
        super.calculateCardDamage(mo);
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
