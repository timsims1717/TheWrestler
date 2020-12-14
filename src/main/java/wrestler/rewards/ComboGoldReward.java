package wrestler.rewards;

import basemod.abstracts.CustomReward;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import wrestler.patches.ComboRewardsPatch;

public class ComboGoldReward extends CustomReward {
    private static final Texture ICON = new Texture(Gdx.files.internal("wrestlerResources/images/ui/gold.png"));

    public int amount;

    public ComboGoldReward(int amount) {
        super(ICON, amount + " (Combo)", ComboRewardsPatch.WRESTLER_COMBO_GOLD);
        goldAmt = amount;
    }

    @Override
    public boolean claimReward() {
        AbstractDungeon.player.gainGold(goldAmt);
        return true;
    }

    public static void addComboGoldToRewards(int gold) {
        for (RewardItem i : AbstractDungeon.getCurrRoom().rewards) {
            if (i.type == ComboRewardsPatch.WRESTLER_COMBO_GOLD) {
                i.goldAmt += gold;
                i.text = i.goldAmt + " Combo Gold";
                return;
            }
        }
        AbstractDungeon.getCurrRoom().rewards.add(new ComboGoldReward(gold));
    }
}
