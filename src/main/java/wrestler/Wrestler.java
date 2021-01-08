package wrestler;

import basemod.BaseMod;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.ReflectionHacks;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.audio.Sfx;
import com.megacrit.cardcrawl.audio.SoundMaster;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.beyond.AwakenedOne;
import com.megacrit.cardcrawl.rewards.RewardSave;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.clapper.util.classutil.*;
import wrestler.cards.AbstractWrestlerCard;
import wrestler.characters.TheWrestler;
import wrestler.patches.ComboPatch.ComboScoreValue;
import wrestler.patches.ComboRewardsPatch;
import wrestler.potions.PlaceholderPotion;
import wrestler.powers.FlourishPower;
import wrestler.powers.PizzazzPower;
import wrestler.relics.ComboCountOneRelic;
import wrestler.relics.MultiGrappleRelic;
import wrestler.relics.ForgetAtStartRelic;
import wrestler.rewards.ComboGoldReward;
import wrestler.ui.ComboUI;
import wrestler.util.IDCheckDontTouchPls;
import wrestler.util.TextureLoader;
import wrestler.variables.Grapple;
import wrestler.variables.PsychicDamage;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Properties;

import static wrestler.characters.TheWrestler.Enums.WRESTLER;
import static wrestler.patches.PsychicDamagePatch.PSYCHIC_SOUND;

//TODO: DON'T MASS RENAME/REFACTOR
//TODO: DON'T MASS RENAME/REFACTOR
//TODO: DON'T MASS RENAME/REFACTOR
//TODO: DON'T MASS RENAME/REFACTOR
// Please don't just mass replace "wrestler" with "yourMod" everywhere.
// It'll be a bigger pain for you. You only need to replace it in 3 places.
// I comment those places below, under the place where you set your ID.

//TODO: FIRST THINGS FIRST: RENAME YOUR PACKAGE AND ID NAMES FIRST-THING!!!
// Right click the package (Open the project pane on the left. Folder with black dot on it. The name's at the very top) -> Refactor -> Rename, and name it whatever you wanna call your mod.
// Scroll down in this file. Change the ID from "wrestler:" to "yourModName:" or whatever your heart desires (don't use spaces). Dw, you'll see it.
// In the JSON strings (resources>localization>eng>[all them files] make sure they all go "yourModName:" rather than "wrestler". You can ctrl+R to replace in 1 file, or ctrl+shift+r to mass replace in specific files/directories (Be careful.).
// Start with the DefaultCommon cards - they are the most commented cards since I don't feel it's necessary to put identical comments on every card.
// After you sorta get the hang of how to make cards, check out the card template which will make your life easier

/*
 * With that out of the way:
 * Welcome to this super over-commented Slay the Spire modding base.
 * Use it to make your own mod of any type. - If you want to add any standard in-game content (character,
 * cards, relics), this is a good starting point.
 * It features 1 character with a minimal set of things: 1 card of each type, 1 debuff, couple of relics, etc.
 * If you're new to modding, you basically *need* the BaseMod wiki for whatever you wish to add
 * https://github.com/daviscook477/BaseMod/wiki - work your way through with this base.
 * Feel free to use this in any way you like, of course. MIT licence applies. Happy modding!
 *
 * And pls. Read the comments.
 */

@SpireInitializer
public class Wrestler implements
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        EditCharactersSubscriber,
        OnCardUseSubscriber,
        OnStartBattleSubscriber,
        PostBattleSubscriber,
        PostInitializeSubscriber,
        PreRoomRenderSubscriber,
        PreStartGameSubscriber {
    // Make sure to implement the subscribers *you* are using (read basemod wiki). Editing cards? EditCardsSubscriber.
    // Making relics? EditRelicsSubscriber. etc., etc., for a full list and how to make your own, visit the basemod wiki.
    public static final Logger logger = LogManager.getLogger(Wrestler.class.getName());
    private static String modID;

    // Mod-settings settings. This is if you want an on/off savable button
    public static Properties wrestlerDefaultSettings = new Properties();
    public static final String ENABLE_PLACEHOLDER_SETTINGS = "enablePlaceholder";
    public static boolean enablePlaceholder = true; // The boolean we'll be setting on/off (true/false)

    //This is for the in-game mod settings panel.
    private static final String MODNAME = "The Wrestler";
    private static final String AUTHOR = "Tim Sims";
    private static final String DESCRIPTION = "A Wrestler from a far city, banished for using mind altering moves.";
    
    // =============== INPUT TEXTURE LOCATION =================
    
    // Colors (RGB)
    // Character Color
    public static final Color INDIGO_WRESTLER = CardHelper.getColor(17.0f, 17.0f, 52.0f);
    
    // Potion Colors in RGB
    public static final Color PLACEHOLDER_POTION_LIQUID = CardHelper.getColor(209.0f, 53.0f, 18.0f); // Orange-ish Red
    public static final Color PLACEHOLDER_POTION_HYBRID = CardHelper.getColor(255.0f, 230.0f, 230.0f); // Near White
    public static final Color PLACEHOLDER_POTION_SPOTS = CardHelper.getColor(100.0f, 25.0f, 10.0f); // Super Dark Red/Brown
    
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
  
    // Card backgrounds - The actual rectangular card.
    private static final String ATTACK_WRESTLER_INDIGO = "wrestlerResources/images/512/bg_attack_wrestler_512.png";
    private static final String SKILL_WRESTLER_INDIGO = "wrestlerResources/images/512/bg_skill_wrestler_512.png";
    private static final String POWER_WRESTLER_INDIGO = "wrestlerResources/images/512/bg_power_wrestler_512.png";
    
    private static final String ENERGY_ORB_WRESTLER_INDIGO = "wrestlerResources/images/512/card_wrestler_orb_512.png";
    private static final String CARD_ENERGY_ORB = "wrestlerResources/images/512/card_small_orb.png";
    
    private static final String ATTACK_WRESTLER_INDIGO_PORTRAIT = "wrestlerResources/images/1024/bg_attack_wrestler.png";
    private static final String SKILL_WRESTLER_INDIGO_PORTRAIT = "wrestlerResources/images/1024/bg_skill_wrestler.png";
    private static final String POWER_WRESTLER_INDIGO_PORTRAIT = "wrestlerResources/images/1024/bg_power_wrestler.png";
    private static final String ENERGY_ORB_WRESTLER_INDIGO_PORTRAIT = "wrestlerResources/images/1024/card_wrestler_orb.png";
    
    // Character assets
    private static final String WRESTLER_BUTTON = "wrestlerResources/images/charSelect/wrestlerButton.png";
    private static final String WRESTLER_PORTRAIT = "wrestlerResources/images/charSelect/wrestlerPortraitBG.png";
    public static final String WRESTLER_SHOULDER_1 = "wrestlerResources/images/char/wrestler/shoulder.png";
    public static final String WRESTLER_SHOULDER_2 = "wrestlerResources/images/char/wrestler/shoulder2.png";
    public static final String WRESTLER_CORPSE = "wrestlerResources/images/char/wrestler/corpse.png";
    
    //Mod Badge - A small icon that appears in the mod settings menu next to your mod.
    public static final String BADGE_IMAGE = "wrestlerResources/images/Badge.png";
    
    // Atlas and JSON files for the Animations
    public static final String WRESTLER_SKELETON_ATLAS = "wrestlerResources/images/char/wrestler/skeleton.atlas";
    public static final String WRESTLER_SKELETON_JSON = "wrestlerResources/images/char/wrestler/skeleton.json";
    
    // =============== MAKE IMAGE PATHS =================
    
    public static String makeCardPath(String resourcePath) {
        return getModID() + "Resources/images/cards/" + resourcePath;
    }
    
    public static String makeRelicPath(String resourcePath) {
        return getModID() + "Resources/images/relics/" + resourcePath;
    }
    
    public static String makeRelicOutlinePath(String resourcePath) {
        return getModID() + "Resources/images/relics/outline/" + resourcePath;
    }
    
    public static String makeOrbPath(String resourcePath) {
        return getModID() + "Resources/orbs/" + resourcePath;
    }
    
    public static String makePowerPath(String resourcePath) {
        return getModID() + "Resources/images/powers/" + resourcePath;
    }
    
    public static String makeEventPath(String resourcePath) {
        return getModID() + "Resources/images/events/" + resourcePath;
    }
    
    // =============== /MAKE IMAGE PATHS/ =================
    
    // =============== /INPUT TEXTURE LOCATION/ =================
    
    
    // =============== SUBSCRIBE, CREATE THE COLOR_GRAY, INITIALIZE =================
    
    public Wrestler() {
        logger.info("Subscribe to BaseMod hooks");
        
        BaseMod.subscribe(this);
      
        setModID("wrestler");
        // cool
        // TODO: NOW READ THIS!!!!!!!!!!!!!!!:
        
        // 1. Go to your resources folder in the project panel, and refactor> rename wrestlerResources to
        // yourModIDResources.
        
        // 2. Click on the localization > eng folder and press ctrl+shift+r, then select "Directory" (rather than in Project)
        // replace all instances of wrestler with yourModID.
        // Because your mod ID isn't the default. Your cards (and everything else) should have Your mod id. Not mine.
        
        // 3. FINALLY and most importantly: Scroll up a bit. You may have noticed the image locations above don't use getModID()
        // Change their locations to reflect your actual ID rather than wrestler. They get loaded before getID is a thing.
        
        logger.info("Done subscribing");

        logger.info("Creating the color " + TheWrestler.Enums.COLOR_INDIGO.toString());

        BaseMod.addColor(TheWrestler.Enums.COLOR_INDIGO, INDIGO_WRESTLER, INDIGO_WRESTLER, INDIGO_WRESTLER,
                INDIGO_WRESTLER, INDIGO_WRESTLER, INDIGO_WRESTLER, INDIGO_WRESTLER,
                ATTACK_WRESTLER_INDIGO, SKILL_WRESTLER_INDIGO, POWER_WRESTLER_INDIGO, ENERGY_ORB_WRESTLER_INDIGO,
                ATTACK_WRESTLER_INDIGO_PORTRAIT, SKILL_WRESTLER_INDIGO_PORTRAIT, POWER_WRESTLER_INDIGO_PORTRAIT,
                ENERGY_ORB_WRESTLER_INDIGO_PORTRAIT, CARD_ENERGY_ORB);
        
        logger.info("Done creating the color");
        
        logger.info("Adding mod settings");
        // This loads the mod settings.
        // The actual mod Button is added below in receivePostInitialize()
        wrestlerDefaultSettings.setProperty(ENABLE_PLACEHOLDER_SETTINGS, "FALSE"); // This is the default setting. It's actually set...
        try {
            SpireConfig config = new SpireConfig("TheWrestler", "wrestlerConfig", wrestlerDefaultSettings); // ...right here
            // the "fileName" parameter is the name of the file MTS will create where it will save our setting.
            config.load(); // Load the setting and set the boolean to equal it
            enablePlaceholder = config.getBool(ENABLE_PLACEHOLDER_SETTINGS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("Done adding mod settings");
        
    }
    
    // ====== NO EDIT AREA ======
    // DON'T TOUCH THIS STUFF. IT IS HERE FOR STANDARDIZATION BETWEEN MODS AND TO ENSURE GOOD CODE PRACTICES.
    // IF YOU MODIFY THIS I WILL HUNT YOU DOWN AND DOWNVOTE YOUR MOD ON WORKSHOP
    
    public static void setModID(String ID) { // DON'T EDIT
        Gson coolG = new Gson(); // EY DON'T EDIT THIS
        //   String IDjson = Gdx.files.internal("IDCheckStringsDONT-EDIT-AT-ALL.json").readString(String.valueOf(StandardCharsets.UTF_8)); // i hate u Gdx.files
        InputStream in = Wrestler.class.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json"); // DON'T EDIT THIS ETHER
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), IDCheckDontTouchPls.class); // OR THIS, DON'T EDIT IT
        logger.info("You are attempting to set your mod ID as: " + ID); // NO WHY
        if (ID.equals(EXCEPTION_STRINGS.DEFAULTID)) { // DO *NOT* CHANGE THIS ESPECIALLY, TO EDIT YOUR MOD ID, SCROLL UP JUST A LITTLE, IT'S JUST ABOVE
            throw new RuntimeException(EXCEPTION_STRINGS.EXCEPTION); // THIS ALSO DON'T EDIT
        } else if (ID.equals(EXCEPTION_STRINGS.DEVID)) { // NO
            modID = EXCEPTION_STRINGS.DEFAULTID; // DON'T
        } else { // NO EDIT AREA
            modID = ID; // DON'T WRITE OR CHANGE THINGS HERE NOT EVEN A LITTLE
        } // NO
        logger.info("Success! ID is " + modID); // WHY WOULD U WANT IT NOT TO LOG?? DON'T EDIT THIS.
    } // NO
    
    public static String getModID() { // NO
        return modID; // DOUBLE NO
    } // NU-UH
    
    private static void pathCheck() { // ALSO NO
        Gson coolG = new Gson(); // NNOPE DON'T EDIT THIS
        //   String IDjson = Gdx.files.internal("IDCheckStringsDONT-EDIT-AT-ALL.json").readString(String.valueOf(StandardCharsets.UTF_8)); // i still hate u btw Gdx.files
        InputStream in = Wrestler.class.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json"); // DON'T EDIT THISSSSS
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), IDCheckDontTouchPls.class); // NAH, NO EDIT
        String packageName = Wrestler.class.getPackage().getName(); // STILL NO EDIT ZONE
        FileHandle resourcePathExists = Gdx.files.internal(getModID() + "Resources"); // PLEASE DON'T EDIT THINGS HERE, THANKS
        if (!modID.equals(EXCEPTION_STRINGS.DEVID)) { // LEAVE THIS EDIT-LESS
            if (!packageName.equals(getModID())) { // NOT HERE ETHER
                throw new RuntimeException(EXCEPTION_STRINGS.PACKAGE_EXCEPTION + getModID()); // THIS IS A NO-NO
            } // WHY WOULD U EDIT THIS
            if (!resourcePathExists.exists()) { // DON'T CHANGE THIS
                throw new RuntimeException(EXCEPTION_STRINGS.RESOURCE_FOLDER_EXCEPTION + getModID() + "Resources"); // NOT THIS
            }// NO
        }// NO
    }// NO
    
    // ====== YOU CAN EDIT AGAIN ======
    
    
    @SuppressWarnings("unused")
    public static void initialize() {
        logger.info("========================= Initializing Wrestler Mod. Hi. =========================");
        Wrestler defaultmod = new Wrestler();
        logger.info("========================= /Wrestler Mod Initialized. Hello World./ =========================");
    }
    
    // ============== /SUBSCRIBE, CREATE THE COLOR_GRAY, INITIALIZE/ =================
    
    
    // =============== LOAD THE CHARACTER =================
    
    @Override
    public void receiveEditCharacters() {
        logger.info("Beginning to edit characters. " + "Add " + WRESTLER.toString());
        
        BaseMod.addCharacter(new TheWrestler("The Wrestler", WRESTLER),
                WRESTLER_BUTTON, WRESTLER_PORTRAIT, WRESTLER);
        
        receiveEditPotions();
        logger.info("Added " + WRESTLER.toString());
    }
    
    // =============== /LOAD THE CHARACTER/ =================
    
    
    // =============== POST-INITIALIZE =================
    
    @Override
    public void receivePostInitialize() {
        logger.info("Loading badge image and mod options");
        
        // Load the Mod Badge
        Texture badgeTexture = TextureLoader.getTexture(BADGE_IMAGE);
        
        // Create the Mod Menu
        ModPanel settingsPanel = new ModPanel();
        
        // Create the on/off button:
        ModLabeledToggleButton enableNormalsButton = new ModLabeledToggleButton("This is the text which goes next to the checkbox.",
                350.0f, 700.0f, Settings.CREAM_COLOR, FontHelper.charDescFont, // Position (trial and error it), color, font
                enablePlaceholder, // Boolean it uses
                settingsPanel, // The mod panel in which this button will be in
                (label) -> {}, // thing??????? idk
                (button) -> { // The actual button:
            
            enablePlaceholder = button.enabled; // The boolean true/false will be whether the button is enabled or not
            try {
                // And based on that boolean, set the settings and save them
                SpireConfig config = new SpireConfig("defaultMod", "wrestlerConfig", wrestlerDefaultSettings);
                config.setBool(ENABLE_PLACEHOLDER_SETTINGS, enablePlaceholder);
                config.save();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        
        settingsPanel.addUIElement(enableNormalsButton); // Add the button to the settings panel. Button is a go.
        
        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);

        
        // =============== EVENTS =================
        
        // This event will be exclusive to the City (act 2). If you want an event that's present at any
        // part of the game, simply don't include the dungeon ID
        // If you want to have a character-specific event, look at slimebound (CityRemoveEventPatch).
        // Essentially, you need to patch the game and say "if a player is not playing my character class, remove the event from the pool"

        // =============== /EVENTS/ =================

        // Setting up Combo
        comboUI = new ComboUI();
        BaseMod.registerCustomReward(
                ComboRewardsPatch.WRESTLER_COMBO_GOLD,
                (rewardSave) -> {
                    return ComboGoldReward.addComboGoldToRewards(rewardSave.amount);
                },
                (customReward) -> {
                    return new RewardSave(customReward.type.toString(), null, customReward.bonusGold, 0);
                });

        // Load Audio
        HashMap<String, Sfx> map = (HashMap<String, Sfx>) ReflectionHacks.getPrivate(CardCrawlGame.sound, SoundMaster.class, "map");
        map.put(PSYCHIC_SOUND, new Sfx("wrestlerResources/audio/PsychicAttack.ogg", false));

//        BaseMod.addAudio(PSYCHIC_SOUND, "wrestlerResources/audio/PsychicAttack.ogg");

        logger.info("Done loading badge Image and mod options");
    }
    
    // =============== / POST-INITIALIZE/ =================
    
    
    // ================ ADD POTIONS ===================
    
    public void receiveEditPotions() {
        logger.info("Beginning to edit potions");
        
        // Class Specific Potion. If you want your potion to not be class-specific,
        // just remove the player class at the end (in this case the "wrestlerEnum.WRESTLER".
        // Remember, you can press ctrl+P inside parentheses like addPotions)
        BaseMod.addPotion(PlaceholderPotion.class, PLACEHOLDER_POTION_LIQUID, PLACEHOLDER_POTION_HYBRID, PLACEHOLDER_POTION_SPOTS, PlaceholderPotion.POTION_ID, WRESTLER);
        
        logger.info("Done editing potions");
    }
    
    // ================ /ADD POTIONS/ ===================
    
    
    // ================ ADD RELICS ===================
    
    @Override
    public void receiveEditRelics() {
        logger.info("Adding relics");

        // Add Wrestler Relics
        // Starter
        BaseMod.addRelicToCustomPool(new ComboCountOneRelic(), TheWrestler.Enums.COLOR_INDIGO);
        UnlockTracker.markRelicAsSeen(ComboCountOneRelic.ID);
        // Uncommon
        BaseMod.addRelicToCustomPool(new MultiGrappleRelic(), TheWrestler.Enums.COLOR_INDIGO);
        UnlockTracker.markRelicAsSeen(MultiGrappleRelic.ID);
        // Rare
        BaseMod.addRelicToCustomPool(new ForgetAtStartRelic(), TheWrestler.Enums.COLOR_INDIGO);
        UnlockTracker.markRelicAsSeen(ForgetAtStartRelic.ID);

        logger.info("Done adding relics!");
    }
    
    // ================ /ADD RELICS/ ===================
    
    
    // ================ ADD CARDS ===================

    @Override
    public void receiveEditCards() {
        //Ignore this
        pathCheck();
        // Add the Custom Dynamic Variables
        logger.info("Adding variables...");
        // Add the Custom Dynamic variables
        BaseMod.addDynamicVariable(new Grapple());
        BaseMod.addDynamicVariable(new PsychicDamage());

        // Add the cards
        try {
            autoAddCards();
        } catch (Exception e) {
            logger.error("Error adding cards: " + e.getMessage());
        }
        
        logger.info("Done adding cards.");
    }

    // Copied this from Alchyr who copied it from kiooeht.
    private static void autoAddCards() throws URISyntaxException, IllegalAccessException, InstantiationException, NotFoundException, CannotCompileException, ClassNotFoundException {
        logger.info("Detecting and adding cards...");
        ClassFinder finder = new ClassFinder();
        URL url = Wrestler.class.getProtectionDomain().getCodeSource().getLocation();
        finder.add(new File(url.toURI()));

        ClassFilter filter =
                new AndClassFilter(
                        new NotClassFilter(new InterfaceOnlyClassFilter()),
                        new NotClassFilter(new AbstractClassFilter()),
                        new ClassModifiersClassFilter(Modifier.PUBLIC),
                        new CardFilter()
                );
        Collection<ClassInfo> foundClasses = new ArrayList<>();
        finder.findClasses(foundClasses, filter);

        for (ClassInfo classInfo : foundClasses) {
            CtClass cls = Loader.getClassPool().get(classInfo.getClassName());

            boolean isCard = false;
            CtClass superCls = cls;
            while (superCls != null) {
                superCls = superCls.getSuperclass();
                if (superCls == null) {
                    break;
                }
                if (superCls.getName().equals(AbstractWrestlerCard.class.getName())) {
                    isCard = true;
                    break;
                }
            }
            if (!isCard) {
                continue;
            }

            AbstractCard card = (AbstractCard) Loader.getClassPool().getClassLoader().loadClass(cls.getName()).newInstance();

            BaseMod.addCard(card);

            UnlockTracker.unlockCard(card.cardID);
        }
    }

    private static class CardFilter implements ClassFilter {
        private static final String PACKAGE = "wrestler.cards";

        @Override
        public boolean accept(ClassInfo classInfo, ClassFinder classFinder) {
            return classInfo.getClassName().startsWith(PACKAGE);
        }
    }
    
    // ================ /ADD CARDS/ ===================
    
    
    // ================ LOAD THE TEXT ===================
    
    @Override
    public void receiveEditStrings() {
        logger.info("You seeing this?");
        logger.info("Beginning to edit strings for mod with ID: " + getModID());
        
        // CardStrings
        BaseMod.loadCustomStringsFile(CardStrings.class,
                getModID() + "Resources/localization/eng/Wrestler-Card-Strings.json");
        
        // PowerStrings
        BaseMod.loadCustomStringsFile(PowerStrings.class,
                getModID() + "Resources/localization/eng/Wrestler-Power-Strings.json");
        
        // RelicStrings
        BaseMod.loadCustomStringsFile(RelicStrings.class,
                getModID() + "Resources/localization/eng/Wrestler-Relic-Strings.json");
        
        // Event Strings
        BaseMod.loadCustomStringsFile(EventStrings.class,
                getModID() + "Resources/localization/eng/Wrestler-Event-Strings.json");
        
        // PotionStrings
        BaseMod.loadCustomStringsFile(PotionStrings.class,
                getModID() + "Resources/localization/eng/Wrestler-Potion-Strings.json");
        
        // CharacterStrings
        BaseMod.loadCustomStringsFile(CharacterStrings.class,
                getModID() + "Resources/localization/eng/Wrestler-Character-Strings.json");
        
        // OrbStrings
        BaseMod.loadCustomStringsFile(OrbStrings.class,
                getModID() + "Resources/localization/eng/Wrestler-Orb-Strings.json");
        
        logger.info("Done editing strings");
    }
    
    // ================ /LOAD THE TEXT/ ===================
    
    // ================ LOAD THE KEYWORDS ===================
    
    @Override
    public void receiveEditKeywords() {
        // Keywords on cards are supposed to be Capitalized, while in Keyword-String.json they're lowercase
        //
        // Multiword keywords on cards are done With_Underscores
        //
        // If you're using multiword keywords, the first element in your NAMES array in your keywords-strings.json has to be the same as the PROPER_NAME.
        // That is, in Card-Strings.json you would have #yA_Long_Keyword (#y highlights the keyword in yellow).
        // In Keyword-Strings.json you would have PROPER_NAME as A Long Keyword and the first element in NAMES be a long keyword, and the second element be a_long_keyword
        
        Gson gson = new Gson();
        String json = Gdx.files.internal(getModID() + "Resources/localization/eng/Wrestler-Keyword-Strings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        com.evacipated.cardcrawl.mod.stslib.Keyword[] keywords = gson.fromJson(json, com.evacipated.cardcrawl.mod.stslib.Keyword[].class);
        
        if (keywords != null) {
            for (Keyword keyword : keywords) {
                BaseMod.addKeyword(getModID().toLowerCase(), keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
                //  getModID().toLowerCase() makes your keyword mod specific (it won't show up in other cards that use that word)
            }
        }
    }
    
    // ================ /LOAD THE KEYWORDS/ ===================    
    
    // this adds "ModName:" before the ID of any card/relic/power etc.
    // in order to avoid conflicts if any other mod uses the same ID.
    public static String makeID(String idText) {
        return getModID() + ":" + idText;
    }

//    public static String imagePath(String path) {
//        return "wrestlerResources/images" + path;
//    }
//
//    public static String languagePath(String path) {
//        return "wrestlerResources/localization" + path;
//    }

    // ================ /COMBO/ ===================

    private static ComboUI comboUI;
    private static boolean drawCombo = false;
    private static final Float TURN_MULTIPLIER_HIGH_HP = 1.2f;
    private static final Float TURN_MULTIPLIER_LOW_HP = 1.5f;

    public static void increaseMultiplier() {
        AbstractPlayer player = AbstractDungeon.player;
        ComboScoreValue.currentMultiplier.set(player, ComboScoreValue.currentMultiplier.get(player) + 1);
        if (player.hasPower(PizzazzPower.POWER_ID)) {
            ((PizzazzPower)player.getPower(PizzazzPower.POWER_ID)).activate();
        }
        if (player.hasPower(FlourishPower.POWER_ID)) {
            ((FlourishPower)player.getPower(FlourishPower.POWER_ID)).activate();
        }
    }

    public static void increaseCombo(int comboAmount) {
        AbstractPlayer player = AbstractDungeon.player;
        int currentMultiplier = ComboScoreValue.currentMultiplier.get(player);
        ComboScoreValue.currentCombo.set(player, ComboScoreValue.currentCombo.get(player) + (comboAmount * currentMultiplier));
        updateTier();
    }

    public static void breakCombo(boolean endOfBattle) {
        AbstractPlayer player = AbstractDungeon.player;
        int combo = ComboScoreValue.currentCombo.get(player);
        int score = ComboScoreValue.currentScore.get(player);
        ComboScoreValue.currentScore.set(player, score + combo);
        resetCombo(endOfBattle);
    }

    public static void resetCombo(boolean endOfBattle) {
        AbstractPlayer player = AbstractDungeon.player;
        ComboScoreValue.currentCombo.set(player, 0);
        if (!endOfBattle) {
            ComboScoreValue.currentMultiplier.set(player, 1);
            ComboScoreValue.currentCardCount.set(player, 0);
            if (player.hasRelic(ComboCountOneRelic.ID)) {
                ComboScoreValue.cardCountMult.set(player, 4);
//        } else if (player.hasRelic(ComboCountTwoRelic.ID)) {
//            ComboScoreValue.cardCountMult.set(player, 3);
            } else {
                ComboScoreValue.cardCountMult.set(player, 5);
            }
        }
    }

    public static void updateTierStartOfTurn() {
        AbstractPlayer player = AbstractDungeon.player;
        int monHP = 0;
        for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
            monHP += m.maxHealth;
            if (m.id.equals(AwakenedOne.ID)) {
                monHP += m.maxHealth;
            }
        }
        ComboScoreValue.totalHP.set(player, monHP);
        ComboScoreValue.currentTurn.set(player, ComboScoreValue.currentTurn.get(player) + 1);
        updateTier();
    }

    public static void updateTier() {
        AbstractPlayer player = AbstractDungeon.player;
        int totalHP = ComboScoreValue.totalHP.get(player);
        int turnNum = ComboScoreValue.currentTurn.get(player);
        int currentTotal = ComboScoreValue.currentScore.get(player) + ComboScoreValue.currentCombo.get(player);

        float SSS, SS, S, A, B, C, D;
        boolean highHP = totalHP > 100;

        if (highHP) {
            SSS = totalHP * 2.0f;
            SS = totalHP * 1.5f;
            S = totalHP * 1.25f;
            A = totalHP;
            B = totalHP * 0.75f;
            C = turnNum > 0 ? totalHP * 0.5f : 0.0f;
            D = turnNum > 1 ? totalHP * 0.25f : 0.0f;
        } else {
            SSS = totalHP * 1.2f;
            SS = totalHP;
            S = totalHP * 0.8f;
            A = totalHP * 0.6f;
            B = totalHP * 0.4f;
            C = turnNum > 0 ? totalHP * 0.3f : 0.0f;
            D = turnNum > 1 ? totalHP * 0.2f : 0.0f;
        }
        for (int i = 0; i < turnNum; i++) {
            SSS *= highHP ? TURN_MULTIPLIER_HIGH_HP : TURN_MULTIPLIER_LOW_HP;
            SS *= highHP ? TURN_MULTIPLIER_HIGH_HP : TURN_MULTIPLIER_LOW_HP;
            S *= highHP ? TURN_MULTIPLIER_HIGH_HP : TURN_MULTIPLIER_LOW_HP;
            A *= highHP ? TURN_MULTIPLIER_HIGH_HP : TURN_MULTIPLIER_LOW_HP;
            B *= highHP ? TURN_MULTIPLIER_HIGH_HP : TURN_MULTIPLIER_LOW_HP;
            C *= highHP ? TURN_MULTIPLIER_HIGH_HP : TURN_MULTIPLIER_LOW_HP;
            D *= highHP ? TURN_MULTIPLIER_HIGH_HP : TURN_MULTIPLIER_LOW_HP;
        }
        int tier = 0;
        if (currentTotal >= SSS) {
            tier = 7;
        } else if (currentTotal >= SS) {
            tier = 6;
        } else if (currentTotal >= S) {
            tier = 5;
        } else if (currentTotal >= A) {
            tier = 4;
        } else if (currentTotal >= B) {
            tier = 3;
        } else if (currentTotal >= C) {
            tier = 2;
        } else if (currentTotal >= D) {
            tier = 1;
        }
        ComboScoreValue.currentTier.set(player, tier);
    }

    @Override
    public void receiveCardUsed(AbstractCard abstractCard) {
        AbstractPlayer player = AbstractDungeon.player;
        int actualCardCount = ComboScoreValue.currentCardCount.get(player) + 1;
        ComboScoreValue.currentCardCount.set(player, actualCardCount);
        int cardCountMult = ComboScoreValue.cardCountMult.get(player);
        int currentCardCount = actualCardCount % cardCountMult;
        if (currentCardCount == 0) {
            if (player.hasRelic(ComboCountOneRelic.ID)) {
                player.getRelic(ComboCountOneRelic.ID).flash();
//            } else if (player.hasRelic(ComboCountTwoRelic.ID)) {
//                player.getRelic(ComboCountTwoRelic.ID).flash();
            }
            increaseMultiplier();
        }
        increaseCombo(1);
    }

    @Override
    public void receivePreStartGame() {
        comboUI.hide();
        drawCombo = false;
    }

    @Override
    public void receivePreRoomRender(SpriteBatch spriteBatch) {
        if (AbstractDungeon.getCurrRoom() != null && drawCombo && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            comboUI.render(spriteBatch, false);
        } else {
            comboUI.render(spriteBatch, true);
        }
    }

    @Override
    public void receiveOnBattleStart(AbstractRoom abstractRoom) {
        AbstractPlayer player = AbstractDungeon.player;
        ComboScoreValue.currentScore.set(player, 0);
        ComboScoreValue.currentTier.set(player, 2);
        ComboScoreValue.currentTurn.set(player, -1);
        resetCombo(false);
        updateTierStartOfTurn();
        if (player.chosenClass == WRESTLER || player.hasRelic("PrismaticShard")) {
            drawCombo = true;
        }
    }

    @Override
    public void receivePostBattle(AbstractRoom room) {
        AbstractPlayer player = AbstractDungeon.player;

        breakCombo(true);

        // Combo Rewards
        int tier = ComboScoreValue.currentTier.get(player);
        AbstractDungeon.getCurrRoom().rewards.add(ComboGoldReward.addComboGoldToRewards(tier));
    }
}
