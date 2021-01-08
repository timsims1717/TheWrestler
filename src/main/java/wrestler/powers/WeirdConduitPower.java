package wrestler.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import wrestler.actions.RememberAction;
import wrestler.util.TextureLoader;

import static wrestler.Wrestler.makePowerPath;

public class WeirdConduitPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = wrestler.Wrestler.makeID(WeirdConduitPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public WeirdConduitPower(final AbstractCreature owner, final int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;

        type = PowerType.BUFF;
        isTurnBased = false;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        addToBot(new RememberAction(amount));
        flash();
    }

    @Override
    public void atStartOfTurnPostDraw() {
        addToBot(new DrawCardAction(amount));
    }

    @Override
    public void updateDescription() {
        String card = (amount == 1 ? DESCRIPTIONS[1] : DESCRIPTIONS[2]);
        description = DESCRIPTIONS[0] + amount + card + DESCRIPTIONS[3] + amount + card + DESCRIPTIONS[4];
    }

    @Override
    public AbstractPower makeCopy() {
        return new WeirdConduitPower(owner, amount);
    }
}
