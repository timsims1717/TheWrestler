package wrestler.rewards;

import basemod.abstracts.CustomReward;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import wrestler.patches.ComboRewardsPatch;
import wrestler.util.TextureLoader;

public class ComboGoldReward extends CustomReward {
    // Textures
    private static final Texture SSS_ICON = TextureLoader.getTexture("wrestlerResources/images/ui/SSS_64.png");
    private static final Texture SS_ICON = TextureLoader.getTexture("wrestlerResources/images/ui/SS_64.png");
    private static final Texture S_ICON = TextureLoader.getTexture("wrestlerResources/images/ui/S_64.png");
    private static final Texture A_ICON = TextureLoader.getTexture("wrestlerResources/images/ui/A_64.png");
    private static final Texture B_ICON = TextureLoader.getTexture("wrestlerResources/images/ui/B_64.png");
    private static final Texture C_ICON = TextureLoader.getTexture("wrestlerResources/images/ui/C_64.png");
    private static final Texture D_ICON = TextureLoader.getTexture("wrestlerResources/images/ui/D_64.png");
    private static final Texture F_ICON = TextureLoader.getTexture("wrestlerResources/images/ui/F_64.png");

    // Tiers
    private static final int SSS_GOLD = 25;
    private static final int SS_GOLD = 18;
    private static final int S_GOLD = 12;
    private static final int A_GOLD = 8;
    private static final int B_GOLD = 5;
    private static final int C_GOLD = 3;
    private static final int D_GOLD = 1;
    private static final int F_GOLD = 0;

    public ComboGoldReward(int gold, Texture icon, int tier) {
        super(icon, gold + " (Combo)", ComboRewardsPatch.WRESTLER_COMBO_GOLD);
        bonusGold = tier;
        goldAmt = gold;
    }

    @Override
    public boolean claimReward() {
        AbstractDungeon.player.gainGold(goldAmt);
        // todo: add sound (probably check hierophant)
        return true;
    }

    public static ComboGoldReward addComboGoldToRewards(int tier) {
        Texture icon;
        int gold;
        if (tier >= 7) {
            icon = SSS_ICON;
            gold = SSS_GOLD;
        } else if (tier == 6) {
            icon = SS_ICON;
            gold = SS_GOLD;
        } else if (tier == 5) {
            icon = S_ICON;
            gold = S_GOLD;
        } else if (tier == 4) {
            icon = A_ICON;
            gold = A_GOLD;
        } else if (tier == 3) {
            icon = B_ICON;
            gold = B_GOLD;
        } else if (tier == 2) {
            icon = C_ICON;
            gold = C_GOLD;
        } else if (tier == 1) {
            icon = D_ICON;
            gold = D_GOLD;
        } else {
            icon = F_ICON;
            gold = F_GOLD;
        }
        return new ComboGoldReward(gold, icon, tier);
    }
}
