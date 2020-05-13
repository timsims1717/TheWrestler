package wrestler.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import wrestler.util.TextureLoader;

import static wrestler.Wrestler.makePowerPath;

public class HorrorPower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;
    public boolean appliedToPlayer;
    public int oldAmount;

    public static final String POWER_ID = wrestler.Wrestler.makeID(HorrorPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public HorrorPower(final AbstractCreature owner, final AbstractCreature source, final int amount, final boolean appliedToPlayer) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        this.source = source;

        type = PowerType.DEBUFF;
        isTurnBased = false;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        this.appliedToPlayer = appliedToPlayer;
        oldAmount = 0;
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        if (amount > 0 && oldAmount != amount) {
            addToBot(new LoseHPAction(this.owner, (AbstractCreature) null, amount, AbstractGameAction.AttackEffect.FIRE));
            oldAmount = amount;
        } else {
            addToBot(new ReducePowerAction(this.owner, this.owner, POWER_ID, half()));
            oldAmount = amount - half();
        }
        flash();
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0]
                + (appliedToPlayer ? DESCRIPTIONS[1] : DESCRIPTIONS[2])
                + DESCRIPTIONS[3] + amount + DESCRIPTIONS[4] + half()
                + DESCRIPTIONS[5] + (oldAmount == amount ? DESCRIPTIONS[6] : "")
                + DESCRIPTIONS[7];
    }

    public int half() {
        return (int)Math.ceil((double)amount * 0.5);
    }

    @Override
    public AbstractPower makeCopy() {
        return new HorrorPower(owner, source, amount, appliedToPlayer);
    }
}
