package wrestler.powers;

import basemod.interfaces.CloneablePowerInterface;
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

public class WeirdConduitPower extends AbstractPower implements CloneablePowerInterface {
    public int energyAmount;
    public int drawAmount;

    public static final String POWER_ID = wrestler.Wrestler.makeID(WeirdConduitPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public WeirdConduitPower(final AbstractCreature owner, final int energyAmount, final int drawAmount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.energyAmount = energyAmount;
        this.drawAmount = drawAmount;
        this.amount = drawAmount;

        type = PowerType.BUFF;
        isTurnBased = false;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        this.addToBot(new GainEnergyAction(energyAmount));
        this.flash();
    }

    @Override
    public void atStartOfTurnPostDraw() {
        addToBot(new DrawCardAction(drawAmount));
    }

    @Override
    public void updateDescription() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < energyAmount; i++) {
            s.append(DESCRIPTIONS[1]);
        }
        description = DESCRIPTIONS[0] + s.toString() + DESCRIPTIONS[2] + drawAmount + (drawAmount == 1 ? DESCRIPTIONS[3] : DESCRIPTIONS[4]);
    }

    @Override
    public AbstractPower makeCopy() {
        return new WeirdConduitPower(owner, energyAmount, drawAmount);
    }
}
