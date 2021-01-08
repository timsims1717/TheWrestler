package wrestler.cards.indigo;

import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import wrestler.cards.AbstractWrestlerCard;
import wrestler.characters.TheWrestler;

import static wrestler.Wrestler.makeCardPath;
import static wrestler.patches.PsychicDamagePatch.PSYCHIC_DAMAGE;
import static wrestler.patches.PsychicDamagePatch.PSYCHIC_EFFECT;


public class Migraine extends AbstractWrestlerCard {

    // TEXT DECLARATION

    public static final String ID = wrestler.Wrestler.makeID(Migraine.class.getSimpleName());
    public static final String IMG = makeCardPath("Attack.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheWrestler.Enums.COLOR_INDIGO;

    private static final int COST = 1;
    private static final int PSYCHIC = 8;
    private static final int UPGRADE_PSYCHIC = 4;
    private static final int INCREASE = 4;
    private static final int UPGRADE_INCREASE = 2;

    public Migraine() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        psychic = basePsychic = PSYCHIC;
        magicNumber = baseMagicNumber = INCREASE;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageRandomEnemyAction(new DamageInfo(p, psychic, PSYCHIC_DAMAGE), PSYCHIC_EFFECT));
        basePsychic += magicNumber;
        baseMagicNumber += magicNumber / 2;
        applyPowers();
        initializeDescription();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradePsychicDamageNumber(UPGRADE_PSYCHIC);
            upgradeMagicNumber(UPGRADE_INCREASE);
            initializeDescription();
        }
    }
}
