package wrestler.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class WindUpAction extends AbstractGameAction {
    public WindUpAction(int amount) {
        this.amount = amount;
    }

    public void update() {
        int cardsPlayed = AbstractDungeon.actionManager.cardsPlayedThisCombat.size();

        if (cardsPlayed >= 2 && ((AbstractCard)AbstractDungeon.actionManager.cardsPlayedThisCombat.get(cardsPlayed - 2)).type == AbstractCard.CardType.ATTACK) {
            addToTop(new GainEnergyAction(amount));
        }

        this.isDone = true;
    }
}
