package wrestler.variables;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import wrestler.cards.AbstractWrestlerCard;

import static wrestler.Wrestler.makeID;

public class PsychicDamage extends DynamicVariable {
    @Override
    public String key() {
        return makeID("Psychic");
    }

    @Override
    public boolean isModified(AbstractCard card) {
        return ((AbstractWrestlerCard) card).isPsychicModified;
    }

    @Override
    public int value(AbstractCard card) {
        return ((AbstractWrestlerCard) card).psychic;
    }

    @Override
    public int baseValue(AbstractCard card) {
        return ((AbstractWrestlerCard) card).basePsychic;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        return ((AbstractWrestlerCard) card).upgradedPsychic;
    }
}