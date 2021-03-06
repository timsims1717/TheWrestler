package wrestler.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;
import wrestler.powers.GrapplePower;

import java.util.Iterator;

public class CrusherAction extends AbstractGameAction {

    private final AbstractPlayer player;
    private final AbstractMonster target;
    private final int energyOnUse;
    private final boolean freeToPlayOnce;
    private final boolean upgraded;
    private final int damage;

    public CrusherAction(AbstractMonster m, AbstractPlayer p, int damage, boolean freeToPlayOnce, int energyOnUse, boolean upgraded) {
        player = p;
        target = m;
        this.energyOnUse = energyOnUse;
        this.freeToPlayOnce = freeToPlayOnce;
        this.upgraded = upgraded;
        this.damage = damage;
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

        if (upgraded) {
            effect += 1;
        }

        if (effect > 0) {
            addToBot(new VFXAction(new WeightyImpactEffect(target.hb.cX, target.hb.cY)));
            addToBot(new WaitAction(0.8F));
            addToBot(new DamageAction(target, new DamageInfo(target, damage * effect, DamageInfo.DamageType.NORMAL)));
            if (!freeToPlayOnce) {
                player.energy.use(EnergyPanel.totalCount);
            }
        }
        isDone = true;
    }
}
