package org.quantumclient.energy;

import java.lang.annotation.*;

/**
 * @author ChiquitaV2
 * @since 19/3/2021
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Subscribe {

    /**
     * Should convert method to a Consumer with LambdaMetafactory
     * @return lambda value
     */
    boolean lambda() default false;
    // i was reading stackoverflow and it said that a lambda would is almost as fast as native
    // but from my testing it's not but i don't wanna delete my work

}
