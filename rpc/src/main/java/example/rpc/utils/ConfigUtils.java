package example.rpc.utils;

import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;

public class ConfigUtils {


    public static <T> T loadConfig(Class<T> clazz, String prefix) {
        return loadConfig(clazz, prefix, "");
    }

    public static <T> T loadConfig(Class<T> clazz, String prefix, String environment) {
        StrBuilder configFileBuilder = new StrBuilder("application");
        if(StrUtil.isNotBlank(environment)) {
            configFileBuilder.append("-").append(environment);
        }
        configFileBuilder.append(".properties");
        Props configFile = new Props(configFileBuilder.toString());

        return configFile.toBean(clazz, prefix);
    }
}
