package wrestler.deprecated;

import com.megacrit.cardcrawl.actions.unique.RandomizeHandCostAction;
import wrestler.cards.AbstractTriggerOnDrawnCard;
import wrestler.characters.TheWrestler;

import static wrestler.Wrestler.makeCardPath;

public class Disorientation_Wrestler extends AbstractTriggerOnDrawnCard {

    // TEXT DECLARATION

    public static final String ID = wrestler.Wrestler.makeID(Disorientation_Wrestler.class.getSimpleName());
    public static final String IMG = makeCardPath("Skill.png");// "public static final String IMG = makeCardPath("${NAME}.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.


    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheWrestler.Enums.COLOR_INDIGO;

    public Disorientation_Wrestler() {
        super(ID, IMG, TYPE, COLOR, RARITY, TARGET);
    }

    @Override
    public void triggerWhenDrawn() {
        addToBot(new RandomizeHandCostAction());
    }
}
