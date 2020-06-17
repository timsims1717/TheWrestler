package wrestler.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.rewards.RewardItem;

public class ComboRewardsPatch {
    @SpireEnum
    public static RewardItem.RewardType WRESTLER_COMBO_GOLD;
    @SpireEnum
    public static RewardItem.RewardType WRESTLER_COMBO_CARD;
}