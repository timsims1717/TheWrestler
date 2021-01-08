package wrestler.cards.indigo;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import wrestler.cards.AbstractWrestlerCard;
import wrestler.characters.TheWrestler;
import wrestler.powers.GrapplePower;

import static wrestler.Wrestler.makeCardPath;

public class BearHug extends AbstractWrestlerCard {

    // TEXT DECLARATION

    public static final String ID = wrestler.Wrestler.makeID(BearHug.class.getSimpleName());
    public static final String IMG = makeCardPath("Attack.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheWrestler.Enums.COLOR_INDIGO;

    private static final int COST = 2;

    private static final int GRAPPLE = 2;
    private static final int UPGRADE_GRAPPLE = 1;

    // /STAT DECLARATION/

    public BearHug() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        grapple = baseGrapple = GRAPPLE;
        damage = baseDamage = GRAPPLE;
        block = baseBlock = GRAPPLE;
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        calculateCardDamage(m);
        calculateBlock(m);
        addToBot(new ApplyPowerAction(m, p, new GrapplePower(m, p, grapple), grapple));
        addToBot(new GainBlockAction(p, p, block));
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), damage > 9 ? AbstractGameAction.AttackEffect.BLUNT_HEAVY : AbstractGameAction.AttackEffect.BLUNT_LIGHT));
    }

    public void calculateBlock(AbstractMonster mo) {
        if (mo != null && mo.hasPower(GrapplePower.POWER_ID)) {
            block = baseBlock = mo.getPower(GrapplePower.POWER_ID).amount + grapple;
        } else {
            block = baseBlock = grapple;
        }
        applyPowersToBlock();
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
