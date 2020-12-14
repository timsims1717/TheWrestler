package wrestler.cards.indigo;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import wrestler.actions.PsychicDamageAction;
import wrestler.cards.AbstractWrestlerCard;
import wrestler.characters.TheWrestler;
import wrestler.powers.GrapplePower;

import static wrestler.Wrestler.makeCardPath;


public class InvisibleHand_Wrestler extends AbstractWrestlerCard {

    // TEXT DECLARATION

    public static final String ID = wrestler.Wrestler.makeID(InvisibleHand_Wrestler.class.getSimpleName());
    public static final String IMG = makeCardPath("InvisibleHand.png");


    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheWrestler.Enums.COLOR_INDIGO;

    private static final int COST = 0;
    private static final int GRAPPLE = 2;
    private static final int UPGRADE_GRAPPLE = 1;
    private static final int LOSEHP = 2;
    private static final int UPGRADE_LOSEHP = 1;

    public InvisibleHand_Wrestler() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        grapple = baseGrapple = GRAPPLE;
        psychic = basePsychic = LOSEHP;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        devoid();
        for (AbstractMonster mon : AbstractDungeon.getCurrRoom().monsters.monsters) {
            addToBot(new ApplyPowerAction(mon, p, new GrapplePower(mon, p, grapple), grapple));
        }
        for (AbstractMonster mon : AbstractDungeon.getCurrRoom().monsters.monsters) {
            addToBot(new PsychicDamageAction(mon, p, psychic));
        }
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeGrappleNumber(UPGRADE_GRAPPLE);
            upgradePsychicDamageNumber(UPGRADE_LOSEHP);
            initializeDescription();
        }
    }
}
