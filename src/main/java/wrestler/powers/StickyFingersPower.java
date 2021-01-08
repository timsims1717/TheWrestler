package wrestler.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import wrestler.actions.DamageAllGrappledEnemiesAction;
import wrestler.util.TextureLoader;

import java.util.Iterator;

import static wrestler.Wrestler.makePowerPath;

public class StickyFingersPower extends AbstractPower implements CloneablePowerInterface {
    private final boolean upgraded;

    public static final String POWER_ID = wrestler.Wrestler.makeID(StickyFingersPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public StickyFingersPower(final AbstractCreature owner, final int amount, final boolean upgraded) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        this.upgraded = upgraded;

        type = PowerType.BUFF;
        isTurnBased = true;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void onAfterCardPlayed(AbstractCard usedCard) {
        if (upgraded) {
            for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (!m.isDeadOrEscaped()) {
                    addToTop(new ApplyPowerAction(m, owner, new GrapplePower(m, owner, amount), amount));
                }
            }
        } else {
            AbstractMonster m = AbstractDungeon.getRandomMonster();
            addToTop(new ApplyPowerAction(m, owner, new GrapplePower(m, owner, amount), amount));
        }
        flash();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        addToBot(new RemoveSpecificPowerAction(owner, owner, this));
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1] + (upgraded ? DESCRIPTIONS[3] : DESCRIPTIONS[2]);
    }

    @Override
    public AbstractPower makeCopy() {
        return new StickyFingersPower(owner, amount, upgraded);
    }
}
