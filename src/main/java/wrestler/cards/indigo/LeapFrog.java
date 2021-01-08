package wrestler.cards.indigo;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import wrestler.cards.AbstractWrestlerCard;
import wrestler.characters.TheWrestler;

import static com.megacrit.cardcrawl.monsters.AbstractMonster.Intent.*;
import static com.megacrit.cardcrawl.monsters.AbstractMonster.Intent.ATTACK_DEFEND;
import static wrestler.Wrestler.makeCardPath;

public class LeapFrog extends AbstractWrestlerCard {

    // TEXT DECLARATION

    public static final String ID = wrestler.Wrestler.makeID(LeapFrog.class.getSimpleName());
    public static final String IMG = makeCardPath("Skill.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF_AND_ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheWrestler.Enums.COLOR_INDIGO;

    private static final int COST = 1;

    private static final int BLOCK = 6;
    private static final int UPGRADE_BLOCK = 3;
    private static final int ENERGY_CARD = 1;

    // /STAT DECLARATION/

    public LeapFrog() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        block = baseBlock = BLOCK;
        magicNumber = baseMagicNumber = ENERGY_CARD;
    }

    @Override
    public void triggerOnGlowCheck() {
        glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (!m.isDeadOrEscaped() && m.intent != ATTACK && m.intent != ATTACK_BUFF && m.intent != ATTACK_DEBUFF && m.intent != ATTACK_DEFEND) {
                glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
                break;
            }
        }
    }


    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, block));
        if (m.isDeadOrEscaped() && m.intent != ATTACK && m.intent != ATTACK_BUFF && m.intent != ATTACK_DEBUFF && m.intent != ATTACK_DEFEND) {
            addToBot(new GainEnergyAction(ENERGY_CARD));
            addToBot(new DrawCardAction(ENERGY_CARD));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_BLOCK);
            initializeDescription();
        }
    }
}
