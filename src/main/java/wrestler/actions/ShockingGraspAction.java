package wrestler.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;

public class ShockingGraspAction extends AbstractGameAction {

    public ShockingGraspAction(AbstractCreature source, AbstractCreature target, int amount) {
        setValues(target, source, amount);
        actionType = AbstractGameAction.ActionType.DAMAGE;
        duration = Settings.ACTION_DUR_FAST;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            addToTop(new SFXAction("ORB_LIGHTNING_EVOKE", 0.1F));
            addToBot(new VFXAction(new LightningEffect(target.drawX, target.drawY), 0.05F));
            target.damage(new DamageInfo(source, amount, DamageInfo.DamageType.NORMAL));
        }
        isDone = true;
    }
}
