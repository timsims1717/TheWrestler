package wrestler.cards.indigo;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import wrestler.actions.LoseHPMindAction;
import wrestler.cards.AbstractWrestlerCard;
import wrestler.characters.TheWrestler;
import wrestler.powers.ComboPower;
import wrestler.powers.CompelledPower;
import wrestler.powers.GrapplePower;

import java.util.Iterator;

import static wrestler.Wrestler.makeCardPath;


public class EldritchPortal_Wrestler extends AbstractWrestlerCard {

    // TEXT DECLARATION

    public static final String ID = wrestler.Wrestler.makeID(EldritchPortal_Wrestler.class.getSimpleName());
    public static final String IMG = makeCardPath("Skill.png");// "public static final String IMG = makeCardPath("${NAME}.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.


    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheWrestler.Enums.COLOR_INDIGO;

    private static final int COST = 0;
    private static final int GRAPPLE = 2;
    private static final int UPGRADE_GRAPPLE = 1;
    private static final int COMPELLED = 2;
    private static final int UPGRADE_COMPELLED = 1;
    private static final int LOSEHP = 2;
    private static final int UPGRADE_LOSEHP = 1;

    public EldritchPortal_Wrestler() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        grapple = baseGrapple = GRAPPLE;
        magicNumber = baseMagicNumber = COMPELLED;
        loseHP = baseLoseHP = LOSEHP;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        devoid();
        for (AbstractMonster mon : AbstractDungeon.getCurrRoom().monsters.monsters) {
            addToBot(new ApplyPowerAction(mon, p, new GrapplePower(mon, p, grapple), grapple));
        }
        for (AbstractMonster mon : AbstractDungeon.getCurrRoom().monsters.monsters) {
            addToBot(new ApplyPowerAction(mon, p, new CompelledPower(mon, p, magicNumber), magicNumber));
        }
        for (AbstractMonster mon : AbstractDungeon.getCurrRoom().monsters.monsters) {
            addToBot(new LoseHPMindAction(mon, p, loseHP));
        }
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeGrappleNumber(UPGRADE_GRAPPLE);
            upgradeMagicNumber(UPGRADE_COMPELLED);
            upgradeLoseHPNumber(UPGRADE_LOSEHP);
            initializeDescription();
        }
    }
}
