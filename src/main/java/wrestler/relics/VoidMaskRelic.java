package wrestler.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import wrestler.actions.ForgetAction;
import wrestler.powers.GrapplePower;
import wrestler.util.TextureLoader;

import java.util.Iterator;

import static wrestler.Wrestler.makeRelicOutlinePath;
import static wrestler.Wrestler.makeRelicPath;

public class VoidMaskRelic extends CustomRelic {

    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     *
     * At the start of combat, discard 1 card from your deck.
     */

    // ID, images, text.
    public static final String ID = wrestler.Wrestler.makeID("VoidMaskRelic");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("placeholder_relic.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("placeholder_relic.png"));

    public VoidMaskRelic() {
        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.MAGICAL);
    }

    // Flash at the start of Battle.
    @Override
    public void atBattleStartPreDraw() {
        flash();
    }

    @Override
    public void atBattleStart() {
        addToBot(new ForgetAction(1));
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
