package DeepSpiringMod.helpers;

public class ModHelper {
    public static String makePath(String id) {
        return "DeepSpiringMod:" + id;
    }

    public static String makeImagePath(String path) {
        return "DeepSpiringModResources/img/" + path;
    }

    public static String makeLocalizationPath(String path) {
        return "DeepSpiringModResources/localization/" + path;
    }
}

