package wrestler.cards.indigo;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import wrestler.cards.AbstractWrestlerCard;
import wrestler.characters.TheWrestler;
import wrestler.powers.GrapplePower;

import static wrestler.Wrestler.makeCardPath;

public class OutOfReach extends AbstractWrestlerCard {

    // TEXT DECLARATION

    public static final String ID = wrestler.Wrestler.makeID(OutOfReach.class.getSimpleName());
    public static final String IMG = makeCardPath("Skill.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheWrestler.Enums.COLOR_INDIGO;

    private static final int COST = 1;

    private static final int BLOCK = 3;
    private static final int UPGRADE_BLOCK = 2;

    // /STAT DECLARATION/

    // todo: add total to card?
    public OutOfReach() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        block = baseBlock = BLOCK;
        wantsTargetGrapple = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int grappleTotal = 0;
        for (AbstractMonster mon : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (isTargetGrappled(mon)) {
                grappleTotal += mon.getPower(GrapplePower.POWER_ID).amount;
                addToBot(new RemoveSpecificPowerAction(mon, p, GrapplePower.POWER_ID));
            }
        }
        if (grappleTotal > 0) {
            addToBot(new GainBlockAction(p, block * grappleTotal)); // todo: should this be one block action per monster?
        }
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_BLOCK);
            initializeDescription();
        }
    }
}
