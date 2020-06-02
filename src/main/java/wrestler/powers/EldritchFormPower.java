package wrestler.powers;

import basemod.BaseMod;
import basemod.interfaces.CloneablePowerInterface;
import basemod.interfaces.PostExhaustSubscriber;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import wrestler.util.TextureLoader;

import static wrestler.Wrestler.makePowerPath;

public class EldritchFormPower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public static final String POWER_ID = wrestler.Wrestler.makeID(EldritchFormPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public EldritchFormPower(final AbstractCreature owner, final AbstractCreature source, final int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        this.source = source;

        type = PowerType.BUFF;
        isTurnBased = false;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        if (card.cardID.equals(VoidCard.ID)) {
            flash();
            addToBot(new GainEnergyAction(amount));
            addToBot(new DrawCardAction(amount));
        }
    }

    @Override
    public void updateDescription() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < amount; i++) {
            s.append(DESCRIPTIONS[1]);
        }
        description = DESCRIPTIONS[0] + s.toString() + DESCRIPTIONS[2];
    }

    @Override
    public AbstractPower makeCopy() {
        return new EldritchFormPower(owner, source, amount);
    }
}
