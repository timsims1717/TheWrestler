package wrestler.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import wrestler.powers.GrapplePower;

import java.util.Iterator;

public class PounceAction extends AbstractGameAction {
    private DamageInfo info;

    public PounceAction(AbstractCreature target, DamageInfo info) {
        this.info = info;
        setValues(target, info);
        actionType = ActionType.DAMAGE;
        duration = startDuration = Settings.ACTION_DUR_FAST;
    }

    public void update() {
        if (duration == startDuration && target != null) {

            AbstractDungeon.effectList.add(new FlashAtkImgEffect(target.hb.cX, target.hb.cY, AttackEffect.BLUNT_HEAVY));
            target.damage(info);
            if (!((AbstractMonster)target).isDying && target.currentHealth > 0 && !target.halfDead) {
                AbstractPlayer player = AbstractDungeon.player;
                int grappleMove = 0;
                Iterator var1 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();
                while (var1.hasNext()) {
                    AbstractMonster mon = (AbstractMonster) var1.next();
                    if (!mon.isDeadOrEscaped() && mon.hasPower(GrapplePower.POWER_ID) && !mon.equals(target)) {
                        grappleMove += mon.getPower(GrapplePower.POWER_ID).amount;
                        addToBot(new RemoveSpecificPowerAction(mon, player, GrapplePower.POWER_ID));
                    }
                }
                if (grappleMove > 0) {
                    addToBot(new ApplyPowerAction(target, player, new GrapplePower(target, player, grappleMove), grappleMove));
                }
            }
        }

        isDone = true;
    }
}
