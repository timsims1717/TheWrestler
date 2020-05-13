package wrestler.cards.indigo;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import wrestler.cards.AbstractWrestlerCard;
import wrestler.characters.TheWrestler;
import wrestler.powers.GrapplePower;

import java.util.Iterator;

import static wrestler.Wrestler.makeCardPath;


public class Melee_Wrestler extends AbstractWrestlerCard {

    // TEXT DECLARATION

    public static final String ID = wrestler.Wrestler.makeID(Melee_Wrestler.class.getSimpleName());
    public static final String IMG = makeCardPath("Attack.png");// "public static final String IMG = makeCardPath("${NAME}.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.


    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheWrestler.Enums.COLOR_INDIGO;

    private static final int COST = 1;

    private static final int DAMAGE = 5;
    private static final int UPGRADE_DMG = 3;
    private static final int GRAPPLE = 1;
    private static final int UPGRADE_GRAPPLE = 1;

    public Melee_Wrestler() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        damage = baseDamage = DAMAGE;
        grapple = baseGrapple = GRAPPLE;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAllEnemiesAction(p, damage, damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        Iterator var1 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();

        while (var1.hasNext()) {
            AbstractMonster mon = (AbstractMonster) var1.next();
            addToBot(new ApplyPowerAction(mon, p, new GrapplePower(mon, p, grapple), grapple));
        }
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DMG);
            upgradeGrappleNumber(UPGRADE_GRAPPLE);
            initializeDescription();
        }
    }
}