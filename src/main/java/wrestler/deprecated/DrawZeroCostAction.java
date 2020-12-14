package wrestler.deprecated;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.SoulGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class DrawZeroCostAction extends AbstractGameAction {
    private boolean shuffleCheck;
    public static ArrayList<AbstractCard> drawnCards = new ArrayList();
    private boolean clearDrawHistory;

    public DrawZeroCostAction(int amount) {
        shuffleCheck = false;
        clearDrawHistory = true;
        this.amount = amount;
        actionType = ActionType.DRAW;
        if (Settings.FAST_MODE) {
            this.duration = Settings.ACTION_DUR_XFAST;
        } else {
            this.duration = Settings.ACTION_DUR_FASTER;
        }
    }

    public DrawZeroCostAction(int amount, boolean clearDrawHistory) {
        this(amount);
        this.clearDrawHistory = clearDrawHistory;
    }

    public void update() {
        if (AbstractDungeon.player.hasPower("No Draw")) {
            AbstractDungeon.player.getPower("No Draw").flash();
            isDone = true;
        } else if (amount <= 0) {
            isDone = true;
        } else {
            int deckSize = AbstractDungeon.player.drawPile.size();
            int discardSize = AbstractDungeon.player.discardPile.size();
            if (!SoulGroup.isActive()) {
                if (deckSize + discardSize == 0) {
                    isDone = true;
                } else if (AbstractDungeon.player.hand.size() == 10) {
                    AbstractDungeon.player.createHandIsFullDialog();
                    isDone = true;
                } else {
                    if (!shuffleCheck) {
                        int tmp;
                        if (amount + AbstractDungeon.player.hand.size() > 10) {
                            tmp = 10 - (amount + AbstractDungeon.player.hand.size());
                            amount += tmp;
                            AbstractDungeon.player.createHandIsFullDialog();
                        }

                        if (amount > deckSize) {
                            tmp = amount - deckSize;
                            addToTop(new DrawZeroCostAction(tmp, false));
                            addToTop(new EmptyDeckShuffleAction());
                            if (deckSize != 0) {
                                addToTop(new DrawZeroCostAction(deckSize, false));
                            }

                            amount = 0;
                            isDone = true;
                            return;
                        }

                        shuffleCheck = true;
                    }

                    duration -= Gdx.graphics.getDeltaTime();
                    if (amount != 0 && duration < 0.0F) {
                        if (Settings.FAST_MODE) {
                            duration = Settings.ACTION_DUR_XFAST;
                        } else {
                            duration = Settings.ACTION_DUR_FASTER;
                        }

                        --amount;
                        if (!AbstractDungeon.player.drawPile.isEmpty()) {
                            AbstractCard c = AbstractDungeon.player.drawPile.getTopCard();
                            if (c.cost > 0) {
                                c.freeToPlayOnce = true;
                            }
                            drawnCards.add(c);
                            AbstractDungeon.player.draw();
                            AbstractDungeon.player.hand.refreshHandLayout();
                        } else {
                            isDone = true;
                        }

                        if (amount == 0) {
                            isDone = true;
                        }
                    }
                }
            }
        }
    }
}
