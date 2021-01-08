package wrestler.cards.indigo;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import wrestler.actions.PsychicDamageAction;
import wrestler.actions.RememberAction;
import wrestler.cards.AbstractWrestlerCard;
import wrestler.characters.TheWrestler;

import static wrestler.Wrestler.makeCardPath;


public class ThoughtEater extends AbstractWrestlerCard {

    // TEXT DECLARATION

    public static final String ID = wrestler.Wrestler.makeID(ThoughtEater.class.getSimpleName());
    public static final String IMG = makeCardPath("Attack.png");


    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheWrestler.Enums.COLOR_INDIGO;

    private static final int COST = 2;
    private static final int PSYCHIC = 15;
    private static final int UPGRADE_PSYCHIC = 5;
    private static final int CARD_MOVE = 2;

    public ThoughtEater() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        psychic = basePsychic = PSYCHIC;
        magicNumber = baseMagicNumber = CARD_MOVE;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new PsychicDamageAction(m, p, psychic));
        addToBot(new RememberAction(magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradePsychicDamageNumber(UPGRADE_PSYCHIC);
            initializeDescription();
        }
    }
}
