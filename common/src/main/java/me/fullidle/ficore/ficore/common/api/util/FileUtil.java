package me.fullidle.ficore.ficore.common.api.util;

import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @apiNote 文件工具,可以把yaml数据存在里面方便处理，自定义多个文件也方便
 * 或许用不习惯，用的时候只需要
 * <pre>{@code
 * FileUtil fileUtil = FileUtil.getInstance(file);
 * FileConfiguration config = fileUtil.getConfiguration();
 * config.set("test","test);
 * fileUtil.save();
 * }</pre>
 * 这样就可以用了没必要直接把config进行save除非你想这样
 * <pre>{@code
 * config.save(fileUtil.getFile());
 * //这可能需要抛出报错
 * }</pre>
 */
@Getter
public class FileUtil {
    private final File file;
    private final FileConfiguration configuration;
    public static final Map<File,FileUtil> cache = new HashMap<>();
    /**
     * @param file 不能是文件夹
     */
    @SneakyThrows
    private FileUtil(File file){
        if (!file.exists()){
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            file.createNewFile();
        }
        this.file = file;
        this.configuration = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * @apiNote 用了后会直接把你改过的yaml数据保存到文件
     */
    @SneakyThrows
    public void save(){
        this.configuration.save(this.file);
    }

    /**
     * 清理缓存
     */
    public static void clearCache(){
        cache.clear();
    }
    /**
     * @param file 非文件夹
     * @param isNew 是否是新的
     * @apiNote 如果缓存内没有则会创建一个新的,如果有的话用isNew判断是否用缓存(缓存内没有则自动补上)
     * @return FileUtil
     */
    public static FileUtil getInstance(File file,boolean isNew) {
        FileUtil fileUtil = new FileUtil(file);
        if (isNew) {
            return fileUtil;
        }
        return cache.computeIfAbsent(file, FileUtil::new);
    }
}
