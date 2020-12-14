package wrestler.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import wrestler.actions.PsychicDamageAction;
import wrestler.util.TextureLoader;

import static wrestler.Wrestler.makePowerPath;

public class CompelledPower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;
    private int hpLossAmount;

    public static final String POWER_ID = wrestler.Wrestler.makeID(CompelledPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("CompelledPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("CompelledPower32.png"));

    public CompelledPower(final AbstractCreature owner, final AbstractCreature source, final int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        hpLossAmount = amount * 3;
        this.source = source;

        type = PowerType.DEBUFF;
        isTurnBased = true;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void onAfterCardPlayed(AbstractCard usedCard) {
        if (usedCard.type == AbstractCard.CardType.ATTACK) {
            updateDescription();
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        hpLossAmount = amount * 4;
        updateDescription();
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        super.onAttack(info, damageAmount, target);
        if (info.type.equals(DamageInfo.DamageType.NORMAL)) {
            attack();
        }
    }

    @Override
    public void atEndOfTurn(final boolean isPlayer) {
        if (owner instanceof AbstractMonster) {
            AbstractMonster m = (AbstractMonster) owner;
            switch (m.intent) {
                case ATTACK_BUFF:
                case BUFF:
                    buff();
                    break;
                case ATTACK_DEBUFF:
                case DEBUFF:
                    debuff();
                    break;
                case ATTACK_DEFEND:
                case DEFEND:
                    block();
                    break;
                case STRONG_DEBUFF:
                    strDebuff();
                    break;
                case DEFEND_DEBUFF:
                    block();
                    debuff();
                    break;
                case DEFEND_BUFF:
                    block();
                    buff();
                    break;
                case ESCAPE:
                    escape();
                    break;
            }
        }
        addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        updateDescription();
    }

    private void attack() {
        addToTop(new PsychicDamageAction(owner, owner, hpLossAmount));
    }

    private void buff() {
        addToBot(new ApplyPowerAction(owner, owner, new WeakPower(owner, amount, true)));
    }

    private void debuff() {
        addToBot(new ApplyPowerAction(owner, owner, new VulnerablePower(owner, amount, true)));
    }

    private void block() {
        addToBot(new ApplyPowerAction(AbstractDungeon.player, owner, new DexterityPower(AbstractDungeon.player, amount)));
    }

    private void strDebuff() {
        addToBot(new ApplyPowerAction(AbstractDungeon.player, owner, new StrengthPower(AbstractDungeon.player, amount)));
    }

    private void escape() {
        addToBot(new PsychicDamageAction(owner, owner, hpLossAmount));
    }

    @Override
    public void updateDescription() {
        if (owner instanceof AbstractMonster) {
            AbstractMonster m = (AbstractMonster) owner;
            switch (m.intent) {
                case ATTACK:
                    description = DESCRIPTIONS[0] + DESCRIPTIONS[1] + hpLossAmount + DESCRIPTIONS[2] + DESCRIPTIONS[10];
                    break;
                case ATTACK_BUFF:
                    description = DESCRIPTIONS[0] + DESCRIPTIONS[1] + hpLossAmount + DESCRIPTIONS[2] + DESCRIPTIONS[9] + DESCRIPTIONS[3] + amount + DESCRIPTIONS[4] + DESCRIPTIONS[10];
                    break;
                case ATTACK_DEBUFF:
                    description = DESCRIPTIONS[0] + DESCRIPTIONS[1] + hpLossAmount + DESCRIPTIONS[2] + DESCRIPTIONS[9] + DESCRIPTIONS[3] + amount + DESCRIPTIONS[5] + DESCRIPTIONS[10];
                    break;
                case ATTACK_DEFEND:
                    description = DESCRIPTIONS[0] + DESCRIPTIONS[1] + hpLossAmount + DESCRIPTIONS[2] + DESCRIPTIONS[9] + DESCRIPTIONS[6] + amount + DESCRIPTIONS[7] + DESCRIPTIONS[10];
                    break;
                case BUFF:
                    description = DESCRIPTIONS[0] + DESCRIPTIONS[3] + amount + DESCRIPTIONS[4] + DESCRIPTIONS[10];
                    break;
                case DEBUFF:
                    description = DESCRIPTIONS[0] + DESCRIPTIONS[3] + amount + DESCRIPTIONS[5] + DESCRIPTIONS[10];
                    break;
                case STRONG_DEBUFF:
                    description = DESCRIPTIONS[0] + DESCRIPTIONS[6] + amount + DESCRIPTIONS[8] + DESCRIPTIONS[10];
                    break;
                case DEFEND:
                    description = DESCRIPTIONS[0] + DESCRIPTIONS[6] + amount + DESCRIPTIONS[7] + DESCRIPTIONS[10];
                    break;
                case DEFEND_BUFF:
                    description = DESCRIPTIONS[0] + DESCRIPTIONS[6] + amount + DESCRIPTIONS[7] + DESCRIPTIONS[9] + DESCRIPTIONS[3] + amount + DESCRIPTIONS[4] + DESCRIPTIONS[10];
                    break;
                case DEFEND_DEBUFF:
                    description = DESCRIPTIONS[0] + DESCRIPTIONS[6] + amount + DESCRIPTIONS[7] + DESCRIPTIONS[9] + DESCRIPTIONS[3] + amount + DESCRIPTIONS[5] + DESCRIPTIONS[10];
                    break;
                case ESCAPE:
                    description = DESCRIPTIONS[11] + hpLossAmount + DESCRIPTIONS[12];
                    break;
                default:
                    description = DESCRIPTIONS[13];
                    break;
            }
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new CompelledPower(owner, source, amount);
    }
}
