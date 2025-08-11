package me.fullidle.ficore.ficore.common.api.pokemon.pools;

import lombok.val;
import me.fullidle.ficore.ficore.common.api.data.FIData;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.ISpeciesWrapper;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Collection;
import java.util.stream.Collectors;

public class ConfigPool implements SpeciesWrapperPool {
    public final SpeciesWrapperPool include;
    public final SpeciesWrapperPool exclude;

    public ConfigPool(SpeciesWrapperPool include, SpeciesWrapperPool exclude) {
        this.include = include;
        this.exclude = exclude;
    }

    @Override
    public Collection<ISpeciesWrapper<?>> values() {
        return removeExclude(include.values());
    }

    public Collection<ISpeciesWrapper<?>> removeExclude(Collection<ISpeciesWrapper<?>> collection) {
        collection.removeAll(exclude.values());
        return collection;
    }

    @Override
    public boolean contains(ISpeciesWrapper<?> wrapper) {
        return !exclude.contains(wrapper) && include.contains(wrapper);
    }

    /**
     * 通过读取yaml格式的配置创建ConfigPool
     */
    public static ConfigPool fromYaml(ConfigurationSection section) {
        return new ConfigPool(
                new CompositePool(section.getStringList("includes").stream().map(ConfigPool::baseParse).collect(Collectors.toList())),
                new CompositePool(section.getStringList("excludes").stream().map(ConfigPool::baseParse).collect(Collectors.toList()))
        );
    }

    /**
     * <pre>
     *  {all} = 所有宝可梦
     *  {legend} = 所有传说宝可梦
     *  {mythical} = 所有神话宝可梦 #1.12.2 该池等同于{legend}
     *  直接写物种名 = 单指定物种 通常经过汉化的模组是可以支持中文名的!
     *  直接写编号 = 指定物种
     *  {gen(slot)} = 指定世代的宝可梦  ==> {gen(1)} = 第一世代的宝可梦
     * </pre>
     * @throws IllegalArgumentException 当无法解析时抛出
     */
    public static SpeciesWrapperPool baseParse(String info) {
        val lowerCase = info.toLowerCase();
        switch (lowerCase) {
            case "{all}": {
                return AllPool.INSTANCE;
            }
            case "{legend}": {
                return LegendPool.INSTANCE;
            }
            case "{mythical}": {
                return MythicalPool.INSTANCE;
            }
            case "{ultra}": {
                return UltraPool.INSTANCE;
            }
        }
        if (lowerCase.startsWith("{gen(")) {
            val index = lowerCase.indexOf(")");
            if (index != -1) {
                val gen = lowerCase.substring(5, index);
                if (NumberUtils.isDigits(gen)) return new GenerationPool(Integer.parseInt(gen));
            }
        }
        if (NumberUtils.isDigits(info))
            return new SinglePool(FIData.V1_version.getSpeciesWrapperFactory().create(Integer.parseInt(info)));
        return new SinglePool(FIData.V1_version.getSpeciesWrapperFactory().create(info));
    }
}
