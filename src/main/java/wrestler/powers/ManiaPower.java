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

public class ManiaPower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public static final String POWER_ID = wrestler.Wrestler.makeID(ManiaPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public ManiaPower(final AbstractCreature owner, final int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;

        type = PowerType.DEBUFF;
        isTurnBased = true;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        AbstractDungeon.player.gameHandSize -= amount;

        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        fontScale = 8.0F;
        amount += stackAmount;
        AbstractDungeon.player.gameHandSize -= amount;
    }

    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1] + (amount == 1 ? DESCRIPTIONS[2] : DESCRIPTIONS[3]);
    }

    @Override
    public void onRemove() {
        AbstractDungeon.player.gameHandSize += amount;
    }

    @Override
    public void atStartOfTurnPostDraw() {
        flash();
        addToBot(new RemoveSpecificPowerAction(owner, owner, this));
    }

    @Override
    public AbstractPower makeCopy() {
        return new ManiaPower(owner, amount);
    }
}
