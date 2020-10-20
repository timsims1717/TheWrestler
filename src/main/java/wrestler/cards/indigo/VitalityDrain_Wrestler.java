package wrestler.cards.indigo;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import wrestler.cards.AbstractWrestlerCard;
import wrestler.characters.TheWrestler;

import static wrestler.Wrestler.makeCardPath;


public class VitalityDrain_Wrestler extends AbstractWrestlerCard {

    // TEXT DECLARATION

    public static final String ID = wrestler.Wrestler.makeID(VitalityDrain_Wrestler.class.getSimpleName());
    public static final String IMG = makeCardPath("Skill.png");// "public static final String IMG = makeCardPath("${NAME}.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheWrestler.Enums.COLOR_INDIGO;

    private static final int COST = 0;

    private static final int STR_DOWN = 4;
    private static final int UPGRADE_STR_DOWN = 2;

    public VitalityDrain_Wrestler() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = STR_DOWN;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        tempStrDown(p, m, magicNumber);
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_STR_DOWN);
            initializeDescription();
        }
    }
}
