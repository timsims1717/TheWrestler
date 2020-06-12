package wrestler.cards;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.math.MathUtils;
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
import wrestler.powers.TormentPower;

import java.util.Iterator;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;

public abstract class AbstractWrestlerCard extends CustomCard {

    public int grapple;
    public int baseGrapple;
    public boolean upgradedGrapple;
    public boolean isGrappleModified;

    public boolean requiresTargetGrapple;
    public boolean wantsTargetGrapple;
    final static public String NotGrappledMessage = "That enemy is not Grappled.";

    public int loseHP;
    public int baseLoseHP;
    public boolean upgradedLoseHP;
    public boolean isLoseHPModified;

    public AbstractWrestlerCard(final String id,
                                final String img,
                                final int cost,
                                final CardType type,
                                final CardColor color,
                                final CardRarity rarity,
                                final CardTarget target) {
        super(id, languagePack.getCardStrings(id).NAME, img, cost, languagePack.getCardStrings(id).DESCRIPTION, type, color, rarity, target);

        // Set all the things to their default values.
        isCostModified = false;
        isCostModifiedForTurn = false;
        isDamageModified = false;
        isBlockModified = false;
        isMagicNumberModified = false;
        isGrappleModified = false;
        isLoseHPModified = false;
        requiresTargetGrapple = false;
        wantsTargetGrapple = false;
    }

    public void displayUpgrades() { // Display the upgrade - when you click a card to upgrade it
        super.displayUpgrades();
        if (upgradedGrapple) {
            grapple = baseGrapple;
            isGrappleModified = true;
        }
        if (upgradedLoseHP) {
            loseHP = baseLoseHP;
            isLoseHPModified = true;
        }
    }

    public void upgradeGrappleNumber(int amount) {
        baseGrapple += amount;
        grapple = baseGrapple;
        upgradedGrapple = true;
    }

    public void upgradeLoseHPNumber(int amount) {
        baseLoseHP += amount;
        loseHP = baseLoseHP;
        upgradedLoseHP = true;
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

    @Override
    public void applyPowers() {
        super.applyPowers();
        if (AbstractDungeon.player.hasPower(TormentPower.POWER_ID)) {
            int amount = AbstractDungeon.player.getPower(TormentPower.POWER_ID).amount;
            isLoseHPModified = false;
            float tmp = (float) baseLoseHP;

            tmp += amount;

            if (baseLoseHP != MathUtils.floor(tmp)) {
                isLoseHPModified = true;
            }

            if (tmp < 0.0F) {
                tmp = 0.0F;
            }

            loseHP = MathUtils.floor(tmp);
        }
    }

    public boolean isTargetGrappled(AbstractMonster m) {
        return m != null && !m.isDeadOrEscaped() && m.hasPower(GrapplePower.POWER_ID);
    }

    public void tempStrDown(AbstractPlayer p, AbstractMonster m, int value) {
        addToBot(new ApplyPowerAction(m, p, new StrengthPower(m, -value), -value));
        addToBot(new ApplyPowerAction(m, p, new GainStrengthPower(m, value), value));
    }

    public void devoid(int amount) {
        addToTop(new MakeTempCardInDrawPileAction(new VoidCard(), amount, true, true));
    }

    public void devoid() {
        devoid(1);
    }
}