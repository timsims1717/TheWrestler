package wrestler.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.SetMoveAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.Mugger;
import com.megacrit.cardcrawl.monsters.exordium.Looter;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import wrestler.powers.CompelledPower;

import java.util.ArrayList;

public class PreventEscapePatch {
    @SpirePatch(clz = Looter.class, method = "takeTurn")
    public static class PreventLooterEscapePatch {
        @SpireInsertPatch(
                locator = LooterEscapeLocator.class
        )
        public static SpireReturn looterInsert(Looter __instance) {
            if (__instance.hasPower(CompelledPower.POWER_ID)) {
                __instance.addToBot(new TalkAction(__instance, CANT_LEAVE_MESSAGE, 0.75F, 2.5F));
                __instance.addToBot(new SetMoveAction(__instance, Looter.MOVES[1], (byte)1, AbstractMonster.Intent.ATTACK, (__instance.damage.get(0)).base));
                __instance.getPower(CompelledPower.POWER_ID).flash();
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }

        private static class LooterEscapeLocator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher msgMatcher = new Matcher.FieldAccessMatcher(Looter.class, "RUN_MSG");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), msgMatcher);
            }
        }
    }

    @SpirePatch(clz = Mugger.class, method = "takeTurn")
    public static class PreventMuggerEscapePatch {
        @SpireInsertPatch(
                locator = MuggerEscapeLocator.class
        )
        public static SpireReturn muggerInsert(Mugger __instance) {
            if (__instance.hasPower(CompelledPower.POWER_ID)) {
                __instance.addToBot(new TalkAction(__instance, CANT_LEAVE_MESSAGE, 0.75F, 2.5F));
                __instance.addToBot(new SetMoveAction(__instance, Mugger.MOVES[1], (byte)1, AbstractMonster.Intent.ATTACK, (__instance.damage.get(0)).base));
                __instance.getPower(CompelledPower.POWER_ID).flash();
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }

        private static class MuggerEscapeLocator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher msgMatcher = new Matcher.FieldAccessMatcher(Mugger.class, "RUN_MSG");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), msgMatcher);
            }
        }
    }

    private static final String CANT_LEAVE_MESSAGE = "I ... can't ... leave!";
}