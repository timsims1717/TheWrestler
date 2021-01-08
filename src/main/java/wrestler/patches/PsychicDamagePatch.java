package wrestler.patches;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.stances.DivinityStance;
import com.megacrit.cardcrawl.stances.WrathStance;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import wrestler.cards.AbstractWrestlerCard;

import java.util.ArrayList;

public class PsychicDamagePatch {
    @SpireEnum
    public static AbstractGameAction.AttackEffect PSYCHIC_EFFECT;

    @SpireEnum
    public static DamageInfo.DamageType PSYCHIC_DAMAGE;

    private static Texture PSYCHIC_IMAGE = null;
    public static final String PSYCHIC_SOUND = "PSYCHIC_SOUND";
    public static final Color PSYCHIC_COLOR = new Color(0.066f, 0.066f, 0.3f, 1.0f);

    /* Damage Actions */

    @SpirePatch(clz = DamageAction.class, method = "update")
    public static class PsychicTintPatch {
        @SpireInsertPatch(
                locator = TintLocator.class
        )
        public static SpireReturn tintInsert(DamageAction __instance) {
            if (__instance.attackEffect == PSYCHIC_EFFECT) {
                if (__instance.target.hasPower(ArtifactPower.POWER_ID)) {
                    __instance.target.getPower(ArtifactPower.POWER_ID).flash();
                } else {
                    __instance.target.tint.color.set(PSYCHIC_COLOR.cpy());
                    __instance.target.tint.changeColor(Color.WHITE.cpy());
//                  AbstractDungeon.actionManager.addToBottom(new SFXAction(PSYCHIC_SOUND));
                }
            }
            return SpireReturn.Continue();
        }

        private static class TintLocator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher matcher = new Matcher.MethodCallMatcher(AbstractCreature.class, "damage");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), matcher);
            }
        }
    }

    @SpirePatch(clz = DamageAllEnemiesAction.class, method = "update")
    public static class PsychicTintMultiPatch {
        @SpireInsertPatch(
                rloc = 41,
                localvars = {"i"}
        )
        public static SpireReturn tintInsert(DamageAllEnemiesAction __instance, int i) {
            if (__instance.attackEffect == PSYCHIC_EFFECT) {
                AbstractMonster mon = AbstractDungeon.getCurrRoom().monsters.monsters.get(i);
                if (mon.hasPower(ArtifactPower.POWER_ID)) {
                    mon.getPower(ArtifactPower.POWER_ID).flash();
                } else {
                    AbstractDungeon.getCurrRoom().monsters.monsters.get(i).tint.color.set(PSYCHIC_COLOR.cpy());
                    AbstractDungeon.getCurrRoom().monsters.monsters.get(i).tint.changeColor(Color.WHITE.cpy());
//                  AbstractDungeon.actionManager.addToBottom(new SFXAction(PSYCHIC_SOUND));
                }
            }
            return SpireReturn.Continue();
        }
    }

    /* Effects */

    @SpirePatch(clz = FlashAtkImgEffect.class, method = SpirePatch.CONSTRUCTOR, paramtypez = { float.class, float.class, AbstractGameAction.AttackEffect.class, boolean.class })
    public static class PsychicImagePatch {
        @SpireInsertPatch(
                rloc = 6
        )
        public static SpireReturn imageInsert(FlashAtkImgEffect __instance, float x, float y, AbstractGameAction.AttackEffect effect, boolean mute) {
            if (effect == PSYCHIC_EFFECT) {
                if (PSYCHIC_IMAGE == null) {
                    PSYCHIC_IMAGE = new Texture(Gdx.files.internal("wrestlerResources/images/vfx/psychic.png"));
                }
                __instance.img = new TextureAtlas.AtlasRegion(PSYCHIC_IMAGE, 0, 0, 250, 242);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = FlashAtkImgEffect.class, method = SpirePatch.CONSTRUCTOR, paramtypez = { float.class, float.class, AbstractGameAction.AttackEffect.class, boolean.class })
    public static class PsychicSoundPatch {
        @SpireInsertPatch(
                rloc = 12
        )
        public static SpireReturn soundInsert(FlashAtkImgEffect __instance, float x, float y, AbstractGameAction.AttackEffect effect, @ByRef boolean[] mute) {
            if (effect == PSYCHIC_EFFECT && !mute[0]) {
                CardCrawlGame.sound.play(PSYCHIC_SOUND);
                mute[0] = true;
            }
            return SpireReturn.Continue();
        }
    }

    /* Powers */

    @SpirePatch(clz = CurlUpPower.class, method = "onAttacked")
    public static class PsychicCurlUpPatch {
        @SpireInsertPatch(
                rloc = 0
        )
        public static SpireReturn<Float> curlUpInsert(CurlUpPower __instance, DamageInfo info, int damageAmount) {
            if (damageAmount < __instance.owner.currentHealth && damageAmount > 0 && info.owner != null && info.type == PSYCHIC_DAMAGE) {
                __instance.flash();
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction((AbstractMonster)__instance.owner, "CLOSED"));
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(__instance.owner, __instance.owner, __instance.amount));
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(__instance.owner, __instance.owner, "Curl Up"));
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = DoubleDamagePower.class, method = "atDamageGive")
    public static class PsychicDoubleDamagePatch {
        @SpireInsertPatch(
                rloc = 0
        )
        public static SpireReturn<Float> doubleDamageInsert(DoubleDamagePower __instance, float damage, DamageInfo.DamageType type) {
            if (type == PSYCHIC_DAMAGE) {
                return SpireReturn.Return(damage * 2.0f);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = MalleablePower.class, method = "onAttacked")
    public static class PsychicMalleablePatch {
        @SpireInsertPatch(
                rloc = 0
        )
        public static SpireReturn<Float> malleableInsert(MalleablePower __instance, DamageInfo info, int damageAmount) {
            if (damageAmount < __instance.owner.currentHealth && damageAmount > 0 && info.owner != null && info.type == PSYCHIC_DAMAGE) {
                __instance.flash();
                if (__instance.owner.isPlayer) {
                    AbstractDungeon.actionManager.addToTop(new GainBlockAction(__instance.owner, __instance.owner, __instance.amount));
                } else {
                    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(__instance.owner, __instance.owner, __instance.amount));
                }

                __instance.amount++;
                __instance.updateDescription();
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = PenNibPower.class, method = "atDamageGive")
    public static class PsychicPenNibPatch {
        @SpireInsertPatch(
                rloc = 0
        )
        public static SpireReturn<Float> penNibInsert(PenNibPower __instance, float damage, DamageInfo.DamageType type) {
            if (type == PSYCHIC_DAMAGE) {
                return SpireReturn.Return(damage * 2.0f);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = SlowPower.class, method = "atDamageReceive")
    public static class PsychicSlowPatch {
        @SpireInsertPatch(
                rloc = 0
        )
        public static SpireReturn<Float> slowInsert(SlowPower __instance, float damage, DamageInfo.DamageType type) {
            if (type == PSYCHIC_DAMAGE) {
                return SpireReturn.Return(damage * (1.0F + (float)__instance.amount * 0.1F));
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = VigorPower.class, method = "atDamageGive")
    public static class PsychicVigorPatch {
        @SpireInsertPatch(
                rloc = 0
        )
        public static SpireReturn<Float> vigorInsert(VigorPower __instance, float damage, DamageInfo.DamageType type) {
            if (type == PSYCHIC_DAMAGE) {
                return SpireReturn.Return(damage + (float)__instance.amount);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = VulnerablePower.class, method = "atDamageReceive")
    public static class PsychicVulnerablePatch {
        @SpireInsertPatch(
                rloc = 0
        )
        public static SpireReturn<Float> vulnInsert(VulnerablePower __instance, float damage, DamageInfo.DamageType type) {
            if (type == PSYCHIC_DAMAGE) {
                if (__instance.owner.isPlayer && AbstractDungeon.player.hasRelic("Odd Mushroom")) {
                    return SpireReturn.Return(damage * 1.25F);
                } else if (!__instance.owner.isPlayer && AbstractDungeon.player.hasRelic("Paper Frog")) {
                    return SpireReturn.Return(damage * 1.75F);
                } else {
                    return SpireReturn.Return(damage * 1.5F);
                }
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = WeakPower.class, method = "atDamageGive")
    public static class PsychicWeakPatch {
        @SpireInsertPatch(
                rloc = 0
        )
        public static SpireReturn<Float> weakInsert(WeakPower __instance, float damage, DamageInfo.DamageType type) {
            if (type == PSYCHIC_DAMAGE) {
                if (!__instance.owner.isPlayer && AbstractDungeon.player.hasRelic("Paper Crane")) {
                    return SpireReturn.Return(damage * 0.6F);
                } else {
                    return SpireReturn.Return(damage * 0.75F);
                }
            }
            return SpireReturn.Continue();
        }
    }

    /* Stances */

    @SpirePatch(clz = WrathStance.class, method = "atDamageGive")
    public static class PsychicWrathPatch {
        @SpireInsertPatch(
                rloc = 0
        )
        public static SpireReturn<Float> wrathInsert(WrathStance __instance, float damage, DamageInfo.DamageType type) {
            if (type == PSYCHIC_DAMAGE) {
                return SpireReturn.Return(damage * 2.0f);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = DivinityStance.class, method = "atDamageGive")
    public static class PsychicDivinityPatch {
        @SpireInsertPatch(
                rloc = 0
        )
        public static SpireReturn<Float> divinityInsert(DivinityStance __instance, float damage, DamageInfo.DamageType type) {
            if (type == PSYCHIC_DAMAGE) {
                return SpireReturn.Return(damage * 3.0f);
            }
            return SpireReturn.Continue();
        }
    }

    /* Block */

    @SpirePatch(clz = AbstractCreature.class, method = "decrementBlock")
    public static class PsychicBlockPatch {
        @SpireInsertPatch(
                rloc = 0
        )
        public static SpireReturn<Integer> blockInsert(AbstractCreature __instance, DamageInfo info, int damageAmount) {
            if (info.type == PSYCHIC_DAMAGE) {
                return SpireReturn.Return(damageAmount);
            }
            return SpireReturn.Continue();
        }
    }

    /* Apply Powers */

    @SpirePatch(clz = AbstractCard.class, method = "applyPowers")
    public static class PsychicPowersDamage {
        @SpireInsertPatch(
                rloc = 6,
                localvars = {"player"}
        )
        public static SpireReturn powersInsert(AbstractCard __instance, AbstractPlayer player) {
            if (__instance instanceof AbstractWrestlerCard) {
                AbstractWrestlerCard inst = (AbstractWrestlerCard) __instance;
                inst.isPsychicModified = false;
                float tmp = (float) inst.basePsychic;

                for (AbstractRelic r : player.relics) {
                    tmp = r.atDamageModify(tmp, inst);
                }

                for (AbstractPower p : player.powers) {
                    tmp = p.atDamageGive(tmp, PSYCHIC_DAMAGE, inst);
                }

                tmp = player.stance.atDamageGive(tmp, PSYCHIC_DAMAGE, inst);

                for (AbstractPower p : player.powers) {
                    tmp = p.atDamageFinalGive(tmp, PSYCHIC_DAMAGE, inst);
                }

                if (tmp < 0.0F) {
                    tmp = 0.0F;
                }

                if (inst.basePsychic != MathUtils.floor(tmp)) {
                    inst.isPsychicModified = true;
                }

                inst.psychic = MathUtils.floor(tmp);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "calculateCardDamage")
    public static class PsychicMultiPowersDamage {
        @SpireInsertPatch(
                locator = MultiLocator.class,
                localvars = {"player"}
        )
        public static SpireReturn calculateMultiPowersInsert(AbstractCard __instance, AbstractMonster mo, AbstractPlayer player) {
            if (__instance instanceof AbstractWrestlerCard) {
                AbstractWrestlerCard inst = (AbstractWrestlerCard) __instance;
                inst.isPsychicModified = false;

                float[] tmp = new float[AbstractDungeon.getCurrRoom().monsters.monsters.size()];
                int i;
                for(i = 0; i < tmp.length; i++) {
                    tmp[i] = (float)inst.basePsychic;
                }

                for(i = 0; i < tmp.length; i++) {
                    for (AbstractRelic r : player.relics) {
                        tmp[i] = r.atDamageModify(tmp[i], inst);
                    }

                    for (AbstractPower p : player.powers) {
                        tmp[i] = p.atDamageGive(tmp[i], PSYCHIC_DAMAGE, inst);
                    }

                    tmp[i] = player.stance.atDamageGive(tmp[i], PSYCHIC_DAMAGE, inst);
                }

                for(i = 0; i < tmp.length; i++) {
                    for (AbstractPower p : player.powers) {
                        tmp[i] = p.atDamageFinalGive(tmp[i], PSYCHIC_DAMAGE, inst);
                    }
                }

                for(i = 0; i < tmp.length; i++) {
                    if (tmp[i] < 0.0F) {
                        tmp[i] = 0.0F;
                    }
                }

                inst.multiPsychicDamage = new int[tmp.length];

                for(i = 0; i < tmp.length; i++) {
                    if (inst.basePsychic != MathUtils.floor(tmp[i])) {
                        inst.isPsychicModified = true;
                    }

                    inst.multiPsychicDamage[i] = MathUtils.floor(tmp[i]);
                }

                inst.psychic = inst.multiPsychicDamage[0];
            }
            return SpireReturn.Continue();
        }

        private static class MultiLocator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher matcher = new Matcher.FieldAccessMatcher(MonsterGroup.class, "monsters");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), matcher);
            }
        }
    }

    /* Calculate Psychic Damage */

    @SpirePatch(clz = AbstractCard.class, method = "calculateCardDamage")
    public static class PsychicCalculateDamage {
        @SpireInsertPatch(
                rloc = 6,
                localvars = {"player"}
        )
        public static SpireReturn calculateDamageInsert(AbstractCard __instance, AbstractMonster mo, AbstractPlayer player) {
            if (__instance instanceof AbstractWrestlerCard) {
                AbstractWrestlerCard inst = (AbstractWrestlerCard) __instance;
                inst.isPsychicModified = false;
                float tmp = (float) inst.basePsychic;

                for (AbstractRelic r : player.relics) {
                    tmp = r.atDamageModify(tmp, inst);
                }

                for (AbstractPower p : player.powers) {
                    tmp = p.atDamageGive(tmp, PSYCHIC_DAMAGE, inst);
                }

                tmp = player.stance.atDamageGive(tmp, PSYCHIC_DAMAGE, inst);

                for (AbstractPower p : mo.powers) {
                    tmp = p.atDamageReceive(tmp, PSYCHIC_DAMAGE, inst);
                }

                for (AbstractPower p : player.powers) {
                    tmp = p.atDamageFinalGive(tmp, PSYCHIC_DAMAGE, inst);
                }

                for (AbstractPower p : mo.powers) {
                    tmp = p.atDamageFinalReceive(tmp, PSYCHIC_DAMAGE, inst);
                    if (mo.hasPower(ArtifactPower.POWER_ID)) {
                        tmp = 0.0f;
                    }
                }

                if (tmp < 0.0F) {
                    tmp = 0.0F;
                }

                if (inst.basePsychic != MathUtils.floor(tmp)) {
                    inst.isPsychicModified = true;
                }

                inst.psychic = MathUtils.floor(tmp);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "calculateCardDamage")
    public static class PsychicCalculateMultiDamage {
        @SpireInsertPatch(
                locator = MultiLocator.class,
                localvars = {"player"}
        )
        public static SpireReturn calculateMultiDamageInsert(AbstractCard __instance, AbstractMonster mo, AbstractPlayer player) {
            if (__instance instanceof AbstractWrestlerCard) {
                AbstractWrestlerCard inst = (AbstractWrestlerCard) __instance;
                inst.isPsychicModified = false;
                ArrayList<AbstractMonster> mons = AbstractDungeon.getCurrRoom().monsters.monsters;

                float[] tmp = new float[mons.size()];
                int i;
                for(i = 0; i < tmp.length; i++) {
                    tmp[i] = (float)inst.basePsychic;
                }

                for(i = 0; i < tmp.length; i++) {
                    for (AbstractRelic r : player.relics) {
                        tmp[i] = r.atDamageModify(tmp[i], inst);
                    }

                    for (AbstractPower p : player.powers) {
                        tmp[i] = p.atDamageGive(tmp[i], PSYCHIC_DAMAGE, inst);
                    }

                    tmp[i] = player.stance.atDamageGive(tmp[i], PSYCHIC_DAMAGE, inst);
                }

                for(i = 0; i < tmp.length; i++) {
                    AbstractMonster m = mons.get(i);
                    if (!m.isDying && !m.isEscaping) {
                        for (AbstractPower p : m.powers) {
                            tmp[i] = p.atDamageReceive(tmp[i], PSYCHIC_DAMAGE, inst);
                        }
                    }
                }

                for(i = 0; i < tmp.length; i++) {
                    for (AbstractPower p : player.powers) {
                        tmp[i] = p.atDamageFinalGive(tmp[i], PSYCHIC_DAMAGE, inst);
                    }
                }

                for(i = 0; i < tmp.length; i++) {
                    AbstractMonster m = mons.get(i);
                    if (!m.isDying && !m.isEscaping) {
                        for (AbstractPower p : m.powers) {
                            tmp[i] = p.atDamageFinalReceive(tmp[i], PSYCHIC_DAMAGE, inst);
                        }
                        if (m.hasPower(ArtifactPower.POWER_ID)) {
                            tmp[i] = 0.0f;
                        }
                    }
                }

                for(i = 0; i < tmp.length; i++) {
                    if (tmp[i] < 0.0F) {
                        tmp[i] = 0.0F;
                    }
                }

                inst.multiPsychicDamage = new int[tmp.length];

                for(i = 0; i < tmp.length; i++) {
                    if (inst.basePsychic != MathUtils.floor(tmp[i])) {
                        inst.isPsychicModified = true;
                    }

                    inst.multiPsychicDamage[i] = MathUtils.floor(tmp[i]);
                }

                inst.psychic = inst.multiPsychicDamage[0];
            }
            return SpireReturn.Continue();
        }

        private static class MultiLocator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher matcher = new Matcher.FieldAccessMatcher(MonsterGroup.class, "monsters");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), matcher);
            }
        }
    }
}
