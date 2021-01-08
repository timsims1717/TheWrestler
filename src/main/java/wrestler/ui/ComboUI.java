package wrestler.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import wrestler.patches.ComboPatch.ComboScoreValue;
import wrestler.util.TextureLoader;

import static com.megacrit.cardcrawl.helpers.FontHelper.largeDialogOptionFont;
import static com.megacrit.cardcrawl.helpers.FontHelper.smallDialogOptionFont;

public class ComboUI {
    // Grades
    private static final String SSS_IMG = "wrestlerResources/images/ui/SSS.png";
    private static final String SS_IMG = "wrestlerResources/images/ui/SS.png";
    private static final String S_IMG = "wrestlerResources/images/ui/S.png";
    private static final String A_IMG = "wrestlerResources/images/ui/A.png";
    private static final String B_IMG = "wrestlerResources/images/ui/B.png";
    private static final String C_IMG = "wrestlerResources/images/ui/C.png";
    private static final String D_IMG = "wrestlerResources/images/ui/D.png";
    private static final String F_IMG = "wrestlerResources/images/ui/F.png";

    private static final Texture SSS_TEXTURE = TextureLoader.getTexture(SSS_IMG);
    private static final Texture SS_TEXTURE = TextureLoader.getTexture(SS_IMG);
    private static final Texture S_TEXTURE = TextureLoader.getTexture(S_IMG);
    private static final Texture A_TEXTURE = TextureLoader.getTexture(A_IMG);
    private static final Texture B_TEXTURE = TextureLoader.getTexture(B_IMG);
    private static final Texture C_TEXTURE = TextureLoader.getTexture(C_IMG);
    private static final Texture D_TEXTURE = TextureLoader.getTexture(D_IMG);
    private static final Texture F_TEXTURE = TextureLoader.getTexture(F_IMG);

    private static final float GRADE_SIZE = 100.0f * Settings.scale;
    private static final float GRADE_SIZE_SS = 136.0f * Settings.scale;
    private static final float GRADE_SIZE_SSS = 173.0f * Settings.scale;

    // Colors
    private static final Color SCORE_LABEL_COLOR = new Color(1.0f, 1.0f, 1.0f, 0.0f);
    private static final Color MULT_LABEL_COLOR = new Color(1.0f, 1.0f, 1.0f, 0.0f);

    // Coordinates
    private static final float LABEL_X = 200.0F * Settings.scale;
    private static final float SCORES_X = 220.0F * Settings.scale;
    private static final float SCORE_Y = Settings.HEIGHT - 200.0F * Settings.scale;
    private static final float COMBO_Y = Settings.HEIGHT - 240.0F * Settings.scale;
    private static final float DIGIT_WIDTH = 20.0F * Settings.scale;
    private static final float CARD_COUNT_OFFSET_Y = -20.0f * Settings.scale;

    private static final float GRADE_X = 340.0f * Settings.scale;
    private static final float GRADE_Y = Settings.HEIGHT - 240.0f * Settings.scale;

    // Text
    private static final String SCORE_TEXT = "SCORE:";
    private static final String COMBO_TEXT = "COMBO:";

    private Integer currentScore;
    private Integer currentCombo;
    private Integer currentMult;

    private int roomLastRendered;
    private boolean display;
    private boolean displayMult;

    public ComboUI() {
        roomLastRendered = -1;
        display = false;
        displayMult = false;
        currentScore = 0;
        currentCombo = 0;
        currentMult = 1;
    }

    public void hide() {
        SCORE_LABEL_COLOR.a = 0.0f;
        MULT_LABEL_COLOR.a = 0.0f;
    }

    public void checkFade() {
        if (display) {
            fadeIn();
            if (displayMult) {
                fadeInMult();
            }
        } else {
            fadeOut();
            fadeOutMult();
        }
    }

    public void fadeOut() {
        if (SCORE_LABEL_COLOR.a > 0.0f) {
            SCORE_LABEL_COLOR.a = Math.max(SCORE_LABEL_COLOR.a - Gdx.graphics.getDeltaTime() * 0.75f, 0.0f);
        }
    }

    public void fadeOutMult() {
        if (MULT_LABEL_COLOR.a > 0.0f) {
            MULT_LABEL_COLOR.a = Math.max(MULT_LABEL_COLOR.a - Gdx.graphics.getDeltaTime() * 0.75f, 0.0f);
        }
    }

    public void fadeIn() {
        if (SCORE_LABEL_COLOR.a < 1.0f) {
            SCORE_LABEL_COLOR.a = Math.max(SCORE_LABEL_COLOR.a + Gdx.graphics.getDeltaTime() * 0.75f, 1.0f);
        }
    }

    public void fadeInMult() {
        if (MULT_LABEL_COLOR.a < 1.0f) {
            MULT_LABEL_COLOR.a = Math.min(MULT_LABEL_COLOR.a + Gdx.graphics.getDeltaTime() * 0.75f, 1.0f);
        }
    }

    public void render(SpriteBatch sb, boolean fading) {
        if (AbstractDungeon.floorNum != roomLastRendered) {
            roomLastRendered = AbstractDungeon.floorNum;
            hide();
        }
        display = !fading;
        displayMult = display && ComboScoreValue.currentCombo.get(AbstractDungeon.player) > 0;
        checkFade();
        FontHelper.renderFontRightAligned(sb, largeDialogOptionFont, SCORE_TEXT, LABEL_X, SCORE_Y, SCORE_LABEL_COLOR);
        FontHelper.renderFontRightAligned(sb, largeDialogOptionFont, COMBO_TEXT, LABEL_X, COMBO_Y, SCORE_LABEL_COLOR);
        renderScore(sb);
        renderCombo(sb);
    }

    public void renderScore(SpriteBatch sb) {
        AbstractPlayer player = AbstractDungeon.player;
        Integer actualScore = ComboScoreValue.currentScore.get(player);
        if (actualScore == 0) {
            currentScore = 0;
        } else if (!actualScore.equals(currentScore)) {
            int diff = actualScore - currentScore;
            currentScore += (diff > 0 ? Math.max(1, diff / 20) : Math.min(-1, diff / 20));
            // todo: change color and grow
        }
        FontHelper.renderFontLeft(sb, largeDialogOptionFont, currentScore.toString(), SCORES_X, SCORE_Y, SCORE_LABEL_COLOR);
//        FontHelper.renderFontLeft(sb, largeDialogOptionFont, ComboScoreValue.currentTier.get(player).toString(), GRADE_X, SCORE_Y, SCORE_LABEL_COLOR);
//        FontHelper.renderFontLeft(sb, largeDialogOptionFont, ComboScoreValue.totalHP.get(player).toString(), GRADE_1_X, SCORE_Y, SCORE_LABEL_COLOR);
//        FontHelper.renderFontLeft(sb, largeDialogOptionFont, ComboScoreValue.currentTurn.get(player).toString(), GRADE_2_X, SCORE_Y, SCORE_LABEL_COLOR);
        Texture grade;
        float width = GRADE_SIZE;
        int tier = ComboScoreValue.currentTier.get(player);
        switch (tier) {
            case 7:
                grade = SSS_TEXTURE;
                width = GRADE_SIZE_SSS;
                break;
            case 6:
                grade = SS_TEXTURE;
                width = GRADE_SIZE_SS;
                break;
            case 5:
                grade = S_TEXTURE;
                break;
            case 4:
                grade = A_TEXTURE;
                break;
            case 3:
                grade = B_TEXTURE;
                break;
            case 2:
                grade = C_TEXTURE;
                break;
            case 1:
                grade = D_TEXTURE;
                break;
            default:
                grade = F_TEXTURE;
                break;
        }
        sb.setColor(SCORE_LABEL_COLOR);
        sb.draw(grade, GRADE_X, GRADE_Y, width, GRADE_SIZE);
    }

    public void renderCombo(SpriteBatch sb) {
        AbstractPlayer player = AbstractDungeon.player;
        Integer actualCombo = ComboScoreValue.currentCombo.get(player);
        if (actualCombo.equals(0)) {
            fadeOutMult();
        }
        if (ComboScoreValue.currentScore.get(player) == 0 && actualCombo == 0) {
            currentCombo = 0;
        } else if (!actualCombo.equals(currentCombo)) {
            int diff = actualCombo - currentCombo;
            currentCombo += (diff > 0 ? Math.max(1, diff / 20) : Math.min(-1, diff / 20));
            // todo: change color and grow
        }
        Integer actualMult = ComboScoreValue.currentMultiplier.get(player);
        if (!actualMult.equals(currentMult)) {
            currentMult = actualMult;
            // todo: flash
        }
        int magnitude = currentCombo.toString().length();
        Integer actualCardCount = ComboScoreValue.currentCardCount.get(player);
        Integer currentCount = actualCardCount % ComboScoreValue.cardCountMult.get(player);
        FontHelper.renderFontLeft(sb, largeDialogOptionFont, currentCombo.toString(), SCORES_X, COMBO_Y, SCORE_LABEL_COLOR);
        FontHelper.renderFontLeft(sb, largeDialogOptionFont, "×" + currentMult.toString(), SCORES_X + magnitude * DIGIT_WIDTH, COMBO_Y, MULT_LABEL_COLOR);
        FontHelper.renderFontLeft(sb, smallDialogOptionFont, currentCount.toString(), SCORES_X + (magnitude + 2) * DIGIT_WIDTH, COMBO_Y + CARD_COUNT_OFFSET_Y, MULT_LABEL_COLOR);
    }
}
