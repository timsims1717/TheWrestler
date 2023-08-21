package wrestler.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import wrestler.powers.GrapplePower;
import wrestler.powers.SubmissionPower;

import java.util.Iterator;

public class WearDownAction extends AbstractGameAction {
    public WearDownAction(AbstractCreature source, int amount) {
        this.amount = amount;
        this.source = source;
    }

    public void update() {
        if (duration == startDuration) {
            for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (!m.isDeadOrEscaped() && m.hasPower(GrapplePower.POWER_ID)) {
                    addToBot(new ApplyPowerAction(m, source, new SubmissionPower(m, source, amount), amount));
                }
            }
        }
        isDone = true;
    }
}
