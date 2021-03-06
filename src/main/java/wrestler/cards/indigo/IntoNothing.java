package wrestler.cards.indigo;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import wrestler.actions.IntoNothingOldAction;
import wrestler.actions.PsychicDamageAction;
import wrestler.cards.AbstractWrestlerCard;
import wrestler.characters.TheWrestler;
import wrestler.powers.IntoNothingPower;

import static wrestler.Wrestler.makeCardPath;
import static wrestler.patches.PsychicDamagePatch.PSYCHIC_DAMAGE;

public class IntoNothing extends AbstractWrestlerCard {

    // TEXT DECLARATION

    public static final String ID = wrestler.Wrestler.makeID(IntoNothing.class.getSimpleName());
    public static final String IMG = makeCardPath("Attack.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheWrestler.Enums.COLOR_INDIGO;

    private static final int COST = 2;

    private static final int PSYCHIC = 10;
    private static final int UPGRADE_PSYCHIC = 4;
    private static final int VOID = 10;
    private static final int UPGRADE_VOID = 4;

    // /STAT DECLARATION/

    public IntoNothing() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        psychic = basePsychic = PSYCHIC;
        magicNumber = baseMagicNumber = VOID;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        devoid();
        addToBot(new PsychicDamageAction(m, p, psychic));
        addToBot(new ApplyPowerAction(m, p, new IntoNothingPower(m, p, magicNumber), magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PSYCHIC);
            upgradeMagicNumber(UPGRADE_VOID);
            initializeDescription();
        }
    }
}
