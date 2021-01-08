package wrestler.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;

import static wrestler.patches.PsychicDamagePatch.PSYCHIC_DAMAGE;
import static wrestler.patches.PsychicDamagePatch.PSYCHIC_EFFECT;

public class PsychicDamageAction extends AbstractGameAction {
    private static final float DURATION = 0.33F;

    public PsychicDamageAction(AbstractCreature target, AbstractCreature source, int amount) {
        this.setValues(target, source, amount);
        this.actionType = ActionType.DAMAGE;
        this.attackEffect = PSYCHIC_EFFECT;
        this.duration = 0.33F;
    }

    public void update() {
        if (duration == 0.33F && target.currentHealth > 0) {
            tickDuration();
            addToTop(new DamageAction(target, new DamageInfo(source, amount, PSYCHIC_DAMAGE), attackEffect));

            if (!Settings.FAST_MODE) {
                addToTop(new WaitAction(0.1F));
            }
        }
        isDone = true;
    }
}
