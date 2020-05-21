package wrestler.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.Iterator;

public class DrawPileToDiscardAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final int numberOfCards;
    private final boolean optional;

    private static final String[] TEXT = {
        "Choose a Card to Discard.",
        "Choose ",
        " Cards to Discard."
    };

    public DrawPileToDiscardAction(int numberOfCards, boolean optional) {
        actionType = ActionType.CARD_MANIPULATION;
        duration = this.startDuration = Settings.ACTION_DUR_FAST;
        player = AbstractDungeon.player;
        this.numberOfCards = numberOfCards;
        this.optional = optional;
    }

    public DrawPileToDiscardAction(int numberOfCards) {
        this(numberOfCards, false);
    }

    public void update() {
        if (duration == startDuration) {
            if (!player.drawPile.isEmpty() && numberOfCards > 0) {
                if (player.drawPile.size() <= numberOfCards && !optional) {
                    ArrayList<AbstractCard> cardsToMove = new ArrayList();
                    Iterator var5 = player.drawPile.group.iterator();

                    AbstractCard c;
                    while(var5.hasNext()) {
                        c = (AbstractCard)var5.next();
                        cardsToMove.add(c);
                    }

                    var5 = cardsToMove.iterator();

                    while(var5.hasNext()) {
                        c = (AbstractCard)var5.next();
                        player.drawPile.moveToDiscardPile(c);
                    }

                    isDone = true;
                } else {
                    CardGroup temp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                    Iterator var6 = this.player.drawPile.group.iterator();

                    while(var6.hasNext()) {
                        AbstractCard c = (AbstractCard)var6.next();
                        temp.addToTop(c);
                    }
                    temp.sortAlphabetically(true);
                    temp.sortByRarityPlusStatusCardType(false);
                    if (numberOfCards == 1) {
                        if (optional) {
                            AbstractDungeon.gridSelectScreen.open(temp, numberOfCards, true, TEXT[0]);
                        } else {
                            AbstractDungeon.gridSelectScreen.open(temp, numberOfCards, TEXT[0], false);
                        }
                    } else if (optional) {
                        AbstractDungeon.gridSelectScreen.open(temp, numberOfCards, true, TEXT[1] + numberOfCards + TEXT[2]);
                    } else {
                        AbstractDungeon.gridSelectScreen.open(temp, numberOfCards, TEXT[1] + numberOfCards + TEXT[2], false);
                    }

                    this.tickDuration();
                }
            } else {
                isDone = true;
            }
        } else {
            if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                Iterator var1 = AbstractDungeon.gridSelectScreen.selectedCards.iterator();

                AbstractCard c;
                while (var1.hasNext()) {
                    c = (AbstractCard)var1.next();
                    player.drawPile.moveToDiscardPile(c);
                    c.unhover();
                }

                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                AbstractDungeon.player.hand.refreshHandLayout();
            }

            this.tickDuration();
        }
    }
}
