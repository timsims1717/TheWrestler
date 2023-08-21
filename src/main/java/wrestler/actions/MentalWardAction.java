package wrestler.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import wrestler.powers.MentalWardPower;

import static wrestler.patches.PsychicDamagePatch.PSYCHIC_EFFECT;

public class MentalWardAction extends AbstractGameAction {

    public MentalWardAction(AbstractCreature source, int amount) {
        this.source = source;
        this.amount = amount;
        actionType = ActionType.DAMAGE;
    }

    public void update() {
        AbstractPlayer p = AbstractDungeon.player;
        if (p.hasPower(MentalWardPower.POWER_ID)) {
            addToTop(new DamageAction(source, new DamageInfo(p, amount, DamageInfo.DamageType.HP_LOSS), PSYCHIC_EFFECT));
            p.getPower(MentalWardPower.POWER_ID).flash();
        }
        isDone = true;
    }
}
