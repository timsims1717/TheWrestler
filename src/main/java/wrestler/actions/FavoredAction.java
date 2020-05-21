package wrestler.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import wrestler.powers.FavoredPower;
import wrestler.util.BoonCreator;

public class FavoredAction extends AbstractGameAction {

    private final AbstractPlayer player;
    private final int energyOnUse;
    private final boolean freeToPlayOnce;

    public FavoredAction(AbstractPlayer p, boolean freeToPlayOnce, int energyOnUse) {
        player = p;
        this.energyOnUse = energyOnUse;
        this.freeToPlayOnce = freeToPlayOnce;
    }

    public void update() {
        int effect = EnergyPanel.totalCount;
        if (energyOnUse != -1) {
            effect = energyOnUse;
        }

        if (player.hasRelic("Chemical X")) {
            effect += 2;
            player.getRelic("Chemical X").flash();
        }

        if (effect > 0) {
            addToBot(new ApplyPowerAction(player, player, new FavoredPower(player, player, effect), effect));
            if (!freeToPlayOnce) {
                player.energy.use(EnergyPanel.totalCount);
            }
        }
        isDone = true;
    }
}
