package wrestler.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.ShowCardAndPoofAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

import java.util.Iterator;

public class IntoNothingAction extends AbstractGameAction {

    private final AbstractPlayer player;
    private AbstractCard theCard = null;
    private DamageInfo info;
    private boolean remove = false;

    private static final String[] TEXT = {
            "Choose a Card to Remove."
    };

    public IntoNothingAction(AbstractCreature target, DamageInfo info) {
        this.info = info;
        setValues(target, info);
        actionType = ActionType.CARD_MANIPULATION;
        duration = startDuration = Settings.ACTION_DUR_FAST;
        player = AbstractDungeon.player;
        actionType = ActionType.DAMAGE;
    }

    public void update() {
        if (duration == startDuration && target != null) {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(target.hb.cX, target.hb.cY, AttackEffect.NONE));
            target.damage(info);
            if ((((AbstractMonster)target).isDying || target.currentHealth <= 0) && !target.halfDead && !target.hasPower("Minion")) {
                remove = true;
            } else {
                isDone = true;
            }

            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
        }

        if (remove && theCard == null) {
            chooseCard();
        }

        tickDuration();

        if (isDone && remove && theCard != null) {
            AbstractCard effectCard = theCard.makeStatEquivalentCopy();
            AbstractDungeon.topLevelEffectsQueue.add(new ExhaustCardEffect(effectCard));
            removeCard();
            addToTop(new WaitAction(Settings.ACTION_DUR_MED));
        }
    }

    public void chooseCard() {
        if (duration == startDuration) {
            if (!player.masterDeck.isEmpty()) {
                CardGroup temp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                Iterator var6 = player.masterDeck.group.iterator();

                while(var6.hasNext()) {
                    AbstractCard c = (AbstractCard)var6.next();
                    temp.addToTop(c);
                }
                temp.sortAlphabetically(true);
                temp.sortByRarityPlusStatusCardType(false);
                AbstractDungeon.gridSelectScreen.open(temp, 1, TEXT[0], false);

                this.tickDuration();
            } else {
                isDone = true;
            }
        } else {
            if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                Iterator var1 = AbstractDungeon.gridSelectScreen.selectedCards.iterator();

                AbstractCard c;
                while (var1.hasNext()) {
                    c = (AbstractCard)var1.next();
                    theCard = c;
                    c.unhover();
                    isDone = true;
                }

                AbstractDungeon.gridSelectScreen.selectedCards.clear();
            }
        }
    }

    public void removeCard() {
        player.masterDeck.removeCard(theCard);
        if (player.hand.group.contains(theCard)) {
            player.hand.moveToExhaustPile(theCard);
            AbstractDungeon.player.hand.refreshHandLayout();
        } else if (player.drawPile.contains(theCard)) {
            player.drawPile.moveToExhaustPile(theCard);
        } else if (player.discardPile.contains(theCard)) {
            player.discardPile.moveToExhaustPile(theCard);
        }
        player.exhaustPile.removeCard(theCard);
    }
}
