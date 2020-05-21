package wrestler.util;

import com.megacrit.cardcrawl.cards.AbstractCard;
import wrestler.Wrestler;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.cardRandomRng;

public class BoonCreator {

    public AbstractCard returnRandomBoon() {
        int perc = cardRandomRng.random(100);
        if (perc < 20) {
            return Wrestler.rareBoons.get(cardRandomRng.random(Wrestler.rareBoons.size() - 1)).makeStatEquivalentCopy();
        } else if (perc < 65) {
            return Wrestler.uncommonBoons.get(cardRandomRng.random(Wrestler.uncommonBoons.size() - 1)).makeStatEquivalentCopy();
        } else {
            return Wrestler.commonBoons.get(cardRandomRng.random(Wrestler.commonBoons.size() - 1)).makeStatEquivalentCopy();
        }
    }
}
