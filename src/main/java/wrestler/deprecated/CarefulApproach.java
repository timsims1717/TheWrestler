package wrestler.deprecated;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import wrestler.cards.AbstractWrestlerCard;
import wrestler.characters.TheWrestler;
import wrestler.powers.GrapplePower;

import static wrestler.Wrestler.makeCardPath;

public class CarefulApproach extends AbstractWrestlerCard {

    // TEXT DECLARATION

    public static final String ID = wrestler.Wrestler.makeID(CarefulApproach.class.getSimpleName());
    public static final String IMG = makeCardPath("Skill.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheWrestler.Enums.COLOR_INDIGO;

    private static final int COST = 0;

    private static final int GRAPPLE = 1;
    private static final int DRAW = 1;

    // /STAT DECLARATION/

    public CarefulApproach() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        grapple = baseGrapple = GRAPPLE;
        magicNumber = baseMagicNumber = DRAW;
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (upgraded) {
            for (AbstractMonster mon : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (!mon.isDeadOrEscaped()) {
                    addToTop(new ApplyPowerAction(mon, p, new GrapplePower(mon, p, grapple), grapple));
                }
            }
        } else {
            addToTop(new ApplyPowerAction(m, p, new GrapplePower(m, p, grapple), grapple));
        }
        addToBot(new DrawCardAction(magicNumber));
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            target = CardTarget.ALL_ENEMY;
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
