package wrestler.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import wrestler.powers.GrapplePower;

import java.util.Iterator;

public abstract class AbstractWrestlerCard extends CustomCard {

    public int grapple;
    public int baseGrapple;
    public boolean upgradedGrapple;
    public boolean isGrappleModified;

    public boolean requiresTargetGrapple;
    public boolean wantsTargetGrapple;
    final static public String NotGrappledMessage = "That enemy is not Grappled.";

    public AbstractWrestlerCard(final String id,
                                final String name,
                                final String img,
                                final int cost,
                                final String rawDescription,
                                final CardType type,
                                final CardColor color,
                                final CardRarity rarity,
                                final CardTarget target) {

        super(id, name, img, cost, rawDescription, type, color, rarity, target);

        // Set all the things to their default values.
        isCostModified = false;
        isCostModifiedForTurn = false;
        isDamageModified = false;
        isBlockModified = false;
        isMagicNumberModified = false;
        isGrappleModified = false;
        requiresTargetGrapple = false;
        wantsTargetGrapple = false;
    }

    public void displayUpgrades() { // Display the upgrade - when you click a card to upgrade it
        super.displayUpgrades();
        if (upgradedGrapple) {
            grapple = baseGrapple;
            isGrappleModified = true;
        }
    }

    public void upgradeGrappleNumber(int amount) {
        baseGrapple += amount;
        grapple = baseGrapple;
        upgradedGrapple = true;
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        boolean can = super.canUse(p, m);
        if (!can) {
            return false;
        }
        if (requiresTargetGrapple) {
            if (m != null) {
                if (!isTargetGrappled(m)) {
                    cantUseMessage = NotGrappledMessage;
                    return false;
                }
            } else {
                Iterator var1 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();
                while (var1.hasNext()) {
                    AbstractMonster mon = (AbstractMonster) var1.next();
                    if (isTargetGrappled(mon)) {
                        return true;
                    }
                }
                return false;
            }
        }
        return true;
    }

    @Override
    public void triggerOnGlowCheck() {
        if (requiresTargetGrapple || wantsTargetGrapple) {
            glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
            Iterator var1 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();

            while (var1.hasNext()) {
                AbstractMonster m = (AbstractMonster) var1.next();
                if (isTargetGrappled(m)) {
                    glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
                    break;
                }
            }
        } else {
            super.triggerOnGlowCheck();
        }
    }

    public boolean isTargetGrappled(AbstractMonster m) {
        return m != null && !m.isDeadOrEscaped() && m.hasPower(GrapplePower.POWER_ID);
    }

    public void tempStrDown(AbstractPlayer p, AbstractMonster m, int value) {
        addToBot(new ApplyPowerAction(m, p, new StrengthPower(m, -value), -value));
        addToBot(new ApplyPowerAction(m, p, new GainStrengthPower(m, value), value));
    }

    public void devoid() {
        addToTop(new MakeTempCardInDrawPileAction(new VoidCard(), 1, true, true));
    }
}