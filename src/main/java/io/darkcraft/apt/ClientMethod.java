package io.darkcraft.apt;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.CLASS;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This annotation can be placed on methods in the common side of a BaseProxy and when invoked on the server
 * will generate a packet containing all of the required information which will then get transmitted to the client
 * and invoked on the client proxy.<p/>
 * 
 * Methods annotated with this must be uniquely named
 * 
 * @author DarkholmeTenk
 */
@Retention(CLASS)
@Target(METHOD)
public @interface ClientMethod {
	
	/**
	 * How the method should be transmitted
	 */
	Broadcast broadcast() default Broadcast.ALL;
	
	String serialisableType() default "TRANSMIT";

	/**
	 * This enum determines which of the sendTo methods will be used to transmit the packet to clients.
	 */
	public static enum Broadcast
	{
		/**
		 * The packet will be transmitted to all clients
		 */
		ALL(false),
		
		/**
		 * The packet will be transmitted to a specific dimension. The first parameter of the method must be an int or World
		 */
		DIMENSION(true),
		
		/**
		 * The packet will be transmitted to a specific player. The first parameter of the method must be an EntityPlayerMP or a PlayerContainer
		 */
		PLAYER(true);
		
		public final boolean skipFirst;
		private Broadcast(boolean skipFirst)
		{
			this.skipFirst = skipFirst;
		}
	}
}
