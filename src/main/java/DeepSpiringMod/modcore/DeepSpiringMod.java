package DeepSpiringMod.modcore;

import DeepSpiringMod.cards.*;
import DeepSpiringMod.characters.Deepfect;
import DeepSpiringMod.patches.PlayerColorEnum;
import DeepSpiringMod.relics.*;
import DeepSpiringMod.helpers.ModHelper;

import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.evacipated.cardcrawl.mod.stslib.Keyword;

import basemod.BaseMod;
// import basemod.helpers.RelicType;
import basemod.interfaces.EditCardsSubscriber;
import basemod.interfaces.EditRelicsSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.EditCharactersSubscriber;
import basemod.interfaces.EditKeywordsSubscriber;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.nio.charset.StandardCharsets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

@SpireInitializer // 加载mod的注解
public class DeepSpiringMod implements
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditCharactersSubscriber,
        EditKeywordsSubscriber {
    // 人物选择界面按钮的图片
    private static final String MY_CHARACTER_BUTTON = "DeepSpiringModResources/img/char/deepfect_button.png";
    // 人物选择界面的立绘
    private static final String MY_CHARACTER_PORTRAIT = "DeepSpiringModResources/img/char/Deepfect_theme.png";

    // 攻击牌的背景（小尺寸）
    private static final String BG_ATTACK_512 = "DeepSpiringModResources/img/512/bg_attack_lime.png";
    // 能力牌的背景（小尺寸）
    private static final String BG_POWER_512 = "DeepSpiringModResources/img/512/bg_power_lime.png";
    // 技能牌的背景（小尺寸）
    private static final String BG_SKILL_512 = "DeepSpiringModResources/img/512/bg_skill_lime.png";
    // 在卡牌和遗物描述中的能量图标
    private static final String SMALL_ORB = "DeepSpiringModResources/img/char/card_lime_small_orb.png";
    // 攻击牌的背景（大尺寸）
    private static final String BG_ATTACK_1024 = "DeepSpiringModResources/img/1024/bg_attack_lime.png";
    // 能力牌的背景（大尺寸）
    private static final String BG_POWER_1024 = "DeepSpiringModResources/img/1024/bg_power_lime.png";
    // 技能牌的背景（大尺寸）
    private static final String BG_SKILL_1024 = "DeepSpiringModResources/img/1024/bg_skill_lime.png";
    // private static final String BG_SKILL_1024 = null;
    // 在卡牌预览界面的能量图标
    private static final String BIG_ORB = "DeepSpiringModResources/img/1024/card_lime_orb.png";
    // 小尺寸的能量图标（战斗中，牌堆预览）
    private static final String ENEYGY_ORB = "DeepSpiringModResources/img/512/card_lime_orb.png";
    public static final Color MY_COLOR = new Color(0.0F / 255.0F, 0.0F / 255.0F, 255.0F / 255.0F, 1.0F);
    // 构造方法
    public DeepSpiringMod() {
        BaseMod.subscribe(this); // 告诉basemod你要订阅事件
        // 注册颜色
        BaseMod.addColor(PlayerColorEnum.DEEP_BLUE, MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR,BG_ATTACK_512, BG_SKILL_512, BG_POWER_512, ENEYGY_ORB, BG_ATTACK_1024, BG_SKILL_1024, BG_POWER_1024, BIG_ORB, SMALL_ORB);
    }

//     // 实现 PostInitializeSubscriber 接口的方法
//    @Override
//     public void receivePostInitialize() {
//         BaseMod.logger.info("DeepSpiringMod 初始化完成！");
//     }

    // 注解需要调用的方法，必须写
    public static void initialize() {
        new DeepSpiringMod();
    }

    // 当basemod开始注册mod卡牌时，便会调用这个函数
    @Override
    public void receiveEditCards() {
        // 这里写添加你卡牌的代码
        BaseMod.addCard(new Strike_DeepBlue());
        BaseMod.addCard(new BlockStrike());
        BaseMod.addCard(new Defend_DeepBlue());
        BaseMod.addCard(new StochasticProcess());
        BaseMod.addCard(new AttentionHead());
        BaseMod.addCard(new ForwardPropagation());
        BaseMod.addCard(new Overflow());
        BaseMod.addCard(new PositionalEncoding());
        BaseMod.addCard(new FeatureMap(0, 0));
        BaseMod.addCard(new Convolution());
        BaseMod.addCard(new StochasticGradientDescent());
        BaseMod.addCard(new StrikeDataset());
        BaseMod.addCard(new DefendDataset());
        BaseMod.addCard(new ChargeDataset());
        BaseMod.addCard(new Misconduct());
        BaseMod.addCard(new ScalingLaw());
        BaseMod.addCard(new Hallucination());
        BaseMod.addCard(new Diffusion());
        BaseMod.addCard(new AIForm());
        BaseMod.addCard(new RecurrentNeuralNetwork());
        BaseMod.addCard(new SOTA());
        BaseMod.addCard(new KFord());
        BaseMod.addCard(new LongTraining());
        BaseMod.addCard(new Regularization());
        BaseMod.addCard(new Embedding());
        BaseMod.addCard(new Distillation());
    }

    public void receiveEditStrings() {
        String lang;
        // if (Settings.language == Settings.GameLanguage.ZHS) {
        //     lang = "ZHS"; // 如果语言设置为简体中文，则加载ZHS文件夹的资源
        // } else {
        //     lang = "ENG"; // 如果没有相应语言的版本，默认加载英语
        // }
        lang = Settings.language.toString();
        BaseMod.loadCustomStringsFile(CardStrings.class, "DeepSpiringModResources/localization/" + lang + "/cards.json"); // 加载相应语言的卡牌本地化内容。
        // 如果是中文，加载的就是"DeepSpiringModResources/localization/ZHS/cards.json"
        BaseMod.loadCustomStringsFile(CharacterStrings.class, "DeepSpiringModResources/localization/" + lang + "/characters.json");
        BaseMod.loadCustomStringsFile(RelicStrings.class, ModHelper.makeLocalizationPath(lang + "/relics.json"));
        BaseMod.loadCustomStringsFile(PowerStrings.class, ModHelper.makeLocalizationPath(lang + "/powers.json"));
        BaseMod.loadCustomStringsFile(UIStrings.class, ModHelper.makeLocalizationPath(lang + "/UIStrings.json"));
    }

    // 当开始添加人物时，调用这个方法
    @Override
    public void receiveEditCharacters() {
        // 向basemod注册人物
        BaseMod.addCharacter(new Deepfect(CardCrawlGame.playerName, PlayerColorEnum.Deepfect), MY_CHARACTER_BUTTON, MY_CHARACTER_PORTRAIT, PlayerColorEnum.Deepfect);
        // BaseMod.addCharacter(new Kael(Kael.charStrings.NAMES[1],AbstractPlayerEnum.Kael),"images/ui/charSelect/defectButton.png",assetPath("/img/character/Kael/portraitKral.jpg"),AbstractPlayerEnum.Kael);
    }

    @Override
    public void receiveEditRelics() {
        // BaseMod.addRelic(new BurntHeatSink(), RelicType.SHARED); // RelicType表示是所有角色都能拿到的遗物，还是一个角色的独有遗物
        // BaseMod.addRelic(new TryBlock(), RelicType.SHARED);
        BaseMod.addRelicToCustomPool(new BurntHeatSink(), PlayerColorEnum.DEEP_BLUE);
        BaseMod.addRelicToCustomPool(new TryBlock(), PlayerColorEnum.DEEP_BLUE);
        BaseMod.addRelicToCustomPool(new GPU(), PlayerColorEnum.DEEP_BLUE);
    }

    @Override
    public void receiveEditKeywords() {
        Gson gson = new Gson();
        String lang;
        lang = Settings.language.toString();

        String json = Gdx.files.internal(ModHelper.makeLocalizationPath(lang + "/keywords.json"))
                .readString(String.valueOf(StandardCharsets.UTF_8));
        Keyword[] keywords = gson.fromJson(json, Keyword[].class);
        if (keywords != null) {
            for (Keyword keyword : keywords) {
                // 这个id要全小写
                BaseMod.addKeyword("deepspiringmod", keyword.NAMES[0], keyword.NAMES, keyword.DESCRIPTION);
            }
        }
    }
}
