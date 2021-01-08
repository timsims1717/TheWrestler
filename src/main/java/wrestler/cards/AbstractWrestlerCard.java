package wrestler.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import wrestler.powers.AgilityTrainingPower;
import wrestler.powers.GrapplePower;
import wrestler.powers.WeightTrainingPower;

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

    public int psychic;
    public int basePsychic;
    public boolean upgradedPsychic;
    public boolean isPsychicModified;
    public int[] multiPsychicDamage;

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
        isPsychicModified = false;
        requiresTargetGrapple = false;
        wantsTargetGrapple = false;
    }

    public void displayUpgrades() {
        super.displayUpgrades();
        if (upgradedGrapple) {
            grapple = baseGrapple;
            isGrappleModified = true;
        }
        if (upgradedPsychic) {
            psychic = basePsychic;
            isPsychicModified = true;
        }
    }

    public void upgradeGrappleNumber(int amount) {
        baseGrapple += amount;
        grapple = baseGrapple;
        upgradedGrapple = true;
    }

    public void upgradePsychicDamageNumber(int amount) {
        basePsychic += amount;
        psychic = basePsychic;
        upgradedPsychic = true;
    }

    @Override
    public void applyPowers() {
        AbstractPlayer p = AbstractDungeon.player;
        grapple = baseGrapple;
        isGrappleModified = false;
        if (p.hasPower(WeightTrainingPower.POWER_ID) && p.hasPower(StrengthPower.POWER_ID)) {
            grapple += p.getPower(StrengthPower.POWER_ID).amount;
            isGrappleModified = true;
        }
        if (p.hasPower(AgilityTrainingPower.POWER_ID) && p.hasPower(DexterityPower.POWER_ID)) {
            grapple += p.getPower(DexterityPower.POWER_ID).amount;
            isGrappleModified = true;
        }
        super.applyPowers();
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
                for (AbstractMonster mon : AbstractDungeon.getCurrRoom().monsters.monsters) {
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

            for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (isTargetGrappled(m)) {
                    glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
                    break;
                }
            }
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
        addToTop(new MakeTempCardInDiscardAction(new VoidCard(), amount));
    }

    public void devoid() {
        devoid(1);
    }
}