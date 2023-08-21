package wrestler.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import wrestler.patches.ComboPatch;
import wrestler.util.TextureLoader;

import static wrestler.Wrestler.makePowerPath;

public class UntouchablePower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = wrestler.Wrestler.makeID(UntouchablePower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public UntouchablePower(final AbstractCreature owner, final int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;

        type = PowerType.BUFF;
        isTurnBased = false;

        region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void atEndOfTurnPreEndTurnCards(boolean isPlayer) {
        if (isPlayer) {
            AbstractPlayer p = (AbstractPlayer) owner;
            if (ComboPatch.ComboScoreValue.currentCombo.get(p) > 0) {
                int mult = ComboPatch.ComboScoreValue.currentMultiplier.get(p);
                flash();
                for (int i = 0; i < amount; i++) {
                    addToBot(new GainBlockAction(owner, owner, mult));
                }
            }
        }
    }

    @Override
    public void updateDescription() {
        if (amount > 1) {
            description = DESCRIPTIONS[0] + DESCRIPTIONS[1] + amount + DESCRIPTIONS[3];
        } else {
            description = DESCRIPTIONS[0] + DESCRIPTIONS[2];
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new UntouchablePower(owner, amount);
    }
}
