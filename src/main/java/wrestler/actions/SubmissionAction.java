package wrestler.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.InstantKillAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;
import wrestler.powers.GrapplePower;

public class SubmissionAction extends AbstractGameAction {
    private static final int THRESHOLD = 50;

    public SubmissionAction(AbstractCreature target) {
        this.duration = Settings.ACTION_DUR_FAST;
        this.source = null;
        this.target = target;
    }

    public void update() {
        if (duration == Settings.ACTION_DUR_FAST
                && target.hasPower(GrapplePower.POWER_ID)
                && target.getPower(GrapplePower.POWER_ID).amount >= THRESHOLD
                && target instanceof AbstractMonster) {
            addToBot(new VFXAction(new WeightyImpactEffect(target.hb.cX, target.hb.cY)));
            addToBot(new WaitAction(0.8F));
            addToBot(new InstantKillAction(target));
        }

        isDone = true;
    }
}
