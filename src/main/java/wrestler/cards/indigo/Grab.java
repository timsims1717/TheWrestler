package wrestler.cards.indigo;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import wrestler.cards.AbstractWrestlerCard;
import wrestler.characters.TheWrestler;
import wrestler.powers.GrapplePower;

import static wrestler.Wrestler.makeCardPath;

public class Grab extends AbstractWrestlerCard {

    // TEXT DECLARATION

    public static final String ID = wrestler.Wrestler.makeID(Grab.class.getSimpleName());
    public static final String IMG = makeCardPath("Grab.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheWrestler.Enums.COLOR_INDIGO;

    private static final int COST = 0;

    private static final int GRAPPLE = 2;
    private static final int UPGRADE_GRP = 1;
    private static final int BLOCK = 2;
    private static final int UPGRADE_BLOCK = 1;

    // /STAT DECLARATION/

    public Grab() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        grapple = baseGrapple = GRAPPLE;
        block = baseBlock = BLOCK;
    }


    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(m, p, new GrapplePower(m, p, grapple), grapple));
        addToBot(new GainBlockAction(p, p, block));
    }


    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_BLOCK);
            upgradeGrappleNumber(UPGRADE_GRP);
            initializeDescription();
        }
    }
}
