package wrestler.deprecated;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import wrestler.actions.IntoNothingOldAction;
import wrestler.cards.AbstractWrestlerCard;
import wrestler.characters.TheWrestler;

import static wrestler.Wrestler.makeCardPath;
import static wrestler.patches.PsychicDamagePatch.PSYCHIC_DAMAGE;

public class IntoNothing_Old extends AbstractWrestlerCard {

    // TEXT DECLARATION

    public static final String ID = wrestler.Wrestler.makeID(IntoNothing_Old.class.getSimpleName());
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

    // /STAT DECLARATION/

    public IntoNothing_Old() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        psychic = basePsychic = PSYCHIC;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new IntoNothingOldAction(m, new DamageInfo(p, psychic, PSYCHIC_DAMAGE)));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PSYCHIC);
            initializeDescription();
        }
    }
}
