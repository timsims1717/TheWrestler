package wrestler.rewards;

import basemod.abstracts.CustomReward;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import wrestler.patches.ComboRewardsPatch;

public class ComboCardReward extends CustomReward {
    private static final Texture ICON_NORMAL = new Texture(Gdx.files.internal("wrestlerResources/images/ui/normalCardReward.png"));
    private static final Texture ICON_BOSS = new Texture(Gdx.files.internal("wrestlerResources/images/ui/bossCardReward.png"));

    public ComboCardReward() {
        super(
                AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss ? ICON_BOSS : ICON_NORMAL,
                TEXT[2] + " (Combo)",
                ComboRewardsPatch.WRESTLER_COMBO_CARD
        );
        cards = AbstractDungeon.getRewardCards();
    }

    @Override
    public boolean claimReward() {
        if (AbstractDungeon.player.hasRelic("Question Card")) {
            AbstractDungeon.player.getRelic("Question Card").flash();
        }

        if (AbstractDungeon.player.hasRelic("Busted Crown")) {
            AbstractDungeon.player.getRelic("Busted Crown").flash();
        }

        if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.COMBAT_REWARD) {
            AbstractDungeon.cardRewardScreen.open(cards, this, TEXT[4]);
            AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.COMBAT_REWARD;
        }

        return false;
    }
}
