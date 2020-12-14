package wrestler.variables;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import wrestler.cards.AbstractWrestlerCard;

import static wrestler.Wrestler.makeID;

public class Grapple extends DynamicVariable {
    @Override
    public String key() {
        return makeID("Grapple");
    }

    @Override
    public boolean isModified(AbstractCard card) {
        return ((AbstractWrestlerCard) card).isGrappleModified;
    }

    @Override
    public int value(AbstractCard card) {
        return ((AbstractWrestlerCard) card).grapple;
    }

    @Override
    public int baseValue(AbstractCard card) {
        return ((AbstractWrestlerCard) card).baseGrapple;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        return ((AbstractWrestlerCard) card).upgradedGrapple;
    }
}