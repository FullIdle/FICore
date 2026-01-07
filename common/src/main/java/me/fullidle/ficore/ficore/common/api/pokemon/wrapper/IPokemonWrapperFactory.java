package me.fullidle.ficore.ficore.common.api.pokemon.wrapper;

/**
 * 感觉之后如果要用使用比较占内存
 * 暂时还没用到的
 */
public interface IPokemonWrapperFactory<T> {
    IPokemonWrapper<T> create(T original);

    IPokemonWrapper<T> create(ISpeciesWrapper<?> speciesWrapper);

    /**
     * 看情况定，想怎么解析怎么解析，我就当是nbt 或者 json看的
     */
    IPokemonWrapper<T> create(String context);
}
