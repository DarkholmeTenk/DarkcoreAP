package io.darkcraft.apt;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.CLASS;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(CLASS)
@Target(METHOD)
public @interface ClientMethod {
	
	Broadcast broadcast() default Broadcast.ALL;
	
	String serialisableType() default "TRANSMIT";

	public static enum Broadcast
	{
		ALL(false),
		DIMENSION(true),
		PLAYER(true);
		
		public final boolean skipFirst;
		private Broadcast(boolean skipFirst)
		{
			this.skipFirst = skipFirst;
		}
	}
}
