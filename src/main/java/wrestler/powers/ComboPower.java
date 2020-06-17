package wrestler.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.IncreaseMaxHpAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import wrestler.cards.AbstractWrestlerCard;
import wrestler.util.TextureLoader;

import static wrestler.Wrestler.makePowerPath;

public class ComboPower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;
    public boolean protect = false;

    public static final String POWER_ID = wrestler.Wrestler.makeID(ComboPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public static final int THRESHOLD = 4;
    public static final int BLOCK = 1;
    public static final int DRAW = 2;
    public static final int ENERGY = 3;
    public static final int GOLD1 = 4;
    public static final int HEAL = 5;
    public static final int GOLD2 = 6;
    public static final int CARD = 7;
    public static final int GOLD3 = 8;
    public static final int MAX_HP = 9;
    public static final int GOLD4 = 10;

    public static final int BLOCK_AMOUNT = 4;
    public static final int DRAW_AMOUNT = 1;
    public static final int ENERGY_AMOUNT = 1;
    public static final int HEAL_AMOUNT = 7;
    public static final int MAX_HP_AMOUNT = 4;
    public static final int GOLD1_AMOUNT = 10;
    public static final int GOLD2_AMOUNT = 25;
    public static final int GOLD3_AMOUNT = 50;
    public static final int GOLD4_AMOUNT = 100;

    public ComboPower(final AbstractCreature owner, final AbstractCreature source, final int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        this.source = source;

        type = PowerType.BUFF;
        isTurnBased = false;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void atStartOfTurnPostDraw() {
        if (amount >= THRESHOLD * ENERGY) {
            addToBot(new GainEnergyAction(ENERGY_AMOUNT));
            flash();
        }
    }

    @Override
    public void atEndOfTurnPreEndTurnCards(boolean isPlayer) {
        if (amount >= THRESHOLD * BLOCK) {
            addToBot(new GainBlockAction(owner, BLOCK_AMOUNT));
            flash();
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        if (stackAmount < 0) {
            return;
        }
        fontScale = 8.0F;
        amount += stackAmount;
        AbstractPlayer player = AbstractDungeon.player;
        if (amount >= THRESHOLD * DRAW && amount - stackAmount < THRESHOLD * DRAW) {
            player.gameHandSize += DRAW_AMOUNT;
        }
    }

    @Override
    public void updateDescription() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            if (i > 0) {
                sb.append(" NL ");
            }
            int j = (i + 1) * THRESHOLD;
            if (j <= amount) {
                sb.append("#b");
            } else {
                if (j - THRESHOLD <= amount) {
                    sb.append("#y");
                }
            }
            sb.append(j);
            sb.append(": ");
            sb.append(DESCRIPTIONS[i]);
        }
        description = sb.toString();
    }

    @Override
    public int onLoseHp(int damageAmount) {
        if (!protect) {
            breakCombo();
        }
        protect = false;
        return damageAmount;
    }

    @Override
    public void onAfterCardPlayed(AbstractCard usedCard) {
        if (!AbstractDungeon.player.hasPower(RelentlessPower.POWER_ID)) {
            if (usedCard instanceof AbstractWrestlerCard) {
                AbstractWrestlerCard card = (AbstractWrestlerCard) usedCard;
                if (card.isCombo) {
                    return;
                }
            }
            breakCombo();
        }
    }

    private void breakCombo() {
        AbstractPlayer player = AbstractDungeon.player;
        if (amount >= THRESHOLD * DRAW) {
            player.gameHandSize -= DRAW_AMOUNT;
        }
        addToBot(new RemoveSpecificPowerAction(owner, owner, this));
    }

    @Override
    public AbstractPower makeCopy() {
        return new ComboPower(owner, source, amount);
    }
}
