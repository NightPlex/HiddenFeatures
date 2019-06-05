package scripts.HardcoreLeveler.data;

import java.awt.Image;

import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;

import scripts.HardcoreLeveler.util.DataUtil;

public interface Constants {
	
	// Tool getting areas
	RSArea MINING_TUTOR_AREA = new RSArea(new RSTile(3227, 3147, 0), 4);
	RSArea WOODCUTTING_TUTOR_AREA = new RSArea(new RSTile(3227, 3246, 0), 1);
	RSArea COMBAT_TUTOR_AREA = new RSArea(new RSTile(3219, 3238, 0), 2);
	RSArea FISHING_TUTOR_AREA = new RSArea(new RSTile(3244, 3157, 0), 1);
	RSArea HAMMER_GROUND_LEVEL_AREA = new RSArea(new RSTile(2971, 3374, 0), new RSTile(2976, 3368, 0));
	RSArea HAMMER_SAME_LEVEL_AREA = new RSArea(new RSTile(2971, 3374, 1), new RSTile(2976, 3368, 1));
	RSArea HAMMER_PROXIMITY_AREA = new RSArea(new RSTile(2974, 3369, 1), new RSTile(2976, 3368, 1));

	// Task areas
	RSArea FISHING_AREA = new RSArea(new RSTile(3088, 3229, 0), 3);
	RSArea FISHING_SAFESPOT = new RSArea(new RSTile(3089, 3223, 0), 1);
	RSArea FIREMAKING_AREA = new RSArea(new RSTile[] { new RSTile(3298, 3428, 0), new RSTile(3198, 3432, 0), new RSTile(3172, 3432, 0), new RSTile(3172, 3428, 0) }); 
	RSArea FIREMAKING_START_AREA = new RSArea (new RSTile(3199, 3428, 0), new RSTile(3199, 3432, 0));
	RSArea NORMAL_TREE_AREA = new RSArea(new RSTile(3171, 3235, 0) , 15);
	RSArea OAK_AREA = new RSArea(new RSTile(3206, 3366, 0) , 5);
	RSArea VARROCK_GYM_AREA = new RSArea(new RSTile(3249,3438,0), new RSTile(3260,3435,0));
	RSArea CENTER_OF_COMBAT_AREA = new RSArea(new RSTile(3179, 3324, 0), 2);
	RSArea COMBAT_AREA = new RSArea(new RSTile[] { new RSTile(3156, 3347, 0), new RSTile(3162, 3345, 0),new RSTile(3168, 3342, 0), new RSTile(3176, 3342, 0), new RSTile(3179, 3344, 0), new RSTile(3182, 3343, 0),new RSTile(3187, 3340, 0), new RSTile(3189, 3339, 0), new RSTile(3196, 3335, 0), new RSTile(3200, 3333, 0),new RSTile(3202, 3332, 0), new RSTile(3203, 3329, 0), new RSTile(3203, 3322, 0), new RSTile(3208, 3318, 0),new RSTile(3210, 3316, 0), new RSTile(3212, 3315, 0), new RSTile(3211, 3309, 0), new RSTile(3211, 3308, 0),new RSTile(3209, 3308, 0), new RSTile(3208, 3309, 0), new RSTile(3206, 3309, 0), new RSTile(3201, 3309, 0),new RSTile(3201, 3308, 0), new RSTile(3200, 3308, 0), new RSTile(3198, 3308, 0), new RSTile(3198, 3307, 0),new RSTile(3196, 3307, 0), new RSTile(3195, 3308, 0), new RSTile(3193, 3308, 0), new RSTile(3192, 3308, 0),new RSTile(3190, 3310, 0), new RSTile(3189, 3311, 0), new RSTile(3188, 3311, 0), new RSTile(3185, 3314, 0),new RSTile(3184, 3315, 0), new RSTile(3179, 3315, 0), new RSTile(3178, 3316, 0), new RSTile(3171, 3316, 0),new RSTile(3169, 3318, 0), new RSTile(3165, 3318, 0), new RSTile(3163, 3318, 0), new RSTile(3163, 3317, 0),new RSTile(3160, 3314, 0), new RSTile(3158, 3315, 0), new RSTile(3155, 3315, 0), new RSTile(3153, 3319, 0),new RSTile(3153, 3327, 0), new RSTile(3153, 3330, 0), new RSTile(3154, 3331, 0), new RSTile(3154, 3334, 0),new RSTile(3153, 3335, 0), new RSTile(3153, 3338, 0), new RSTile(3152, 3339, 0), new RSTile(3152, 3342, 0),new RSTile(3154, 3345, 0), new RSTile(3155, 3347, 0) });
	RSArea SMITHING_AREA = new RSArea(new RSTile(3188, 3426 ,0), 1);
	RSArea SMELTING_AREA = new RSArea(new RSTile(2974, 3369,0), 1);
	RSArea MINING_AREA = new RSArea(new RSTile(3285, 3365, 0) , 5);
	RSArea COOKING_AREA = new RSArea(new RSTile(3238, 3411, 0) , 1);
	
	// Tutors
	int MINING_TUTOR_ID = 3222;
	int WOODCUTTING_TUTOR_ID = 3226;
	int COMBAT_TUTOR_ID = 3216;
	int FISHING_TUTOR_ID = 3221;
	int[] DIALOGUE_INTERFACES = { 219, 217, 193, 231 };
	
	// Equipment IDs
	/** Tools are stored in the arrays from best to worst because the script will try to use the lowest index available in the bank */
	int SMALL_FISHING_NET = 303;
	int TINDERBOX = 590;
	int HAMMER = 2347;
	int BRONZE_PICKAXE = 1265;
	int IRON_PICKAXE = 1267;
	int[] PICKAXES = { IRON_PICKAXE, BRONZE_PICKAXE };
	int BRONZE_AXE = 1351;
	int IRON_AXE = 1349;
	int[] AXES = { IRON_AXE, BRONZE_AXE };
	int TRAINING_SWORD = 9703;
	int BRONZE_SWORD = 1277;
	int BRONZE_LONGSWORD = 1291;
	int BRONZE_SCIMITAR = 1321;
	int IRON_SWORD = 1279;
	int IRON_LONGSWORD = 1293;
	int IRON_SCIMITAR = 1323;
	int[] WEAPONS = {IRON_SCIMITAR, IRON_LONGSWORD, IRON_SWORD, BRONZE_SCIMITAR, BRONZE_LONGSWORD, BRONZE_SWORD, TRAINING_SWORD};

	// Resources
	int BONES = 526;
	int COWHIDE = 1739;
	int RAW_BEEF = 2132;
	int TIN_ORE = 438;
	int COPPER_ORE = 436;
	int NORMAL_LOGS = 1511;
	int OAK_LOGS = 1521;
	int BRONZE_BAR = 2349;
	int RAW_SHRIMPS = 317;
	int RAW_ANCHOVIES = 321;
	int [] RAW_FOOD = {RAW_SHRIMPS, RAW_ANCHOVIES};
	
	// Resource objects / NPCs
	int FISHING_SPOT = 1525;
    int [] REGULAR_TREES = {1276, 1278, 1282, 1286};
    int [] OAK_TREE = {1751};
    int ANVIL = 2097;
	int FURANCE = 24009;
	int [] TIN_ROCK = {7485, 7486};
	int [] COPPER_ROCK = {7453, 7484};
	int RANGE = 7183;
	int DUMMY = 1764; 
	int COW [] = {2808, 2809, 2806, 2805, 2807};
	
	// Paint
	Image NOOB_ACCOUNT_LEVELER_PAINT = DataUtil.getImage("https://i.imgur.com/G4CL1sK.png");
	Image TASK_LIST_PAINT = DataUtil.getImage("https://i.imgur.com/7P58Avx.png");
	Image TASK_SWITCH_BUTTON = DataUtil.getImage("https://i.imgur.com/LlpHiYy.png");
	String DEATH_MESSAGE = "LOL";
	
}