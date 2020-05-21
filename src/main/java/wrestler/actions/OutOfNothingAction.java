package wrestler.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.NightmarePower;

import java.util.ArrayList;
import java.util.Iterator;

public class OutOfNothingAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private AbstractCard cardToDupe;

    private static final String[] TEXT = {
        "Copy"
    };

    public OutOfNothingAction() {
        actionType = ActionType.CARD_MANIPULATION;
        duration = startDuration = Settings.ACTION_DUR_FAST;
        player = AbstractDungeon.player;
    }

    public void update() {
        if (duration == Settings.ACTION_DUR_FAST) {
            if (player.hand.isEmpty() || player.drawPile.isEmpty()) {
                isDone = true;
            } else if (player.hand.size() == 1) {
                cardToDupe = player.hand.getBottomCard();
            } else {
                AbstractDungeon.handCardSelectScreen.open(TEXT[0], 1, false, false);
            }
        } else if (cardToDupe == null && !AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            cardToDupe = AbstractDungeon.handCardSelectScreen.selectedCards.getBottomCard();
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            this.tickDuration();
        } else if (cardToDupe != null) {
            int total = 0;
            AbstractCard c;
            for (int i = 0; i < player.drawPile.group.size(); i++) {
                c = player.drawPile.group.get(i);
                if (c.cardID.equals(VoidCard.ID)) {
                    player.drawPile.moveToExhaustPile(c);
                    total++;
                }
            }

            addToBot(new MakeTempCardInDrawPileAction(cardToDupe.makeCopy(), total, true, true));

            isDone = true;
        }
        this.tickDuration();
    }
}
