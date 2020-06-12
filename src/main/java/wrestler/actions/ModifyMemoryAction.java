package wrestler.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.Iterator;

public class ModifyMemoryAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final int numberOfCards;
    private int numberOfChosen;
    private boolean secondOpened = false;
    private boolean first = true;

    private static final String[] TEXT = {
        "Choose up to one Card to Discard.",
        "Choose up to ",
        " Cards to Discard.",
        "Choose a Card to Shuffle into Your Deck.",
        "Choose ",
        " Cards to to Shuffle into Your Deck."
    };

    public ModifyMemoryAction(int numberOfCards) {
        actionType = ActionType.CARD_MANIPULATION;
        duration = this.startDuration = Settings.ACTION_DUR_FAST;
        player = AbstractDungeon.player;
        this.numberOfCards = numberOfCards;
    }

    @SuppressWarnings("Duplicates")
    public void update() {
        if (first) {
            if (duration == startDuration) {
                if (!player.drawPile.isEmpty() && numberOfCards > 0) {
                    CardGroup temp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                    Iterator var6 = this.player.drawPile.group.iterator();

                    while (var6.hasNext()) {
                        AbstractCard c = (AbstractCard) var6.next();
                        temp.addToTop(c);
                    }
                    temp.sortAlphabetically(true);
                    temp.sortByRarityPlusStatusCardType(false);
                    if (numberOfCards == 1) {
                        AbstractDungeon.gridSelectScreen.open(temp, numberOfCards, true, TEXT[0]);
                    } else {
                        AbstractDungeon.gridSelectScreen.open(temp, numberOfCards, true, TEXT[1] + numberOfCards + TEXT[2]);
                    }

                    tickDuration();
                } else {
                    isDone = true;
                }
            } else {
                if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                    Iterator var1 = AbstractDungeon.gridSelectScreen.selectedCards.iterator();

                    AbstractCard c;
                    while (var1.hasNext()) {
                        c = (AbstractCard) var1.next();
                        player.drawPile.moveToDiscardPile(c);
                        c.unhover();
                        numberOfChosen++;
                    }

                    AbstractDungeon.gridSelectScreen.selectedCards.clear();
                    AbstractDungeon.player.hand.refreshHandLayout();
                    first = false;
                }

                tickDuration();
            }
        } else {
            if (!secondOpened) {
                if (!player.discardPile.isEmpty() && numberOfChosen > 0) {
                    if (player.discardPile.size() <= numberOfChosen) {
                        ArrayList<AbstractCard> cardsToMove = new ArrayList();
                        Iterator var5 = player.discardPile.group.iterator();

                        AbstractCard c;
                        while (var5.hasNext()) {
                            c = (AbstractCard) var5.next();
                            cardsToMove.add(c);
                        }

                        var5 = cardsToMove.iterator();

                        while (var5.hasNext()) {
                            c = (AbstractCard) var5.next();
                            player.discardPile.moveToDeck(c, true);
                        }

                        isDone = true;
                    } else {
                        CardGroup temp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                        Iterator var6 = this.player.discardPile.group.iterator();

                        while (var6.hasNext()) {
                            AbstractCard c = (AbstractCard) var6.next();
                            temp.addToTop(c);
                        }
                        temp.sortAlphabetically(true);
                        temp.sortByRarityPlusStatusCardType(false);
                        if (numberOfChosen == 1) {
                            AbstractDungeon.gridSelectScreen.open(temp, numberOfChosen, TEXT[3], false);
                        } else {
                            AbstractDungeon.gridSelectScreen.open(temp, numberOfChosen, TEXT[4] + numberOfChosen + TEXT[5], false);
                        }

                        tickDuration();
                        secondOpened = true;
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
                        player.discardPile.moveToDeck(c, true);
                        c.unhover();
                    }

                    AbstractDungeon.gridSelectScreen.selectedCards.clear();
                    AbstractDungeon.player.hand.refreshHandLayout();
                    isDone = true;
                }
                tickDuration();
            }
        }
    }
}
