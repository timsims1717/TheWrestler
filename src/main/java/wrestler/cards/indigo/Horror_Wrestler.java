package wrestler.cards.indigo;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import wrestler.actions.MakeRandomCardAction;
import wrestler.cards.AbstractTriggerOnDrawnCard;
import wrestler.characters.TheWrestler;
import wrestler.powers.GrapplePower;

import java.util.Iterator;

import static wrestler.Wrestler.makeCardPath;

public class Horror_Wrestler extends AbstractTriggerOnDrawnCard {

    // TEXT DECLARATION

    public static final String ID = wrestler.Wrestler.makeID(Horror_Wrestler.class.getSimpleName());
    public static final String IMG = makeCardPath("Skill.png");// "public static final String IMG = makeCardPath("${NAME}.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.


    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheWrestler.Enums.COLOR_INDIGO;

    private static final int DEBUFF = 1;
    private static final int UPGRADE_DEBUFF = 1;

    public Horror_Wrestler() {
        super(ID, IMG, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = DEBUFF;
    }

    @Override
    public void triggerWhenDrawn() {
        AbstractPlayer p = AbstractDungeon.player;
        Iterator var1 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();

        while (var1.hasNext()) {
            AbstractMonster mon = (AbstractMonster) var1.next();
            addToBot(new ApplyPowerAction(mon, p, new WeakPower(mon, magicNumber, false), magicNumber));
        }

        var1 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();

        while (var1.hasNext()) {
            AbstractMonster mon = (AbstractMonster) var1.next();
            addToBot(new ApplyPowerAction(mon, p, new VulnerablePower(mon, magicNumber, false), magicNumber));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeMagicNumber(UPGRADE_DEBUFF);
        }
        super.upgrade();
    }
}
