package wrestler.variables;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import wrestler.cards.AbstractWrestlerCard;

import static wrestler.Wrestler.makeID;

public class LoseHP extends DynamicVariable {
    @Override
    public String key() {
        return makeID("LoseHP");
    }

    @Override
    public boolean isModified(AbstractCard card) {
        return ((AbstractWrestlerCard) card).isLoseHPModified;
    }

    @Override
    public int value(AbstractCard card) {
        return ((AbstractWrestlerCard) card).loseHP;
    }

    @Override
    public int baseValue(AbstractCard card) {
        return ((AbstractWrestlerCard) card).baseLoseHP;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        return ((AbstractWrestlerCard) card).upgradedLoseHP;
    }
}