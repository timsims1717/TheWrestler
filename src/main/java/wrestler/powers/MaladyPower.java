package wrestler.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import wrestler.actions.PsychicDamageAction;
import wrestler.util.TextureLoader;

import static wrestler.Wrestler.makePowerPath;

public class MaladyPower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;
    public int increaseAmount;

    public static final String POWER_ID = wrestler.Wrestler.makeID(MaladyPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public MaladyPower(final AbstractCreature owner, final AbstractCreature source, final int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        this.increaseAmount = amount;
//        if (source.hasPower(TormentPower.POWER_ID)) {
//            this.amount += source.getPower(TormentPower.POWER_ID).amount;
//        }
        this.source = source;

        type = PowerType.DEBUFF;
        isTurnBased = false;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
//        if (source.hasPower(TormentPower.POWER_ID)) {
//            this.amount += source.getPower(TormentPower.POWER_ID).amount;
//        }
        increaseAmount += stackAmount;
    }

    @Override
    public void onExhaust(AbstractCard abstractCard) {
        if (abstractCard.cardID.equals(VoidCard.ID)) {
            flash();
            addToTop(new ApplyPowerAction(owner, owner, new MaladyPower(owner, source, increaseAmount)));
            addToTop(new PsychicDamageAction(owner, owner, amount));
            updateDescription();
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1] + increaseAmount + DESCRIPTIONS[2];
    }

    @Override
    public AbstractPower makeCopy() {
        return new MaladyPower(owner, source, amount);
    }
}
