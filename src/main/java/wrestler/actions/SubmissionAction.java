package wrestler.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.InstantKillAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import wrestler.powers.GrapplePower;

import java.util.Iterator;

public class SubmissionAction extends AbstractGameAction {

    private final int cutoff;

    public SubmissionAction(AbstractCreature target, int cutoff) {
        this.duration = Settings.ACTION_DUR_FAST;
        this.source = null;
        this.target = target;
        this.cutoff = cutoff;
    }

    public void update() {
        if (duration == Settings.ACTION_DUR_FAST
                && target.hasPower(GrapplePower.POWER_ID)
                && target.getPower(GrapplePower.POWER_ID).amount >= this.cutoff
                && target instanceof AbstractMonster) {
            addToBot(new InstantKillAction(target));
        }

        isDone = true;
    }
}
