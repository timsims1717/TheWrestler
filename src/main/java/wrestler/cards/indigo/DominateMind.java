package wrestler.cards.indigo;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import wrestler.actions.PsychicDamageAction;
import wrestler.cards.AbstractWrestlerCard;
import wrestler.characters.TheWrestler;
import wrestler.powers.CompelledPower;

import static wrestler.Wrestler.makeCardPath;

public class DominateMind extends AbstractWrestlerCard {

    // TEXT DECLARATION

    public static final String ID = wrestler.Wrestler.makeID(DominateMind.class.getSimpleName());
    public static final String IMG = makeCardPath("Attack.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheWrestler.Enums.COLOR_INDIGO;

    private static final int COST = 4;
    private static final int PSYCHIC = 24;
    private static final int UPGRADE_PSYCHIC = 8;
    private static final int DEBUFF = 3;
    private static final int UPGRADE_DEBUFF = 2;

    public DominateMind() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        psychic = basePsychic = PSYCHIC;
        magicNumber = baseMagicNumber = DEBUFF;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        devoid();
        addToBot(new PsychicDamageAction(m, p, psychic));
        addToBot(new ApplyPowerAction(m, p, new WeakPower(m, magicNumber, false), magicNumber));
        addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, magicNumber, false), magicNumber));
        addToBot(new ApplyPowerAction(m, p, new CompelledPower(m, p, magicNumber), magicNumber));
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradePsychicDamageNumber(UPGRADE_PSYCHIC);
            upgradeMagicNumber(UPGRADE_DEBUFF);
            initializeDescription();
        }
    }
}
