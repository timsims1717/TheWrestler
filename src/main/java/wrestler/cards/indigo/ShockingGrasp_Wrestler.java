package wrestler.cards.indigo;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import wrestler.actions.DamageAllGrappledEnemiesAction;
import wrestler.cards.AbstractWrestlerCard;
import wrestler.characters.TheWrestler;

import java.util.ArrayList;

import static wrestler.Wrestler.makeCardPath;


public class ShockingGrasp_Wrestler extends AbstractWrestlerCard {

    // TEXT DECLARATION

    public static final String ID = wrestler.Wrestler.makeID(ShockingGrasp_Wrestler.class.getSimpleName());
    public static final String IMG = makeCardPath("Attack.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheWrestler.Enums.COLOR_INDIGO;

    private static final int COST = 1;

    private static final int DAMAGE = 3;
    private static final int UPGRADE_DAMAGE = 1;
    private static final int COUNT = 3;

    public ShockingGrasp_Wrestler() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        damage = baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = COUNT;
        isMultiDamage = true;
        wantsTargetGrapple = true;
    }

    // Actions the card should do. todo: change to a Shocking Grasp Action
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        boolean anyGrappled = false;
        for (AbstractMonster mon : AbstractDungeon.getMonsters().monsters) {
            if (isTargetGrappled(mon)) {
                anyGrappled = true;
            }
        }
        if (anyGrappled) {
            for (int i = 0; i < magicNumber; i++) {
                addToTop(new SFXAction("ORB_LIGHTNING_EVOKE", 0.1F));
                for (int j = 0; j < AbstractDungeon.getCurrRoom().monsters.monsters.size(); j++) {
                    AbstractMonster mon = AbstractDungeon.getCurrRoom().monsters.monsters.get(j);
                    if (isTargetGrappled(mon)) {
                        addToBot(new VFXAction(new LightningEffect(mon.drawX, mon.drawY), 0.05F));
                        mon.damage(new DamageInfo(p, multiDamage[j], damageTypeForTurn));
                    }
                }
                addToBot(new WaitAction(0.8F));
            }
        }
        super.use(p,m);
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DAMAGE);
            initializeDescription();
        }
    }
}
