package wrestler.cards.indigo;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import wrestler.cards.AbstractWrestlerCard;
import wrestler.characters.TheWrestler;
import wrestler.powers.CompelledPower;

import static wrestler.Wrestler.makeCardPath;

public class CharmingWords extends AbstractWrestlerCard {

    // TEXT DECLARATION

    public static final String ID = wrestler.Wrestler.makeID(CharmingWords.class.getSimpleName());
    public static final String IMG = makeCardPath("Skill.png");


    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheWrestler.Enums.COLOR_INDIGO;

    private static final int COST = 1;

    private static final int BLOCK = 8;
    private static final int UPGRADE_BLOCK = 3;
    private static final int COMPELLED = 1;
    private static final int UPGRADE_COMPELLED = 1;

    // /STAT DECLARATION/


    public CharmingWords() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        block = baseBlock = BLOCK;
        magicNumber = baseMagicNumber = COMPELLED;
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
        addToBot(new ApplyPowerAction(m, p, new CompelledPower(m, p, magicNumber), magicNumber));
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_BLOCK);
            upgradeMagicNumber(UPGRADE_COMPELLED);
            initializeDescription();
        }
    }
}
