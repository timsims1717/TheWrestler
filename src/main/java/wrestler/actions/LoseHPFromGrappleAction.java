package wrestler.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import wrestler.powers.GrapplePower;

import java.util.Iterator;

public class LoseHPFromGrappleAction extends AbstractGameAction {
    AbstractCard card;
    int multiplier;
    AbstractMonster target;

    public LoseHPFromGrappleAction(AbstractCard callingCard, AbstractMonster m, int multiplier) {
        card = callingCard;
        this.multiplier = multiplier;
        target = m;
    }

    public void update() {
        if (target == null) {
            Iterator var1 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();

            while (var1.hasNext()) {
                AbstractMonster m = (AbstractMonster) var1.next();
                if (m != null && !m.isDeadOrEscaped() && m.hasPower(GrapplePower.POWER_ID)) {
                    GrapplePower g = (GrapplePower) m.getPower(GrapplePower.POWER_ID);
                    g.triggerLoseHP(multiplier);
                }
            }
        } else {
            if (!target.isDeadOrEscaped() && target.hasPower(GrapplePower.POWER_ID)) {
                GrapplePower g = (GrapplePower) target.getPower(GrapplePower.POWER_ID);
                g.triggerLoseHP(multiplier);
            }
        }

        this.isDone = true;
    }
}
