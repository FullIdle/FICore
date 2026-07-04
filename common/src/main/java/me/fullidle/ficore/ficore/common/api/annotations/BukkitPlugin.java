package me.fullidle.ficore.ficore.common.api.annotations;


import org.bukkit.event.Listener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BukkitPlugin {
    Class<? extends Listener>[] listeners() default {};
    Class<?>[] commands() default {};
}
