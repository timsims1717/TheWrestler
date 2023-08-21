package wrestler.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import wrestler.util.TextureLoader;

import static wrestler.Wrestler.makePowerPath;

public class HypnotizedPower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;
    public boolean shouldRemove;

    public static final String POWER_ID = wrestler.Wrestler.makeID(HypnotizedPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("HypnotizedPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("HypnotizedPower32.png"));

    public HypnotizedPower(final AbstractCreature owner, final AbstractCreature source, final int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        this.source = source;
        shouldRemove = false;

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
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        AbstractPlayer p = AbstractDungeon.player;
        if (source.equals(owner) && !source.equals(p)) {
            if (power.type == PowerType.BUFF && target.equals(owner)) {
                addToBot(new ApplyPowerAction(p, owner, power));
                shouldRemove = true;
            } else if (power.type == PowerType.DEBUFF && target.equals(p)) {
                addToBot(new ApplyPowerAction(owner, owner, power));
                shouldRemove = true;
            }
        }
    }

    @Override
    public void atEndOfTurn(final boolean isPlayer) {
        if (owner instanceof AbstractMonster) {
            AbstractMonster m = (AbstractMonster) owner;
            if (m.intent == AbstractMonster.Intent.ESCAPE) {
                escape();
            }
        }
        updateDescription();
    }

    private void attack() {
        if (owner instanceof AbstractMonster) {
            int damage = ((AbstractMonster) owner).getIntentDmg();
            if (damage > 0 && amount > 0) {
                addToTop(new DamageAction(owner, new DamageInfo(owner, Math.max(Math.round(damage * amount / 10.0f), 1), DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
            }
        }
        shouldRemove = true;
    }

    private void escape() {
        addToBot(new DamageAction(owner, new DamageInfo(owner, amount, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        shouldRemove = true;
    }

    @Override
    public void updateDescription() {
        if (owner instanceof AbstractMonster) {
            AbstractMonster m = (AbstractMonster) owner;
            if (m.intent == AbstractMonster.Intent.ESCAPE) {
                description = DESCRIPTIONS[2] + amount + DESCRIPTIONS[3];
            } else {
                description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
            }
        }
    }

    @Override
    public void atEndOfRound() {
        if (shouldRemove) {
            addToTop(new RemoveSpecificPowerAction(owner, owner, POWER_ID));
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new HypnotizedPower(owner, source, amount);
    }
}
