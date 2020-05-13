package wrestler.util;

import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.ArrayList;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.cardRandomRng;

public class BoonCreator {

    public AbstractCard returnRandomBoon() {
        ArrayList<AbstractCard> list = new ArrayList();

        list.add(new wrestler.cards.special.IntangibleBoon_Wrestler());
        list.add(new wrestler.cards.special.PlatedArmorBoon_Wrestler());

        return list.get(cardRandomRng.random(list.size() - 1));
    }
}
