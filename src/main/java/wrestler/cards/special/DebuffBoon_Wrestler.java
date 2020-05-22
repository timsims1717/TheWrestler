package wrestler.cards.special;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerToRandomEnemyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import wrestler.cards.AbstractWrestlerCard;
import wrestler.powers.HorrorPower;

import java.util.Iterator;

import static wrestler.Wrestler.makeCardPath;

public class DebuffBoon_Wrestler extends AbstractWrestlerCard {

    // TEXT DECLARATION

    public static final String ID = wrestler.Wrestler.makeID(DebuffBoon_Wrestler.class.getSimpleName());
    public static final String IMG = makeCardPath("Skill.png");// "public static final String IMG = makeCardPath("${NAME}.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.


    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = CardColor.COLORLESS;

    private static final int COST = 0;

    private static final int DEBUFF = 2;
    private static final int UPGRADE_DEBUFF = 1;

    // /STAT DECLARATION/


    public DebuffBoon_Wrestler() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = DEBUFF;
        exhaust = true;
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Iterator var1 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();
        while (var1.hasNext()) {
            AbstractMonster mon = (AbstractMonster) var1.next();
            addToBot(new ApplyPowerToRandomEnemyAction(p, new WeakPower(mon, magicNumber, false), magicNumber));
            addToBot(new ApplyPowerToRandomEnemyAction(p, new VulnerablePower(mon, magicNumber, false), magicNumber));
        }
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_DEBUFF);
            initializeDescription();
        }
    }
}
