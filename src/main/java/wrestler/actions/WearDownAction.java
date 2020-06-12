package wrestler.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import wrestler.cards.indigo.CheapShot_Wrestler;
import wrestler.powers.GrapplePower;

import java.util.Iterator;

public class WearDownAction extends AbstractGameAction {
    private static final float PERC = 0.1F;

    public WearDownAction() {
        attackEffect = AttackEffect.FIRE;
    }

    public void update() {
        if (duration == startDuration) {
            Iterator var1 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();

            while (var1.hasNext()) {
                AbstractMonster m = (AbstractMonster) var1.next();
                if (!m.isDeadOrEscaped() && m.hasPower(GrapplePower.POWER_ID)) {

                    if (m.currentHealth > 0) {
                        AbstractDungeon.effectList.add(new FlashAtkImgEffect(m.hb.cX, m.hb.cY, attackEffect));
                    }

                    m.damage(new DamageInfo(source, (int) Math.ceil(PERC * m.currentHealth), DamageInfo.DamageType.HP_LOSS));
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
