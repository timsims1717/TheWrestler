package wrestler.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import wrestler.Wrestler;

import java.util.ArrayList;

public class ComboPatch {
    @SpirePatch(clz = AbstractPlayer.class, method = SpirePatch.CLASS)
    public static class ComboScoreValue {
        public static SpireField<Integer> currentScore = new SpireField<>(()->0);
        public static SpireField<Integer> currentCombo = new SpireField<>(()->0);
        public static SpireField<Integer> currentMultiplier = new SpireField<>(()->1);
        public static SpireField<Integer> currentCardCount = new SpireField<>(()->0);
        public static SpireField<Integer> cardCountMult = new SpireField<>(()->4);
        public static SpireField<Integer> currentTier = new SpireField<>(()->0);
        public static SpireField<Integer> totalHP = new SpireField<>(()->0);
        public static SpireField<Integer> currentTurn = new SpireField<>(()->-1);
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "damage")
    public static class BreakComboPatch {
        @SpireInsertPatch(
                locator = BreakLocator.class,
                localvars = {"damageAmount"}
        )
        public static SpireReturn<Integer> breakComboInsert(AbstractPlayer __instance, DamageInfo info, int damageAmount) {
            Wrestler.breakCombo(false);
            return SpireReturn.Continue();
        }

        private static class BreakLocator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher matcher = new Matcher.FieldAccessMatcher(GameActionManager.class, "damageReceivedThisTurn");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), matcher);
            }
        }
    }

    @SpirePatch(clz = AbstractMonster.class, method = "damage")
    public static class DamageComboPatch {
        @SpireInsertPatch(
                locator = DamageLocator.class
        )
        public static SpireReturn<Integer> damageComboInsert(AbstractMonster __instance, DamageInfo info) {
            Wrestler.increaseCombo(__instance.lastDamageTaken);
            return SpireReturn.Continue();
        }

        private static class DamageLocator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher matcher = new Matcher.MethodCallMatcher(AbstractMonster.class, "useStaggerAnimation");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), matcher);
            }
        }
    }

    @SpirePatch(clz = AbstractMonster.class, method = "damage")
    public static class AllDeadComboPatch {
        @SpireInsertPatch(
                locator = DeadLocator.class
        )
        public static SpireReturn<Integer> allDeadComboInsert(AbstractMonster __instance, DamageInfo info) {
            Wrestler.breakCombo(true);
            return SpireReturn.Continue();
        }

        private static class DeadLocator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher matcher = new Matcher.MethodCallMatcher(GameActionManager.class, "cleanCardQueue");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), matcher);
            }
        }
    }

    @SpirePatch(clz = GameActionManager.class, method = "getNextAction")
    public static class StartTurnComboPatch {
        @SpireInsertPatch(
                locator = TurnLocator.class
        )
        public static SpireReturn<Integer> startTurnComboInsert(GameActionManager __instance) {
            Wrestler.updateTierStartOfTurn();
            return SpireReturn.Continue();
        }

        private static class TurnLocator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher matcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "applyStartOfTurnRelics");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), matcher);
            }
        }
    }
}
