package wrestler.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import wrestler.util.TextureLoader;

import static wrestler.Wrestler.makePowerPath;

public class GrapplePower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;
    public int turnsSinceApplied;
    public int turnsUntilGone;

    public static final String POWER_ID = wrestler.Wrestler.makeID(GrapplePower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("GrapplePower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("GrapplePower32.png"));

    public GrapplePower(final AbstractCreature owner, final AbstractCreature source) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        turnsSinceApplied = 0;
        this.source = source;

        type = PowerType.DEBUFF;
        isTurnBased = true;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        turnsSinceApplied = 0;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        turnsUntilGone = 1;
        AbstractPlayer p = AbstractDungeon.player;
        if (p.hasPower(WeightTrainingPower.POWER_ID)) {
            turnsUntilGone += p.getPower(WeightTrainingPower.POWER_ID).amount;
        }
        int diff = turnsUntilGone - turnsSinceApplied;
        if (diff == 0) {
            description = DESCRIPTIONS[0];
        } else if (diff == 1) {
            description = DESCRIPTIONS[1];
        } else {
            description = DESCRIPTIONS[2] + diff + DESCRIPTIONS[3];
        }
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (!target.equals(owner) && power.ID.equals(POWER_ID)) {
            if (AbstractDungeon.player.hasPower(AgilityTrainingPower.POWER_ID)) {
                AbstractDungeon.player.getPower(AgilityTrainingPower.POWER_ID).flash();
            } else {
                addToBot(new RemoveSpecificPowerAction(owner, owner, POWER_ID));
            }
        }
    }

    @Override
    public void atEndOfTurn(final boolean isPlayer) {
        turnsSinceApplied += 1;
        turnsUntilGone = 1;
        AbstractPlayer p = AbstractDungeon.player;
        if (p.hasPower(WeightTrainingPower.POWER_ID)) {
            turnsUntilGone += p.getPower(WeightTrainingPower.POWER_ID).amount;
        }
        if (turnsUntilGone < turnsSinceApplied) {
            addToBot(new RemoveSpecificPowerAction(owner, owner, POWER_ID));
        } else {
            updateDescription();
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new GrapplePower(owner, source);
    }
}
