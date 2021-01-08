package wrestler.cards.indigo;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import wrestler.cards.AbstractWrestlerCard;
import wrestler.characters.TheWrestler;
import wrestler.powers.GrapplePower;

import static wrestler.Wrestler.makeCardPath;
import static wrestler.patches.PsychicDamagePatch.PSYCHIC_DAMAGE;
import static wrestler.patches.PsychicDamagePatch.PSYCHIC_EFFECT;


public class InvisibleHand extends AbstractWrestlerCard {

    // TEXT DECLARATION

    public static final String ID = wrestler.Wrestler.makeID(InvisibleHand.class.getSimpleName());
    public static final String IMG = makeCardPath("InvisibleHand.png");


    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheWrestler.Enums.COLOR_INDIGO;

    private static final int COST = 0;
    private static final int GRAPPLE = 1;
    private static final int UPGRADE_GRAPPLE = 1;
    private static final int PSYCHIC = 2;
    private static final int UPGRADE_PSYCHIC = 1;

    public InvisibleHand() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        grapple = baseGrapple = GRAPPLE;
        psychic = basePsychic = PSYCHIC;
        isMultiDamage = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAllEnemiesAction(p, multiPsychicDamage, PSYCHIC_DAMAGE, PSYCHIC_EFFECT));
        for (AbstractMonster mon : AbstractDungeon.getCurrRoom().monsters.monsters) {
            addToBot(new ApplyPowerAction(mon, p, new GrapplePower(mon, p, grapple), grapple));
        }
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeGrappleNumber(UPGRADE_GRAPPLE);
            upgradePsychicDamageNumber(UPGRADE_PSYCHIC);
            initializeDescription();
        }
    }
}
