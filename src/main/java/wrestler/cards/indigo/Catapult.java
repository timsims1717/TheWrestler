package wrestler.cards.indigo;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import wrestler.actions.DamageAllGrappledEnemiesAction;
import wrestler.cards.AbstractWrestlerCard;
import wrestler.characters.TheWrestler;
import wrestler.powers.GrapplePower;

import static wrestler.Wrestler.makeCardPath;

public class Catapult extends AbstractWrestlerCard {

    // TEXT DECLARATION

    public static final String ID = wrestler.Wrestler.makeID(Catapult.class.getSimpleName());
    public static final String IMG = makeCardPath("Attack.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheWrestler.Enums.COLOR_INDIGO;

    private static final int COST = 2;

    private static final int DAMAGE = 19;
    private static final int UPGRADE_DMG = 5;

    // /STAT DECLARATION/

    public Catapult() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        damage = baseDamage = DAMAGE;
        requiresTargetGrapple = true;
        isMultiDamage = true;
    }


    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (AbstractMonster mon : AbstractDungeon.getCurrRoom().monsters.monsters) {
            addToBot(new RemoveSpecificPowerAction(mon, mon, GrapplePower.POWER_ID));
        }

        addToBot(new DamageAllEnemiesAction(p, multiDamage, damageTypeForTurn, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DMG);
            initializeDescription();
        }
    }
}
