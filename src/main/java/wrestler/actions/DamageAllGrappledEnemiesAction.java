package wrestler.actions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import wrestler.powers.GrapplePower;

public class DamageAllGrappledEnemiesAction extends AbstractGameAction {
    public int[] damage;
    private int baseDamage;
    private boolean firstFrame;
    private boolean utilizeBaseDamage;

    public DamageAllGrappledEnemiesAction(AbstractCreature source, int[] amount, DamageInfo.DamageType type, AbstractGameAction.AttackEffect effect, boolean isFast) {
        this.firstFrame = true;
        this.utilizeBaseDamage = false;
        this.source = source;
        this.damage = amount;
        this.actionType = AbstractGameAction.ActionType.DAMAGE;
        this.damageType = type;
        this.attackEffect = effect;
        if (isFast) {
            this.duration = Settings.ACTION_DUR_XFAST;
        } else {
            this.duration = Settings.ACTION_DUR_FAST;
        }

    }

    public DamageAllGrappledEnemiesAction(AbstractCreature source, int[] amount, DamageInfo.DamageType type, AbstractGameAction.AttackEffect effect) {
        this(source, amount, type, effect, false);
    }

    public DamageAllGrappledEnemiesAction(AbstractPlayer player, int baseDamage, DamageInfo.DamageType type, AbstractGameAction.AttackEffect effect) {
        this(player, (int[])null, type, effect, false);
        this.damageType = type;
        this.baseDamage = baseDamage;
        this.utilizeBaseDamage = true;
    }

    public void update() {
        int n;
        if (this.firstFrame) {
            boolean playedMusic = false;
            n = AbstractDungeon.getCurrRoom().monsters.monsters.size();
            if (this.utilizeBaseDamage) {
                this.damage = DamageInfo.createDamageMatrix(baseDamage, damageType == DamageInfo.DamageType.THORNS);
            }

            for (int i = 0; i < n; ++i) {
                AbstractMonster m = AbstractDungeon.getCurrRoom().monsters.monsters.get(i);
                if (!m.isDying && m.currentHealth > 0 && !m.isEscaping && m.hasPower(GrapplePower.POWER_ID)) {
                    if (playedMusic) {
                        AbstractDungeon.effectList.add(new FlashAtkImgEffect(m.hb.cX, m.hb.cY, this.attackEffect, true));
                    } else {
                        playedMusic = true;
                        AbstractDungeon.effectList.add(new FlashAtkImgEffect(m.hb.cX, m.hb.cY, this.attackEffect));
                    }
                }
            }

            this.firstFrame = false;
        }

        this.tickDuration();
        if (this.isDone) {
//            Iterator var4 = AbstractDungeon.player.powers.iterator();
//
//            while(var4.hasNext()) {
//                AbstractPower p = (AbstractPower)var4.next();
//                p.onDamageAllEnemies(this.damage);
//            }

            int temp = AbstractDungeon.getCurrRoom().monsters.monsters.size();

            for (int i = 0; i < temp; ++i) {
                AbstractMonster m = AbstractDungeon.getCurrRoom().monsters.monsters.get(i);
                if (!m.isDeadOrEscaped() && m.hasPower(GrapplePower.POWER_ID)) {
                    if (this.attackEffect == AbstractGameAction.AttackEffect.POISON) {
                        m.tint.color.set(Color.CHARTREUSE);
                        m.tint.changeColor(Color.WHITE.cpy());
                    } else if (this.attackEffect == AbstractGameAction.AttackEffect.FIRE) {
                        m.tint.color.set(Color.RED);
                        m.tint.changeColor(Color.WHITE.cpy());
                    }

                    m.damage(new DamageInfo(this.source, this.damage[i], this.damageType));
                }
            }

            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }

            if (!Settings.FAST_MODE) {
                this.addToTop(new WaitAction(0.1F));
            }
        }

    }
}
