package wrestler.deprecated;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import wrestler.cards.AbstractWrestlerCard;
import wrestler.characters.TheWrestler;
import wrestler.powers.InfectionPower;

import static wrestler.Wrestler.makeCardPath;


public class Malady_Wrestler extends AbstractWrestlerCard {

    // TEXT DECLARATION

    public static final String ID = wrestler.Wrestler.makeID(Malady_Wrestler.class.getSimpleName());
    public static final String IMG = makeCardPath("Attack.png");


    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheWrestler.Enums.COLOR_INDIGO;

    private static final int COST = 1;

    private static final int DAMAGE = 10;
    private static final int HP_LOSS = 8;
    private static final int UPGRADE_HP_LOSS = 3;
    private static final int INCREASE = 8;
    private static final int UPGRADE_INCREASE = 3;

    public Malady_Wrestler() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        damage = baseDamage = DAMAGE;
        psychic = basePsychic = HP_LOSS;
        magicNumber = baseMagicNumber = INCREASE;
        exhaust = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        devoid();
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        addToBot(new ApplyPowerAction(m, p, new InfectionPower(m, p, magicNumber), psychic));
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradePsychicDamageNumber(UPGRADE_HP_LOSS);
            upgradeMagicNumber(UPGRADE_INCREASE);
            initializeDescription();
        }
    }

    public AbstractCard makeCopy() {
        return new Malady_Wrestler();
    }
}
