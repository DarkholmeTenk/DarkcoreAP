package io.darkcraft.apt;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This method should be placed on a CommonProxy which extends {@link io.darkcraft.darkcore.mod.proxy.BaseProxy BaseProxy}
 * and denotes that some of the methods in the proxy will be clientside methods.<p/>
 * 
 * As a result an Impl class will be generated from the proxy and the {@literal @SidedProxy} annotation should refer to this
 * new Impl class
 * 
 * @author DarkholmeTenk
 *
 */
@Retention(CLASS)
@Target(TYPE)
public @interface CommonProxy {

}
