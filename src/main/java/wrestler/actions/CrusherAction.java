package wrestler.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import wrestler.powers.FavoredPower;
import wrestler.powers.GrapplePower;

import java.util.Iterator;

public class CrusherAction extends AbstractGameAction {

    private final AbstractPlayer player;
    private final int energyOnUse;
    private final boolean freeToPlayOnce;
    private final boolean upgraded;

    public CrusherAction(AbstractPlayer p, boolean freeToPlayOnce, int energyOnUse, boolean upgraded) {
        player = p;
        this.energyOnUse = energyOnUse;
        this.freeToPlayOnce = freeToPlayOnce;
        this.upgraded = upgraded;
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
            Iterator var1 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();

            while (var1.hasNext()) {
                AbstractMonster mon = (AbstractMonster) var1.next();
                addToBot(new ApplyPowerAction(mon, player, new GrapplePower(mon, player, effect), effect));
                if (upgraded) {
                    addToBot(new ApplyPowerAction(mon, player, new GrapplePower(mon, player, effect), effect));
                }
            }
            addToBot(new LoseHPFromGrappleAction(null, null, effect + (upgraded ? 1 : 0)));
            if (!freeToPlayOnce) {
                player.energy.use(EnergyPanel.totalCount);
            }
        }
        isDone = true;
    }
}
