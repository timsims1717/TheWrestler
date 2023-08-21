package wrestler.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import wrestler.cards.indigo.CheapShot;

import java.util.Iterator;

public class CheapShotAction extends AbstractGameAction {
    private AbstractCard card;
    private boolean upgraded;

    public CheapShotAction(AbstractCard card, int amount, boolean upgraded) {
        this.card = card;
        this.amount = amount;
        this.upgraded = upgraded;
    }

    public void update() {
        card.baseDamage -= amount;
        card.applyPowers();
        if (!upgraded) {
            Iterator var1 = AbstractDungeon.player.discardPile.group.iterator();

            AbstractCard c;
            while (var1.hasNext()) {
                c = (AbstractCard) var1.next();
                if (c instanceof CheapShot) {
                    c.baseDamage -= this.amount;
                    c.applyPowers();
                }
            }

            var1 = AbstractDungeon.player.drawPile.group.iterator();

            while (var1.hasNext()) {
                c = (AbstractCard) var1.next();
                if (c instanceof CheapShot) {
                    c.baseDamage -= this.amount;
                    c.applyPowers();
                }
            }

            var1 = AbstractDungeon.player.hand.group.iterator();

            while (var1.hasNext()) {
                c = (AbstractCard) var1.next();
                if (c instanceof CheapShot) {
                    c.baseDamage -= this.amount;
                    c.applyPowers();
                }
            }
        }

        isDone = true;
    }
}
