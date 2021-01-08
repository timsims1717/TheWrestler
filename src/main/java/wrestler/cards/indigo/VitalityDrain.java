package wrestler.cards.indigo;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import wrestler.actions.VitalityDrainAction;
import wrestler.cards.AbstractWrestlerCard;
import wrestler.characters.TheWrestler;
import wrestler.powers.GrapplePower;

import static wrestler.Wrestler.makeCardPath;
import static wrestler.patches.PsychicDamagePatch.PSYCHIC_DAMAGE;
import static wrestler.patches.PsychicDamagePatch.PSYCHIC_EFFECT;

public class VitalityDrain extends AbstractWrestlerCard {

    // TEXT DECLARATION

    public static final String ID = wrestler.Wrestler.makeID(VitalityDrain.class.getSimpleName());
    public static final String IMG = makeCardPath("Attack.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;


    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheWrestler.Enums.COLOR_INDIGO;

    private static final int COST = 2;

    // /STAT DECLARATION/


    public VitalityDrain() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        requiresTargetGrapple = true;
        exhaust = true;
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m.hasPower(GrapplePower.POWER_ID)) {
            int grpl = m.getPower(GrapplePower.POWER_ID).amount;
            addToBot(new DamageAction(m, new DamageInfo(p, grpl, PSYCHIC_DAMAGE), PSYCHIC_EFFECT));
            addToBot(new VitalityDrainAction(p, m, upgraded));
        }
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
