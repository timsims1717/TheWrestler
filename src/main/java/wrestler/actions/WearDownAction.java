package wrestler.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import wrestler.powers.GrapplePower;

import java.util.Iterator;

public class WearDownAction extends AbstractGameAction {
    public WearDownAction() {
        attackEffect = AttackEffect.FIRE;
    }

    public void update() {
        if (duration == startDuration) {
            for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (!m.isDeadOrEscaped() && m.hasPower(GrapplePower.POWER_ID)) {

                    if (m.currentHealth > 0) {
                        AbstractDungeon.effectList.add(new FlashAtkImgEffect(m.hb.cX, m.hb.cY, attackEffect));
                    }

                    m.damage(new DamageInfo(source, m.getPower(GrapplePower.POWER_ID).amount, DamageInfo.DamageType.THORNS));
                    if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                        AbstractDungeon.actionManager.clearPostCombatActions();
                    }

                    if (Settings.FAST_MODE) {
                        addToTop(new WaitAction(0.2F));
                    } else {
                        addToTop(new WaitAction(0.4F));
                    }
                }
            }
        }
        isDone = true;
    }
}
