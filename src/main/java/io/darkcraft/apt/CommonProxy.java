package io.darkcraft.apt;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;

@Retention(CLASS)
@Target(TYPE)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public @interface CommonProxy {

}
