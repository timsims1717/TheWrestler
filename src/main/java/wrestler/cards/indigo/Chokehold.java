package wrestler.cards.indigo;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import wrestler.cards.AbstractWrestlerCard;
import wrestler.characters.TheWrestler;
import wrestler.powers.GrapplePower;
import wrestler.powers.SubmissionPower;

import static wrestler.Wrestler.makeCardPath;

public class Chokehold extends AbstractWrestlerCard {

    // TEXT DECLARATION

    public static final String ID = wrestler.Wrestler.makeID(Chokehold.class.getSimpleName());
    public static final String IMG = makeCardPath("Attack.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheWrestler.Enums.COLOR_INDIGO;

    private static final int COST = 1;

    private static final int TIMES = 2;
    private static final int UPGRADE_TIMES = 1;

    // /STAT DECLARATION/

    public Chokehold() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = TIMES;
        damage = baseDamage = 0;
        requiresTargetGrapple = true;
    }


    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        calculateCardDamage(m);
        addToBot(new DamageAction(m, new DamageInfo(m, damage * magicNumber, damageTypeForTurn), damage > 9 ? AbstractGameAction.AttackEffect.BLUNT_HEAVY : AbstractGameAction.AttackEffect.BLUNT_LIGHT));
    }

    @Override // todo: add damage to card (needs this one and apply powers)
    public void calculateCardDamage(AbstractMonster mo) {
        if (mo != null && mo.hasPower(SubmissionPower.POWER_ID)) {
            damage = baseDamage = mo.getPower(SubmissionPower.POWER_ID).amount;
        } else {
            damage = baseDamage = 0;
        }
        super.calculateCardDamage(mo);
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_TIMES);
            initializeDescription();
        }
    }
}
