package wrestler.cards.indigo;

import basemod.BaseMod;
import basemod.interfaces.PostExhaustSubscriber;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReduceCostAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.red.BloodForBlood;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import wrestler.cards.AbstractWrestlerCard;
import wrestler.characters.TheWrestler;

import static wrestler.Wrestler.makeCardPath;


public class EldritchLightning_Wrestler extends AbstractWrestlerCard implements PostExhaustSubscriber {

    // TEXT DECLARATION

    public static final String ID = wrestler.Wrestler.makeID(EldritchLightning_Wrestler.class.getSimpleName());
    public static final String IMG = makeCardPath("Attack.png");// "public static final String IMG = makeCardPath("${NAME}.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.


    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheWrestler.Enums.COLOR_INDIGO;

    private static final int COST = 3;
    private static final int REDUCE = 1;

    private static final int DAMAGE = 5;
    private static final int UPGRADE_DMG = 2;
    private static final int COUNT = 3;

    public EldritchLightning_Wrestler() {
        this(COST);
    }

    public EldritchLightning_Wrestler(int cost) {
        super(ID, IMG, cost, TYPE, COLOR, RARITY, TARGET);
        damage = baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = COUNT;
        BaseMod.subscribe(this);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.devoid();
        for (int i = 0; i < COUNT; i++) {
            this.addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.LIGHTNING));
        }
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DMG);
            initializeDescription();
        }
    }

    @Override
    public void receivePostExhaust(AbstractCard abstractCard) {
        if (abstractCard.cardID.equals(VoidCard.ID)) {
            addToBot(new ReduceCostAction(this.uuid, REDUCE));
        }
    }

    public AbstractCard makeCopy() {
        return new EldritchLightning_Wrestler(cost);
    }
}
