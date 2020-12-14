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
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDrawPileEffect;

import java.util.ArrayList;
import java.util.Iterator;

public class OutOfNothingAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final int energyOnUse;
    private final boolean freeToPlayOnce;
    private int effect;
    private AbstractCard cardToDupe;

    private static final String[] TEXT = {
        "Copy"
    };

    public OutOfNothingAction(AbstractPlayer p, boolean freeToPlayOnce, int energyOnUse, boolean upgraded) {
        duration = Settings.ACTION_DUR_FAST;
        player = p;
        this.energyOnUse = energyOnUse;
        this.freeToPlayOnce = freeToPlayOnce;

        effect = EnergyPanel.totalCount;
        if (energyOnUse != -1) {
            effect = energyOnUse;
        }

        if (upgraded) {
            effect += 1;
        }

        if (player.hasRelic("Chemical X")) {
            effect += 2;
            player.getRelic("Chemical X").flash();
        }
    }

    public void update() {
        if (effect > 0) {
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
                tickDuration();
            } else if (cardToDupe != null) {
                AbstractCard c;
                int i;
                if (effect < 6) {
                    for(i = 0; i < effect; i++) {
                        c = cardToDupe.makeStatEquivalentCopy();
                        if (c.type != AbstractCard.CardType.CURSE && c.type != AbstractCard.CardType.STATUS && AbstractDungeon.player.hasPower("MasterRealityPower")) {
                            c.upgrade();
                        }

                        AbstractDungeon.effectList.add(new ShowCardAndAddToDrawPileEffect(c, true, false));
                    }
                } else {
                    for(i = 0; i < effect; i++) {
                        c = cardToDupe.makeStatEquivalentCopy();
                        if (c.type != AbstractCard.CardType.CURSE && c.type != AbstractCard.CardType.STATUS && AbstractDungeon.player.hasPower("MasterRealityPower")) {
                            c.upgrade();
                        }

                        AbstractDungeon.effectList.add(new ShowCardAndAddToDrawPileEffect(c, true, false));
                    }
                }
                player.hand.addToBottom(cardToDupe);
                if (!freeToPlayOnce) {
                    player.energy.use(EnergyPanel.totalCount);
                }
                isDone = true;
            }
            tickDuration();
        }
    }
}
