package wrestler.actions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import wrestler.powers.GrapplePower;

public class BearHugAction extends AbstractGameAction {

    public BearHugAction(AbstractCreature source, int amount) {
        this.source = source;
        this.amount = amount;
        this.actionType = ActionType.BLOCK;
    }

    public void update() {
        for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!m.isDeadOrEscaped() && m.hasPower(GrapplePower.POWER_ID)) {
                addToBot(new GainBlockAction(source, source, amount));
            }
        }
        isDone = true;
    }
}
