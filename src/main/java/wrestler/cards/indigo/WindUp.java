package wrestler.cards.indigo;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import wrestler.actions.WindUpAction;
import wrestler.cards.AbstractWrestlerCard;
import wrestler.characters.TheWrestler;

import static wrestler.Wrestler.makeCardPath;

public class WindUp extends AbstractWrestlerCard {

    // TEXT DECLARATION

    public static final String ID = wrestler.Wrestler.makeID(WindUp.class.getSimpleName());
    public static final String IMG = makeCardPath("Skill.png");


    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheWrestler.Enums.COLOR_INDIGO;

    private static final int COST = 1;

    private static final int VIGOR = 5;
    private static final int UPGRADE_VIGOR = 3;
    private static final int ENERGY = 1;

    // /STAT DECLARATION/

    public WindUp() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = VIGOR;
    }

    @Override
    public void triggerOnGlowCheck() {
        glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        int cardsPlayed = AbstractDungeon.actionManager.cardsPlayedThisCombat.size();
        if (cardsPlayed > 0 && ((AbstractCard)AbstractDungeon.actionManager.cardsPlayedThisCombat.get(cardsPlayed - 1)).type == AbstractCard.CardType.ATTACK) {
            glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        }
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new VigorPower(p, magicNumber), magicNumber));
        addToBot(new WindUpAction(ENERGY));
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_VIGOR);
            initializeDescription();
        }
    }
}
