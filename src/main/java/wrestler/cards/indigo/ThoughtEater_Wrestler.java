package wrestler.cards.indigo;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import wrestler.actions.LoseHPMindAction;
import wrestler.cards.AbstractWrestlerCard;
import wrestler.characters.TheWrestler;
import wrestler.deprecated.MindvicePower;
import wrestler.powers.CompelledPower;

import static wrestler.Wrestler.makeCardPath;


public class ThoughtEater_Wrestler extends AbstractWrestlerCard {

    // TEXT DECLARATION

    public static final String ID = wrestler.Wrestler.makeID(ThoughtEater_Wrestler.class.getSimpleName());
    public static final String IMG = makeCardPath("Skill.png");// "public static final String IMG = makeCardPath("${NAME}.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.


    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheWrestler.Enums.COLOR_INDIGO;

    private static final int COST = 2;
    private static final int LOSEHP = 12;
    private static final int UPGRADE_LOSEHP = 3;
    private static final int COMPULSION = 3;
    private static final int UPGRADE_COMPULSION = 1;

    public ThoughtEater_Wrestler() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        loseHP = baseLoseHP = LOSEHP;
        magicNumber = baseMagicNumber = COMPULSION;
        isCombo = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new LoseHPMindAction(m, p, loseHP));
        addToBot(new DrawCardAction(magicNumber));
        super.use(p,m);
    }

    @Override
    public void comboUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(m, p, new CompelledPower(m, p, magicNumber), magicNumber));
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeLoseHPNumber(UPGRADE_LOSEHP);
            upgradeMagicNumber(UPGRADE_COMPULSION);
            initializeDescription();
        }
    }
}
