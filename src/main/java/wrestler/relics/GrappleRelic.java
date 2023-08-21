package wrestler.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import wrestler.powers.GrapplePower;
import wrestler.util.TextureLoader;

import java.util.Iterator;

import static wrestler.Wrestler.makeRelicOutlinePath;
import static wrestler.Wrestler.makeRelicPath;

public class GrappleRelic extends CustomRelic {

    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     *
     * At the start of combat, Grapple a random enemy.
     */

    // ID, images, text.
    public static final String ID = wrestler.Wrestler.makeID("GrappleRelic");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("placeholder_relic.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("placeholder_relic.png"));

    public GrappleRelic() {
        super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, LandingSound.MAGICAL);
    }

    // Flash at the start of Battle.
    @Override
    public void atBattleStartPreDraw() {
        flash();
    }

    @Override
    public void atBattleStart() {
        AbstractMonster m = AbstractDungeon.getRandomMonster();
        AbstractPlayer p = AbstractDungeon.player;
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(m, m, new GrapplePower(m, p))
        );
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
