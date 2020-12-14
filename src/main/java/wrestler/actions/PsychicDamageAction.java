package wrestler.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class PsychicDamageAction extends AbstractGameAction {
    private static final float DURATION = 0.33F;

    public PsychicDamageAction(AbstractCreature target, AbstractCreature source, int amount) {
        this.setValues(target, source, amount);
        this.actionType = ActionType.DAMAGE;
        this.attackEffect = AttackEffect.FIRE;
        this.duration = 0.33F;
    }

    @SuppressWarnings("Duplicates")
    public void update() {
        if (target.hasPower(ArtifactPower.POWER_ID)) {
            addToTop(new ApplyPowerAction(target, source, new WeakPower(target, 1, false)));
        }
        if (duration == 0.33F && target.currentHealth > 0) {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(target.hb.cX, target.hb.cY, attackEffect));
            tickDuration();
            target.damage(new DamageInfo(source, amount, DamageInfo.DamageType.HP_LOSS));
            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }

            if (!Settings.FAST_MODE) {
                addToTop(new WaitAction(0.1F));
            }
        }
    }
}
