package wrestler.variables;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import wrestler.cards.AbstractWrestlerCard;

import static wrestler.Wrestler.makeID;

public class Horror extends DynamicVariable {
    @Override
    public String key() {
        return makeID("Horror");
    }

    @Override
    public boolean isModified(AbstractCard card) {
        return ((AbstractWrestlerCard) card).isHorrorModified;
    }

    @Override
    public int value(AbstractCard card) {
        return ((AbstractWrestlerCard) card).horror;
    }

    @Override
    public int baseValue(AbstractCard card) {
        return ((AbstractWrestlerCard) card).baseHorror;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        return ((AbstractWrestlerCard) card).upgradedHorror;
    }
}