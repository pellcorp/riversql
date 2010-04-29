package com.riversql.util;

import java.io.PrintStream;
import java.io.PrintWriter;
/**
 * Base runtime exception.
 *
 * @author <A HREF="mailto:colbell@users.sourceforge.net">Colin Bell</A>
 */
public class BaseRuntimeException extends RuntimeException
{
	/** If this exception is wrapped around another it is stored here. */
	private Throwable _wrapee;

	/**
	 * Default ctor. Creates an exception with an empty string ("")
	 * as its message.
	 */
	public BaseRuntimeException()
	{
		this("");
	}

	/**
	 * Ctor specifying the message.
	 *
	 * @param	msg	 The message.
	 */
	public BaseRuntimeException(String msg)
	{
		super(msg != null ? msg : "");
	}

	/**
	 * Ctor specifying an exception that this one should
	 * be wrapped around.
	 *
	 * @param	wrapee	The wrapped exception.
	 */
	public BaseRuntimeException(Throwable wrapee)
	{
		super(getMessageFromException(wrapee));
		_wrapee = wrapee;
	}

	public String toString()
	{
		if (_wrapee != null)
		{
			return _wrapee.toString();
		}
		return super.toString();
	}

	public void printStackTrace()
	{
		if (_wrapee != null)
		{
			_wrapee.printStackTrace();
		}
		else
		{
			super.printStackTrace();
		}
	}

	public void printStackTrace(PrintStream s)
	{
		if (_wrapee != null)
		{
			_wrapee.printStackTrace(s);
		}
		else
		{
			super.printStackTrace(s);
		}
	}

	public void printStackTrace(PrintWriter wtr)
	{
		if (_wrapee != null)
		{
			_wrapee.printStackTrace(wtr);
		}
		else
		{
			super.printStackTrace(wtr);
		}
	}

	/**
	 * Retrieve the exception that this one is wrapped around. This can be
	 * <TT>null</TT>.
	 *
	 * @return	The wrapped exception or <TT>null</TT>.
	 */
	public Throwable getWrappedThrowable()
	{
		return _wrapee;
	}

	private static String getMessageFromException(Throwable th)
	{
		String rtn = "";
		if (th != null)
		{
			String msg = th.getMessage();
			if (msg != null)
			{
				rtn = msg;
			}
		}
		return rtn;
	}
}
