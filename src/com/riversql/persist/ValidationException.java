package com.riversql.persist;

import com.riversql.util.BaseException;

public class ValidationException extends BaseException
{
	public ValidationException(String msg)
	{
		super(msg);
	}
}
