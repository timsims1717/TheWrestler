package wrestler.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAndDeckAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.unique.AddCardToDeckAction;
import com.megacrit.cardcrawl.actions.unique.BurnIncreaseAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.beyond.*;
import com.megacrit.cardcrawl.monsters.city.Taskmaster;
import com.megacrit.cardcrawl.monsters.ending.CorruptHeart;
import com.megacrit.cardcrawl.monsters.ending.SpireSpear;
import com.megacrit.cardcrawl.monsters.exordium.*;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PainfulStabsPower;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import wrestler.powers.MentalWardPower;

import java.util.ArrayList;

public class MentalWardPatch {
    public static void checkForMentalWardTrigger(AbstractCreature source) {
        if (source instanceof AbstractMonster && AbstractDungeon.player.hasPower(MentalWardPower.POWER_ID)) {
            MentalWardPower p = (MentalWardPower) AbstractDungeon.player.getPower(MentalWardPower.POWER_ID);
            p.triggerBot(source);
        }
    }

    @SpirePatch(clz = ApplyPowerAction.class, method = "update")
    public static class ApplyPowerNewPatch {
        @SpireInsertPatch(
                locator = ApplyPowerNewLocator.class
        )
        public static SpireReturn insert(ApplyPowerAction __instance) {
            if (__instance.target.equals(AbstractDungeon.player) &&__instance.source instanceof AbstractMonster && AbstractDungeon.player.hasPower(MentalWardPower.POWER_ID)) {
                MentalWardPower p = (MentalWardPower) AbstractDungeon.player.getPower(MentalWardPower.POWER_ID);
                p.triggerTop(__instance.source);
            }
            return SpireReturn.Continue();
        }

        private static class ApplyPowerNewLocator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher matcher = new Matcher.MethodCallMatcher(AbstractPower.class, "onInitialApplication");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), matcher);
            }
        }
    }

    @SpirePatch(clz = ApplyPowerAction.class, method = "update")
    public static class ApplyPowerPatch {
        @SpireInsertPatch(
                locator = ApplyPowerLocator.class
        )
        public static SpireReturn insert(ApplyPowerAction __instance) {
            if (__instance.target.equals(AbstractDungeon.player) &&__instance.source instanceof AbstractMonster && AbstractDungeon.player.hasPower(MentalWardPower.POWER_ID)) {
                MentalWardPower p = (MentalWardPower) AbstractDungeon.player.getPower(MentalWardPower.POWER_ID);
                p.triggerTop(__instance.source);
            }
            return SpireReturn.Continue();
        }

        private static class ApplyPowerLocator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher matcher = new Matcher.MethodCallMatcher(AbstractPower.class, "updateDescription");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), matcher);
            }
        }
    }

    /* Beyond */

    @SpirePatch(clz = AwakenedOne.class, method = "takeTurn")
    public static class AwakenedOnePatch {
        @SpireInsertPatch(
                locator = AwakenedOneLocator.class
        )
        public static SpireReturn insert(AwakenedOne __instance) {
            checkForMentalWardTrigger(__instance);
            return SpireReturn.Continue();
        }

        private static class AwakenedOneLocator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher matcher = new Matcher.NewExprMatcher(MakeTempCardInDrawPileAction.class);
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), matcher);
            }
        }
    }

    @SpirePatch(clz = Deca.class, method = "takeTurn")
    public static class DecaPatch {
        @SpireInsertPatch(
                locator = DecaLocator.class
        )
        public static SpireReturn insert(Deca __instance) {
            checkForMentalWardTrigger(__instance);
            return SpireReturn.Continue();
        }

        private static class DecaLocator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher matcher = new Matcher.NewExprMatcher(MakeTempCardInDiscardAction.class);
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), matcher);
            }
        }
    }

    @SpirePatch(clz = Nemesis.class, method = "takeTurn")
    public static class NemesisPatch {
        @SpireInsertPatch(
                locator = NemesisLocator.class
        )
        public static SpireReturn insert(Nemesis __instance) {
            checkForMentalWardTrigger(__instance);
            return SpireReturn.Continue();
        }

        private static class NemesisLocator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher matcher = new Matcher.NewExprMatcher(MakeTempCardInDiscardAction.class);
                return LineFinder.findAllInOrder(ctMethodToPatch, new ArrayList<>(), matcher);
            }
        }
    }

    @SpirePatch(clz = OrbWalker.class, method = "takeTurn")
    public static class OrbWalkerPatch {
        @SpireInsertPatch(
                locator = OrbWalkerLocator.class
        )
        public static SpireReturn insert(OrbWalker __instance) {
            checkForMentalWardTrigger(__instance);
            return SpireReturn.Continue();
        }

        private static class OrbWalkerLocator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher matcher = new Matcher.NewExprMatcher(MakeTempCardInDiscardAndDeckAction.class);
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), matcher);
            }
        }
    }

    @SpirePatch(clz = Repulsor.class, method = "takeTurn")
    public static class RepulsorPatch {
        @SpireInsertPatch(
                locator = RepulsorLocator.class
        )
        public static SpireReturn insert(Repulsor __instance) {
            checkForMentalWardTrigger(__instance);
            return SpireReturn.Continue();
        }

        private static class RepulsorLocator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher matcher = new Matcher.NewExprMatcher(MakeTempCardInDrawPileAction.class);
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), matcher);
            }
        }
    }

    @SpirePatch(clz = SnakeDagger.class, method = "takeTurn")
    public static class SnakeDaggerPatch {
        @SpireInsertPatch(
                locator = SnakeDaggerLocator.class
        )
        public static SpireReturn insert(SnakeDagger __instance) {
            checkForMentalWardTrigger(__instance);
            return SpireReturn.Continue();
        }

        private static class SnakeDaggerLocator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher matcher = new Matcher.NewExprMatcher(MakeTempCardInDiscardAction.class);
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), matcher);
            }
        }
    }

    @SpirePatch(clz = TimeEater.class, method = "takeTurn")
    public static class TimeEaterPatch {
        @SpireInsertPatch(
                locator = TimeEaterLocator.class
        )
        public static SpireReturn insert(TimeEater __instance) {
            checkForMentalWardTrigger(__instance);
            return SpireReturn.Continue();
        }

        private static class TimeEaterLocator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher matcher = new Matcher.NewExprMatcher(MakeTempCardInDiscardAction.class);
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), matcher);
            }
        }
    }

    @SpirePatch(clz = WrithingMass.class, method = "takeTurn")
    public static class WrithingMassPatch {
        @SpireInsertPatch(
                locator = WrithingMassLocator.class
        )
        public static SpireReturn insert(WrithingMass __instance) {
            checkForMentalWardTrigger(__instance);
            return SpireReturn.Continue();
        }

        private static class WrithingMassLocator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher matcher = new Matcher.NewExprMatcher(AddCardToDeckAction.class);
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), matcher);
            }
        }
    }

    /* City */

    @SpirePatch(clz = PainfulStabsPower.class, method = "onInflictDamage")
    public static class PainfulStabsPatch {
        @SpireInsertPatch(
                locator = PainfulStabsLocator.class
        )
        public static SpireReturn insert(PainfulStabsPower __instance) {
            checkForMentalWardTrigger(__instance.owner);
            return SpireReturn.Continue();
        }

        private static class PainfulStabsLocator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher matcher = new Matcher.NewExprMatcher(MakeTempCardInDiscardAction.class);
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), matcher);
            }
        }
    }

    @SpirePatch(clz = Taskmaster.class, method = "takeTurn")
    public static class TaskmasterPatch {
        @SpireInsertPatch(
                locator = TaskmasterLocator.class
        )
        public static SpireReturn insert(Taskmaster __instance) {
            checkForMentalWardTrigger(__instance);
            return SpireReturn.Continue();
        }

        private static class TaskmasterLocator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher matcher = new Matcher.NewExprMatcher(MakeTempCardInDiscardAction.class);
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), matcher);
            }
        }
    }

    /* Ending */

    @SpirePatch(clz = CorruptHeart.class, method = "takeTurn")
    public static class CorruptHeartPatch {
        @SpireInsertPatch(
                locator = CorruptHeartLocator.class
        )
        public static SpireReturn insert(CorruptHeart __instance) {
            checkForMentalWardTrigger(__instance);
            return SpireReturn.Continue();
        }

        private static class CorruptHeartLocator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher matcher = new Matcher.NewExprMatcher(MakeTempCardInDrawPileAction.class);
                return LineFinder.findAllInOrder(ctMethodToPatch, new ArrayList<>(), matcher);
            }
        }
    }

    @SpirePatch(clz = SpireSpear.class, method = "takeTurn")
    public static class SpireSpearPatch {
        @SpireInsertPatch(
                locator = SpireSpearLocator.class
        )
        public static SpireReturn insert(SpireSpear __instance) {
            checkForMentalWardTrigger(__instance);
            return SpireReturn.Continue();
        }

        private static class SpireSpearLocator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher matcher = new Matcher.NewExprMatcher(MakeTempCardInDrawPileAction.class);
                int[] first = LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), matcher);
                matcher = new Matcher.NewExprMatcher(MakeTempCardInDiscardAction.class);
                int[] second = LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), matcher);
                return new int[]{ first[0], second[0] };
            }
        }
    }

    /* Ending */

    @SpirePatch(clz = AcidSlime_L.class, method = "takeTurn")
    public static class AcidSlime_LPatch {
        @SpireInsertPatch(
                locator = AcidSlime_LLocator.class
        )
        public static SpireReturn insert(AcidSlime_L __instance) {
            checkForMentalWardTrigger(__instance);
            return SpireReturn.Continue();
        }

        private static class AcidSlime_LLocator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher matcher = new Matcher.NewExprMatcher(MakeTempCardInDiscardAction.class);
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), matcher);
            }
        }
    }

    @SpirePatch(clz = AcidSlime_M.class, method = "takeTurn")
    public static class AcidSlime_MPatch {
        @SpireInsertPatch(
                locator = AcidSlime_MLocator.class
        )
        public static SpireReturn insert(AcidSlime_M __instance) {
            checkForMentalWardTrigger(__instance);
            return SpireReturn.Continue();
        }

        private static class AcidSlime_MLocator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher matcher = new Matcher.NewExprMatcher(MakeTempCardInDiscardAction.class);
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), matcher);
            }
        }
    }

    @SpirePatch(clz = Hexaghost.class, method = "takeTurn")
    public static class HexaghostPatch {
        @SpireInsertPatch(
                locator = HexaghostLocator.class
        )
        public static SpireReturn insert(Hexaghost __instance) {
            checkForMentalWardTrigger(__instance);
            return SpireReturn.Continue();
        }

        private static class HexaghostLocator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher matcher = new Matcher.NewExprMatcher(MakeTempCardInDiscardAction.class);
                int[] first = LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), matcher);
                matcher = new Matcher.NewExprMatcher(BurnIncreaseAction.class);
                int[] second = LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), matcher);
                return new int[]{ first[0], second[0] };
            }
        }
    }

    @SpirePatch(clz = Sentry.class, method = "takeTurn")
    public static class SentryPatch {
        @SpireInsertPatch(
                locator = SentryLocator.class
        )
        public static SpireReturn insert(Sentry __instance) {
            checkForMentalWardTrigger(__instance);
            return SpireReturn.Continue();
        }

        private static class SentryLocator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher matcher = new Matcher.NewExprMatcher(MakeTempCardInDiscardAction.class);
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), matcher);
            }
        }
    }

    @SpirePatch(clz = SlimeBoss.class, method = "takeTurn")
    public static class SlimeBossPatch {
        @SpireInsertPatch(
                locator = SlimeBossLocator.class
        )
        public static SpireReturn insert(SlimeBoss __instance) {
            checkForMentalWardTrigger(__instance);
            return SpireReturn.Continue();
        }

        private static class SlimeBossLocator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher matcher = new Matcher.NewExprMatcher(MakeTempCardInDiscardAction.class);
                return LineFinder.findAllInOrder(ctMethodToPatch, new ArrayList<>(), matcher);
            }
        }
    }

    @SpirePatch(clz = SpikeSlime_L.class, method = "takeTurn")
    public static class SpikeSlime_LPatch {
        @SpireInsertPatch(
                locator = SpikeSlime_LLocator.class
        )
        public static SpireReturn insert(SpikeSlime_L __instance) {
            checkForMentalWardTrigger(__instance);
            return SpireReturn.Continue();
        }

        private static class SpikeSlime_LLocator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher matcher = new Matcher.NewExprMatcher(MakeTempCardInDiscardAction.class);
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), matcher);
            }
        }
    }

    @SpirePatch(clz = SpikeSlime_M.class, method = "takeTurn")
    public static class SpikeSlime_MPatch {
        @SpireInsertPatch(
                locator = SpikeSlime_MLocator.class
        )
        public static SpireReturn insert(SpikeSlime_M __instance) {
            checkForMentalWardTrigger(__instance);
            return SpireReturn.Continue();
        }

        private static class SpikeSlime_MLocator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher matcher = new Matcher.NewExprMatcher(MakeTempCardInDiscardAction.class);
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), matcher);
            }
        }
    }
}
