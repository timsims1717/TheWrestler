package wrestler.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.InstantKillAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import wrestler.powers.GrapplePower;

public class SubmissionAction extends AbstractGameAction {

    public SubmissionAction(AbstractCreature target) {
        this.duration = Settings.ACTION_DUR_FAST;
        this.source = null;
        this.target = target;
    }

    public void update() {
        if (duration == Settings.ACTION_DUR_FAST
                && target.hasPower(GrapplePower.POWER_ID)
                && target.getPower(GrapplePower.POWER_ID).amount >= target.currentHealth
                && target instanceof AbstractMonster) {
            addToBot(new InstantKillAction(target));
        }

        isDone = true;
    }
}
