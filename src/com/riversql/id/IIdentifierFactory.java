package com.riversql.id;

/**
 * This interface defines the requirements for a factory that generates
 * indentifiers.
 *
 * @author <A HREF="mailto:colbell@users.sourceforge.net">Colin Bell</A>
 */
public interface IIdentifierFactory
{
	/**
	 * Create a new identifier.
	 *
	 * @return	The new identifier object.
	 */
	IIdentifier createIdentifier();
}
