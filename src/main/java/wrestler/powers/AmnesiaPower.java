package wrestler.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import wrestler.actions.ForgetAction;
import wrestler.util.TextureLoader;

import static wrestler.Wrestler.makePowerPath;

public class AmnesiaPower extends AbstractPower implements CloneablePowerInterface {
    public boolean upgraded;

    public static final String POWER_ID = wrestler.Wrestler.makeID(AmnesiaPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("AmnesiaPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("AmnesiaPower32.png"));

    public AmnesiaPower(final AbstractCreature owner, final int amount, final boolean upgraded) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        this.upgraded = upgraded;

        type = PowerType.BUFF;
        isTurnBased = false;

        region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        flash();
        addToBot(new ForgetAction(amount, upgraded));
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[upgraded ? 1 : 0] + amount + DESCRIPTIONS[2];
    }

    @Override
    public AbstractPower makeCopy() {
        return new AmnesiaPower(owner, amount, upgraded);
    }
}
