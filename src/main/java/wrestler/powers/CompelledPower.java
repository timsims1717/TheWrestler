package wrestler.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import wrestler.util.TextureLoader;

import static wrestler.Wrestler.makePowerPath;
import static wrestler.patches.PsychicDamagePatch.PSYCHIC_EFFECT;

public class CompelledPower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;

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
                case ESCAPE:
                    escape();
                case ATTACK:
                case ATTACK_BUFF:
                case ATTACK_DEBUFF:
                case ATTACK_DEFEND:
                    addToTop(new RemoveSpecificPowerAction(owner, owner, POWER_ID));
                    break;
            }
        }
        addToBot(new ReducePowerAction(owner, owner, POWER_ID, 1));
        updateDescription();
    }

    private void attack() {
        if (owner instanceof AbstractMonster) {
            int damage = ((AbstractMonster) owner).getIntentDmg();
            if (damage > 0 && amount > 0) {
                addToTop(new DamageAction(owner, new DamageInfo(owner, Math.max(Math.round(damage * amount / 10.0f), 1), DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
            }
        }
    }

    private void escape() {
        addToBot(new DamageAction(owner, new DamageInfo(owner, 5, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
    }

    @Override
    public void updateDescription() {
        if (owner instanceof AbstractMonster) {
            AbstractMonster m = (AbstractMonster) owner;
            if (m.intent == AbstractMonster.Intent.ESCAPE) {
                description = DESCRIPTIONS[2];
            } else {
                description = DESCRIPTIONS[0] + (amount * 10) + DESCRIPTIONS[1];
            }
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new CompelledPower(owner, source, amount);
    }
}
