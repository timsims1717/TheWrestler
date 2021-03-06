package wrestler.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import wrestler.deprecated.CloseQuartersPower;
import wrestler.util.TextureLoader;

import static wrestler.Wrestler.makePowerPath;
import static wrestler.patches.PsychicDamagePatch.PSYCHIC_DAMAGE;

public class GrapplePower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public static final String POWER_ID = wrestler.Wrestler.makeID(GrapplePower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("GrapplePower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("GrapplePower32.png"));

    public GrapplePower(final AbstractCreature owner, final AbstractCreature source, final int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        this.source = source;

        type = PowerType.DEBUFF;
        isTurnBased = true;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        this.description = DESCRIPTIONS[0];
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
        int total = 1;
        if (!AbstractDungeon.player.hasPower(WeightTrainingPower.POWER_ID)) {
            if (owner.hasPower(StrengthPower.POWER_ID)) {
                int str = owner.getPower(StrengthPower.POWER_ID).amount;
                if (str > 0) {
                    total += str;
                }
            }
        } else {
            AbstractDungeon.player.getPower(WeightTrainingPower.POWER_ID).flash();
        }
        addToBot(new ReducePowerAction(owner, owner, POWER_ID, total));
    }

    @Override
    public AbstractPower makeCopy() {
        return new GrapplePower(owner, source, amount);
    }
}
