package wrestler.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import wrestler.util.TextureLoader;

import static wrestler.Wrestler.makeRelicOutlinePath;
import static wrestler.Wrestler.makeRelicPath;

public class ComboCountOneRelic extends CustomRelic {

    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     *
     * Your Combo Multiplier increases every 4 cards instead of 5.
     */

    // ID, images, text.
    public static final String ID = wrestler.Wrestler.makeID("ComboCountOneRelic");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("placeholder_relic.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("placeholder_relic.png"));

    public ComboCountOneRelic() {
        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.MAGICAL);
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
