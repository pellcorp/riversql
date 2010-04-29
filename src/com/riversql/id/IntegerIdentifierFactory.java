package com.riversql.id;

/**
 * This class is a factory that generates <tt>IntegerIdentifier</tt>
 * objects. Each identifier generated will have a value one greater
 * than the previously generated one.
 *
 * @author <A HREF="mailto:colbell@users.sourceforge.net">Colin Bell</A>
 */
public class IntegerIdentifierFactory implements IIdentifierFactory
{
	private int _next;

	/**
	 * Default ctor. First identifier generated will have a value of zero.
	 */
	public IntegerIdentifierFactory()
	{
		this(0);
	}

	/**
	 * ctor specifying the value of the first identifier.
	 *
	 * @param	initialValue	Value for first identifier generated.
	 */
	public IntegerIdentifierFactory(int initialValue)
	{
		super();
		_next = initialValue;
	}

	/**
	 * Create a new identifier.
	 *
	 * @return	The new identifier object.
	 */
	public synchronized IIdentifier createIdentifier()
	{
		return new IntegerIdentifier(_next++);
	}
}
