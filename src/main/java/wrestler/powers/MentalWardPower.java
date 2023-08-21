package wrestler.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import wrestler.actions.MentalWardAction;
import wrestler.util.TextureLoader;

import static wrestler.Wrestler.makePowerPath;
import static wrestler.patches.PsychicDamagePatch.PSYCHIC_EFFECT;

public class MentalWardPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = wrestler.Wrestler.makeID(MentalWardPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("MentalWardPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("MentalWardPower32.png"));

    public MentalWardPower(final AbstractCreature owner, final int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;

        type = PowerType.BUFF;
        isTurnBased = true;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    public void triggerTop(AbstractCreature source) {
        addToTop(new MentalWardAction(source, amount));
    }

    public void triggerBot(AbstractCreature source) {
        addToBot(new MentalWardAction(source, amount));
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new MentalWardPower(owner, amount);
    }
}
