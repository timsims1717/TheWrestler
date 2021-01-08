package wrestler.cards.indigo;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.WeakPower;
import wrestler.cards.AbstractWrestlerCard;
import wrestler.characters.TheWrestler;

import java.lang.reflect.Field;

import static com.megacrit.cardcrawl.monsters.AbstractMonster.Intent.*;
import static wrestler.Wrestler.makeCardPath;

public class MindMeld extends AbstractWrestlerCard {

    // TEXT DECLARATION

    public static final String ID = wrestler.Wrestler.makeID(MindMeld.class.getSimpleName());
    public static final String IMG = makeCardPath("Skill.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheWrestler.Enums.COLOR_INDIGO;

    private static final int COST = 2;
    private static final int UPGRADE_COST = 1;

    private static final int WEAK = 3;

    // /STAT DECLARATION/

    public MindMeld() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = WEAK;
        exhaust = true;
    }


    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m.intent == ATTACK
                || m.intent == ATTACK_BUFF
                || m.intent == ATTACK_DEBUFF
                || m.intent == ATTACK_DEFEND) {
            try {
                Field f = AbstractMonster.class.getDeclaredField("move");
                f.setAccessible(true);
                EnemyMoveInfo move = (EnemyMoveInfo) f.get(m);
                int multi = 1;
                if (move.isMultiDamage) {
                    multi = move.multiplier;
                }

                int attack = m.getIntentDmg();
                addToBot(new GainBlockAction(p, attack * multi));
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        addToBot(new ApplyPowerAction(m, p, new WeakPower(m, magicNumber, false), magicNumber));
    }


    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
            initializeDescription();
        }
    }
}
