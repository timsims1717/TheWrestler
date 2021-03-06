package wrestler.cards.indigo;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.defect.DoubleEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import wrestler.cards.AbstractWrestlerCard;
import wrestler.characters.TheWrestler;

import static wrestler.Wrestler.makeCardPath;

public class TagTeam extends AbstractWrestlerCard {

    // TEXT DECLARATION

    public static final String ID = wrestler.Wrestler.makeID(TagTeam.class.getSimpleName());
    public static final String IMG = makeCardPath("Skill.png");// "public static final String IMG = makeCardPath("${NAME}.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheWrestler.Enums.COLOR_INDIGO;

    private static final int COST = 2;
    private static final int UPGRADE_COST = 1;

    // /STAT DECLARATION/

    public TagTeam() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        exhaust = true;
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DoubleEnergyAction());
        int count = Math.max(p.hand.size() - 1, 0);
        addToBot(new DrawCardAction(count));
        if (p.hasPower(StrengthPower.POWER_ID)) {
            int str = p.getPower(StrengthPower.POWER_ID).amount;
            addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, str), str));
            addToBot(new ApplyPowerAction(p, p, new LoseStrengthPower(p, str), str));
        }
        if (p.hasPower(DexterityPower.POWER_ID)) {
            int dex = p.getPower(DexterityPower.POWER_ID).amount;
            addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, dex), dex));
            addToBot(new ApplyPowerAction(p, p, new LoseDexterityPower(p, dex), dex));
        }
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
            initializeDescription();
        }
    }
}
