package wrestler.deprecated;

import wrestler.actions.MakeRandomCardAction;
import wrestler.cards.AbstractTriggerOnDrawnCard;
import wrestler.characters.TheWrestler;

import static wrestler.Wrestler.makeCardPath;

public class Delusion_Wrestler extends AbstractTriggerOnDrawnCard {

    // TEXT DECLARATION

    public static final String ID = wrestler.Wrestler.makeID(Delusion_Wrestler.class.getSimpleName());
    public static final String IMG = makeCardPath("Skill.png");// "public static final String IMG = makeCardPath("${NAME}.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.


    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheWrestler.Enums.COLOR_INDIGO;

    public Delusion_Wrestler() {
        super(ID, IMG, TYPE, COLOR, RARITY, TARGET);
    }

    @Override
    public void triggerWhenDrawn() {
        addToTop(new MakeRandomCardAction());
    }
}
