package com.riversql.id;

public interface IIdentifier
{
	public boolean equals(Object rhs);

	public String toString();

	public int hashCode();
}
