package wrestler;

import basemod.BaseMod;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.rewards.RewardSave;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import wrestler.cards.indigo.*;
import wrestler.characters.TheWrestler;
import wrestler.patches.ComboRewardsPatch;
import wrestler.potions.PlaceholderPotion;
import wrestler.powers.ComboPower;
import wrestler.relics.MultiGrappleRelic;
import wrestler.relics.VoidMaskRelic;
import wrestler.rewards.ComboCardReward;
import wrestler.rewards.ComboGoldReward;
import wrestler.util.IDCheckDontTouchPls;
import wrestler.util.TextureLoader;
import wrestler.variables.Grapple;
import wrestler.variables.PsychicDamage;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

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
        PostBattleSubscriber,
        PostInitializeSubscriber {
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
            SpireConfig config = new SpireConfig("defaultMod", "wrestlerConfig", wrestlerDefaultSettings); // ...right here
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
        logger.info("Beginning to edit characters. " + "Add " + TheWrestler.Enums.WRESTLER.toString());
        
        BaseMod.addCharacter(new TheWrestler("The Wrestler", TheWrestler.Enums.WRESTLER),
                WRESTLER_BUTTON, WRESTLER_PORTRAIT, TheWrestler.Enums.WRESTLER);
        
        receiveEditPotions();
        logger.info("Added " + TheWrestler.Enums.WRESTLER.toString());
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

        // Registering Combo rewards
        BaseMod.registerCustomReward(
                ComboRewardsPatch.WRESTLER_COMBO_CARD,
                (rewardSave) -> {
                    return new wrestler.rewards.ComboCardReward();
                },
                (customReward) -> {
                    return new RewardSave(customReward.type.toString(), null, 0, 0);
                });
        BaseMod.registerCustomReward(
                ComboRewardsPatch.WRESTLER_COMBO_GOLD,
                (rewardSave) -> {
                    return new wrestler.rewards.ComboGoldReward(rewardSave.amount);
                },
                (customReward) -> {
                    return new RewardSave(customReward.type.toString(), null, ((ComboGoldReward)customReward).goldAmt, 0);
                });
        logger.info("Done loading badge Image and mod options");
    }
    
    // =============== / POST-INITIALIZE/ =================
    
    
    // ================ ADD POTIONS ===================
    
    public void receiveEditPotions() {
        logger.info("Beginning to edit potions");
        
        // Class Specific Potion. If you want your potion to not be class-specific,
        // just remove the player class at the end (in this case the "wrestlerEnum.WRESTLER".
        // Remember, you can press ctrl+P inside parentheses like addPotions)
        BaseMod.addPotion(PlaceholderPotion.class, PLACEHOLDER_POTION_LIQUID, PLACEHOLDER_POTION_HYBRID, PLACEHOLDER_POTION_SPOTS, PlaceholderPotion.POTION_ID, TheWrestler.Enums.WRESTLER);
        
        logger.info("Done editing potions");
    }
    
    // ================ /ADD POTIONS/ ===================
    
    
    // ================ ADD RELICS ===================
    
    @Override
    public void receiveEditRelics() {
        logger.info("Adding relics");

        // Add Wrestler Relics
        // Starter
        BaseMod.addRelicToCustomPool(new VoidMaskRelic(), TheWrestler.Enums.COLOR_INDIGO);
        UnlockTracker.markRelicAsSeen(VoidMaskRelic.ID);
        // Uncommon
        BaseMod.addRelicToCustomPool(new MultiGrappleRelic(), TheWrestler.Enums.COLOR_INDIGO);
        UnlockTracker.markRelicAsSeen(MultiGrappleRelic.ID);

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
        // Don't comment out/delete these cards (yet). You need 1 of each type and rarity (technically) for your game not to crash
        // when generating card rewards/shop screen items.

        basicCards();
        commonCards();
        uncommonCards();
        rareCards();
        
        logger.info("Done adding cards.");
    }

    public void basicCards() {
        logger.info("Adding Basic cards...");
        BaseMod.addCard(new Defend_Wrestler());
        UnlockTracker.unlockCard(Defend_Wrestler.ID);
        BaseMod.addCard(new Grab_Wrestler());
        UnlockTracker.unlockCard(Grab_Wrestler.ID);
        BaseMod.addCard(new HipThrow_Wrestler());
        UnlockTracker.unlockCard(HipThrow_Wrestler.ID);
        BaseMod.addCard(new Strike_Wrestler());
        UnlockTracker.unlockCard(Strike_Wrestler.ID);
    }

    public void commonCards() {
        logger.info("Adding Common cards...");
        BaseMod.addCard(new CarefulApproach_Wrestler());
        UnlockTracker.unlockCard(CarefulApproach_Wrestler.ID);
        BaseMod.addCard(new CharmingWords_Wrestler());
        UnlockTracker.unlockCard(CharmingWords_Wrestler.ID);
        BaseMod.addCard(new CheapShot_Wrestler());
        UnlockTracker.unlockCard(CheapShot_Wrestler.ID);
        BaseMod.addCard(new ElbowCut_Wrestler());
        UnlockTracker.unlockCard(ElbowCut_Wrestler.ID);
        BaseMod.addCard(new HalfNelson_Wrestler());
        UnlockTracker.unlockCard(HalfNelson_Wrestler.ID);
        BaseMod.addCard(new HammerStrike_Wrestler());
        UnlockTracker.unlockCard(HammerStrike_Wrestler.ID);
        BaseMod.addCard(new Jab_Wrestler());
        UnlockTracker.unlockCard(Jab_Wrestler.ID);
        BaseMod.addCard(new Melee_Wrestler());
        UnlockTracker.unlockCard(Melee_Wrestler.ID);
        BaseMod.addCard(new MindSpike_Wrestler());
        UnlockTracker.unlockCard(MindSpike_Wrestler.ID);
        BaseMod.addCard(new OneTwoStrike_Wrestler());
        UnlockTracker.unlockCard(OneTwoStrike_Wrestler.ID);
        BaseMod.addCard(new PenetratingShot_Wrestler());
        UnlockTracker.unlockCard(PenetratingShot_Wrestler.ID);
        BaseMod.addCard(new Scramble_Wrestler());
        UnlockTracker.unlockCard(Scramble_Wrestler.ID);
        BaseMod.addCard(new Sidestep_Wrestler());
        UnlockTracker.unlockCard(Sidestep_Wrestler.ID);
        BaseMod.addCard(new Suplex_Wrestler());
        UnlockTracker.unlockCard(Suplex_Wrestler.ID);
        BaseMod.addCard(new VitalityDrain_Wrestler());
        UnlockTracker.unlockCard(VitalityDrain_Wrestler.ID);
        BaseMod.addCard(new EldritchBlast_Wrestler());
        UnlockTracker.unlockCard(EldritchBlast_Wrestler.ID);
        BaseMod.addCard(new Whispers_Wrestler());
        UnlockTracker.unlockCard(Whispers_Wrestler.ID);
        BaseMod.addCard(new WindUp_Wrestler());
        UnlockTracker.unlockCard(WindUp_Wrestler.ID);
    }

    public void uncommonCards() {
        logger.info("Adding Uncommon cards...");
        BaseMod.addCard(new ArmBar_Wrestler());
        UnlockTracker.unlockCard(ArmBar_Wrestler.ID);
        BaseMod.addCard(new BearHug_Wrestler());
        UnlockTracker.unlockCard(BearHug_Wrestler.ID);
        BaseMod.addCard(new BodyShield_Wrestler());
        UnlockTracker.unlockCard(BodyShield_Wrestler.ID);
        BaseMod.addCard(new Brainstorm_Wrestler());
        UnlockTracker.unlockCard(Brainstorm_Wrestler.ID);
        BaseMod.addCard(new ChillTouch_Wrestler());
        UnlockTracker.unlockCard(ChillTouch_Wrestler.ID);
        BaseMod.addCard(new Chokehold_Wrestler());
        UnlockTracker.unlockCard(Chokehold_Wrestler.ID);
        BaseMod.addCard(new CloseQuarters_Wrestler());
        UnlockTracker.unlockCard(CloseQuarters_Wrestler.ID);
        BaseMod.addCard(new CrowdPleaser_Wrestler());
        UnlockTracker.unlockCard(CrowdPleaser_Wrestler.ID);
        BaseMod.addCard(new DarkSuggestion_Wrestler());
        UnlockTracker.unlockCard(DarkSuggestion_Wrestler.ID);
        BaseMod.addCard(new InvisibleHand_Wrestler());
        UnlockTracker.unlockCard(InvisibleHand_Wrestler.ID);
        BaseMod.addCard(new Flourish_Wrestler());
        UnlockTracker.unlockCard(Flourish_Wrestler.ID);
        BaseMod.addCard(new GetReady_Wrestler());
        UnlockTracker.unlockCard(GetReady_Wrestler.ID);
        BaseMod.addCard(new GetSet_Wrestler());
        UnlockTracker.unlockCard(GetSet_Wrestler.ID);
        BaseMod.addCard(new Go_Wrestler());
        UnlockTracker.unlockCard(Go_Wrestler.ID);
        BaseMod.addCard(new Lariat_Wrestler());
        UnlockTracker.unlockCard(Lariat_Wrestler.ID);
        BaseMod.addCard(new Migraine_Wrestler());
        UnlockTracker.unlockCard(Migraine_Wrestler.ID);
        BaseMod.addCard(new MindRead_Wrestler());
        UnlockTracker.unlockCard(MindRead_Wrestler.ID);
        BaseMod.addCard(new ModifyMemory_Wrestler());
        UnlockTracker.unlockCard(ModifyMemory_Wrestler.ID);
        BaseMod.addCard(new MountainBomb_Wrestler());
        UnlockTracker.unlockCard(MountainBomb_Wrestler.ID);
        BaseMod.addCard(new OutOfReach_Wrestler());
        UnlockTracker.unlockCard(OutOfReach_Wrestler.ID);
        BaseMod.addCard(new Piledriver_Wrestler());
        UnlockTracker.unlockCard(Piledriver_Wrestler.ID);
        BaseMod.addCard(new Pounce_Wrestler());
        UnlockTracker.unlockCard(Pounce_Wrestler.ID);
        BaseMod.addCard(new Roundhouse_Wrestler());
        UnlockTracker.unlockCard(Roundhouse_Wrestler.ID);
        BaseMod.addCard(new ShockingGrasp_Wrestler());
        UnlockTracker.unlockCard(ShockingGrasp_Wrestler.ID);
        BaseMod.addCard(new Roundhouse_Wrestler());
        UnlockTracker.unlockCard(Roundhouse_Wrestler.ID);
        BaseMod.addCard(new StickyFingers_Wrestler());
        UnlockTracker.unlockCard(StickyFingers_Wrestler.ID);
        BaseMod.addCard(new ThoughtEater_Wrestler());
        UnlockTracker.unlockCard(ThoughtEater_Wrestler.ID);
        BaseMod.addCard(new UncannyLaughter_Wrestler());
        UnlockTracker.unlockCard(UncannyLaughter_Wrestler.ID);
        BaseMod.addCard(new Untouchable_Wrestler());
        UnlockTracker.unlockCard(Untouchable_Wrestler.ID);
        BaseMod.addCard(new VoidCage_Wrestler());
        UnlockTracker.unlockCard(VoidCage_Wrestler.ID);
    }

    public void rareCards() {
        logger.info("Adding Rare cards...");
        BaseMod.addCard(new Amnesia_Wrestler());
        UnlockTracker.unlockCard(Amnesia_Wrestler.ID);
        BaseMod.addCard(new Catapult_Wrestler());
        UnlockTracker.unlockCard(Catapult_Wrestler.ID);
        BaseMod.addCard(new Crusher_Wrestler());
        UnlockTracker.unlockCard(Crusher_Wrestler.ID);
        BaseMod.addCard(new DominateMind_Wrestler());
        UnlockTracker.unlockCard(DominateMind_Wrestler.ID);
        BaseMod.addCard(new EldritchForm_Wrestler());
        UnlockTracker.unlockCard(EldritchForm_Wrestler.ID);
        BaseMod.addCard(new EldritchInsight_Wrestler());
        UnlockTracker.unlockCard(EldritchInsight_Wrestler.ID);
        BaseMod.addCard(new FullBodyVice_Wrestler());
        UnlockTracker.unlockCard(FullBodyVice_Wrestler.ID);
        BaseMod.addCard(new Haymaker_Wrestler());
        UnlockTracker.unlockCard(Haymaker_Wrestler.ID);
        BaseMod.addCard(new IntoNothing_Wrestler());
        UnlockTracker.unlockCard(IntoNothing_Wrestler.ID);
        BaseMod.addCard(new IronGrip_Wrestler());
        UnlockTracker.unlockCard(IronGrip_Wrestler.ID);
        BaseMod.addCard(new Leverage_Wrestler());
        UnlockTracker.unlockCard(Leverage_Wrestler.ID);
        BaseMod.addCard(new MindMeld_Wrestler());
        UnlockTracker.unlockCard(MindMeld_Wrestler.ID);
        BaseMod.addCard(new OutOfNothing_Wrestler());
        UnlockTracker.unlockCard(OutOfNothing_Wrestler.ID);
        BaseMod.addCard(new Pizzazz_Wrestler());
        UnlockTracker.unlockCard(Pizzazz_Wrestler.ID);
        BaseMod.addCard(new Submission_Wrestler());
        UnlockTracker.unlockCard(Submission_Wrestler.ID);
        BaseMod.addCard(new TagTeam_Wrestler());
        UnlockTracker.unlockCard(TagTeam_Wrestler.ID);
        BaseMod.addCard(new WearDown_Wrestler());
        UnlockTracker.unlockCard(WearDown_Wrestler.ID);
        BaseMod.addCard(new WeirdConduit_Wrestler());
        UnlockTracker.unlockCard(WeirdConduit_Wrestler.ID);
    }
    
    // There are better ways to do this than listing every single individual card, but I do not want to complicate things
    // in a "tutorial" mod. This will do and it's completely ok to use. If you ever want to clean up and
    // shorten all the imports, go look take a look at other mods, such as Hubris.
    
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

    // ================ /POST BATTLE/ ===================


    @Override
    public void receivePostBattle(AbstractRoom room) {
        AbstractPlayer player = AbstractDungeon.player;
        // Combo Rewards
        if (player.hasPower(ComboPower.POWER_ID)) {
            int amount = player.getPower(ComboPower.POWER_ID).amount;
            if (amount >= ComboPower.THRESHOLD * ComboPower.GOLD1) {
                ComboGoldReward.addComboGoldToRewards(ComboPower.GOLD1_AMOUNT);
            }
            if (amount >= ComboPower.THRESHOLD * ComboPower.HEAL) {
                player.heal(ComboPower.HEAL_AMOUNT);
            }
            if (amount >= ComboPower.THRESHOLD * ComboPower.GOLD2) {
                ComboGoldReward.addComboGoldToRewards(ComboPower.GOLD2_AMOUNT);
            }
            if (amount >= ComboPower.THRESHOLD * ComboPower.CARD) {
                AbstractDungeon.getCurrRoom().rewards.add(0, new ComboCardReward());
            }
            if (amount >= ComboPower.THRESHOLD * ComboPower.GOLD3) {
                ComboGoldReward.addComboGoldToRewards(ComboPower.GOLD3_AMOUNT);
            }
            if (amount >= ComboPower.THRESHOLD * ComboPower.MAX_HP) {
                player.increaseMaxHp(ComboPower.MAX_HP_AMOUNT, false);
            }
            if (amount >= ComboPower.THRESHOLD * ComboPower.GOLD4) {
                ComboGoldReward.addComboGoldToRewards(ComboPower.GOLD4_AMOUNT);
            }
        }
    }
}
