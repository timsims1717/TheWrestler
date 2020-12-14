package wrestler.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.SetMoveAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.Looter;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import wrestler.powers.CompelledPower;

import java.util.ArrayList;

public class EnemyPowerExhaustPatch {
    @SpirePatch(clz = CardGroup.class, method = "moveToExhaustPile")
    public static class Patch {
        @SpireInsertPatch(
                locator = ExhaustLocator.class,
                localvars = {"c"}
        )
        public static SpireReturn insert(CardGroup __instance, AbstractCard c) {
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                for (AbstractPower p : m.powers) {
                    p.onExhaust(c);
                }
            }
            return SpireReturn.Continue();
        }

        private static class ExhaustLocator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher matcher = new Matcher.MethodCallMatcher(AbstractCard.class, "triggerOnExhaust");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), matcher);
            }
        }
    }
}