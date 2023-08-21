package wrestler.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlyingOrbEffect;

public class VitalityDrainAction extends AbstractGameAction {
    public VitalityDrainAction(AbstractCreature source, AbstractCreature target) {
        setValues(target, source);
        actionType = ActionType.DAMAGE;
        duration = Settings.ACTION_DUR_FAST;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            int dmg = target.lastDamageTaken;
            addToTop(new HealAction(source, source, dmg));
            addToTop(new WaitAction(0.1F));
            for (int j = 0; j < dmg / 2 && j < 10; ++j) {
                addToBot(new VFXAction(new FlyingOrbEffect(target.hb.cX, target.hb.cY)));
            }
        }

        if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
            AbstractDungeon.actionManager.clearPostCombatActions();
        }
        isDone = true;
    }
}
