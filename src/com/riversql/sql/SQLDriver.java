package com.riversql.sql;

import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.riversql.id.IIdentifier;
import com.riversql.persist.ValidationException;
import com.riversql.util.PropertyChangeReporter;
import com.riversql.util.StringManager;
import com.riversql.util.StringManagerFactory;
/**
 * This represents a JDBC driver.
 * This class is a <CODE>JavaBean</CODE>.
 *
 * @author <A HREF="mailto:colbell@users.sourceforge.net">Colin Bell</A>
 */
public class SQLDriver implements ISQLDriver, Cloneable, Serializable
{
    static final long serialVersionUID = 8506401259069527981L;
    
	/** Internationalized strings for this class. */
	private static final StringManager s_stringMgr =
		StringManagerFactory.getStringManager(SQLDriver.class);

	private interface IStrings
	{
		String ERR_BLANK_NAME = s_stringMgr.getString("SQLDriver.error.blankname");
		String ERR_BLANK_DRIVER = s_stringMgr.getString("SQLDriver.error.blankdriver");
		String ERR_BLANK_URL = s_stringMgr.getString("SQLDriver.error.blankurl");
	}

	/** The <CODE>IIdentifier</CODE> that uniquely identifies this object. */
	private IIdentifier _id;

	/** The name of this driver. */
	private String _name;

	/**
	 * File name associated with <CODE>_jarFileURL</CODE>.
	 */
	private String _jarFileName = null;

	/** Names for driver jar files. */
	@SuppressWarnings("unchecked")
	private List _jarFileNamesList = new ArrayList();

	/** The class name of the JDBC driver. */
	private String _driverClassName;

	/** Default URL required to access the database. */
	private String _url;

	/** Is the JDBC driver class for this object loaded? */
	private boolean _jdbcDriverClassLoaded;

	/** Object to handle property change events. */
	private transient PropertyChangeReporter _propChgReporter;

    /** Default Website URL for more info about the JDBC driver */
    private String _websiteUrl;
    
	/**
	 * Ctor specifying the identifier.
	 *
	 * @param	id	Uniquely identifies this object.
	 */
	public SQLDriver(IIdentifier id)
	{
		super();
		_id = id;
		_name = "";
		_jarFileName = null;
		_driverClassName = null;
		_url = "";
        _websiteUrl = "";
	}

	/**
	 * Default ctor.
	 */
	public SQLDriver()
	{
		super();
	}

	/**
	 * Assign data from the passed <CODE>ISQLDriver</CODE> to this one. This
	 * does <B>not</B> copy the identifier.
	 *
	 * @param	rhs	 <CODE>ISQLDriver</CODE> to copy data from.
	 *
	 * @exception	ValidationException
	 *				Thrown if an error occurs assigning data from
	 *				<CODE>rhs</CODE>.
	 */
	public synchronized void assignFrom(ISQLDriver rhs)
		throws ValidationException
	{
		setName(rhs.getName());
		setJarFileNames(rhs.getJarFileNames());
		setDriverClassName(rhs.getDriverClassName());
		setUrl(rhs.getUrl());
		setJDBCDriverClassLoaded(rhs.isJDBCDriverClassLoaded());
        setWebSiteUrl(rhs.getWebSiteUrl());
	}

	/**
	 * Returns <TT>true</TT> if this objects is equal to the passed one. Two
	 * <TT>ISQLDriver</TT> objects are considered equal if they have the same
	 * identifier.
	 */
	 
	public boolean equals(Object rhs)
	{
		boolean rc = false;
		if (rhs != null && rhs.getClass().equals(getClass()))
		{
			rc = ((ISQLDriver) rhs).getIdentifier().equals(getIdentifier());
		}
		return rc;
	}

	/**
	 * Returns a hash code value for this object.
	 */
	 
	public synchronized int hashCode()
	{
		return getIdentifier().hashCode();
	}

	/**
	 * Returns the name of this <TT>ISQLDriver</TT>.
	 */
	 
	public String toString()
	{
		return getName();
	}

	/**
	 * Return a clone of this object.
	 */
	 
	public Object clone()
	{
		try
		{
			final SQLDriver driver = (SQLDriver)super.clone();
			driver._propChgReporter = null;
			return driver;
		}
		catch (CloneNotSupportedException ex)
		{
			throw new InternalError(ex.getMessage()); // Impossible.
		}
	}

	/**
	 * Compare this <TT>ISQLDriver</TT> to another object. If the passed object
	 * is a <TT>ISQLDriver</TT>, then the <TT>getName()</TT> functions of the two
	 * <TT>ISQLDriver</TT> objects are used to compare them. Otherwise, it throws a
	 * ClassCastException (as <TT>ISQLDriver</TT> objects are comparable only to
	 * other <TT>ISQLDriver</TT> objects).
	 */
	public int compareTo(Object rhs)
	{
		return _name.compareTo(((ISQLDriver)rhs).getName());
	}

	public void addPropertyChangeListener(PropertyChangeListener listener)
	{
		getPropertyChangeReporter().addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener)
	{
		getPropertyChangeReporter().removePropertyChangeListener(listener);
	}

	public void setReportPropertyChanges(boolean report)
	{
		getPropertyChangeReporter().setNotify(report);
	}

	public IIdentifier getIdentifier()
	{
		return _id;
	}

	public void setIdentifier(IIdentifier id)
	{
		_id = id;
	}

	public String getDriverClassName()
	{
		return _driverClassName;
	}

	public void setDriverClassName(String driverClassName)
		throws ValidationException
	{
		String data = getString(driverClassName);
		if (data.length() == 0)
		{
			throw new ValidationException(IStrings.ERR_BLANK_DRIVER);
		}
		if (_driverClassName != data)
		{
			final String oldValue = _driverClassName;
			_driverClassName = data;
			getPropertyChangeReporter().firePropertyChange(
				ISQLDriver.IPropertyNames.DRIVER_CLASS,
				oldValue,
				_driverClassName);
		}
	}

	/**
	 * @deprecated	Replaced by getJarFileNames().
	 */
	@Deprecated
	public String getJarFileName()
	{
		return _jarFileName;
	}

	public void setJarFileName(String value)
	{
		if (value == null)
		{
			value = "";
		}
		if (_jarFileName == null || !_jarFileName.equals(value))
		{
			final String oldValue = _jarFileName;
			_jarFileName = value;
			getPropertyChangeReporter().firePropertyChange(
				ISQLDriver.IPropertyNames.JARFILE_NAME,
				oldValue,
				_jarFileName);
		}
	}

	@SuppressWarnings("unchecked")
	public synchronized String[] getJarFileNames()
	{
		return (String[]) _jarFileNamesList.toArray(
			new String[_jarFileNamesList.size()]);
	}

	@SuppressWarnings("unchecked")
	public synchronized void setJarFileNames(String[] values)
	{
		String[] oldValue =
			(String[]) _jarFileNamesList.toArray(
				new String[_jarFileNamesList.size()]);
		_jarFileNamesList.clear();

		if (values == null)
		{
			values = new String[0];
		}

		for (int i = 0; i < values.length; ++i)
		{
			_jarFileNamesList.add(values[i]);
		}

		getPropertyChangeReporter().firePropertyChange(
			ISQLDriver.IPropertyNames.JARFILE_NAMES,
			oldValue,
			values);
	}
	public String getUrl()
	{
		return _url;
	}

	public void setUrl(String url) throws ValidationException
	{
		String data = getString(url);
		if (data.length() == 0)
		{
			throw new ValidationException(IStrings.ERR_BLANK_URL);
		}
		if (_url != data)
		{
			final String oldValue = _url;
			_url = data;
			getPropertyChangeReporter().firePropertyChange(
				ISQLDriver.IPropertyNames.URL,
				oldValue,
				_url);
		}
	}

	public String getName()
	{
		return _name;
	}

	public void setName(String name) throws ValidationException
	{
		String data = getString(name);
		if (data.length() == 0)
		{
			throw new ValidationException(IStrings.ERR_BLANK_NAME);
		}
		if (_name != data)
		{
			final String oldValue = _name;
			_name = data;
			getPropertyChangeReporter().firePropertyChange(
				ISQLDriver.IPropertyNames.NAME,
				oldValue,
				_name);
		}
	}

	public boolean isJDBCDriverClassLoaded()
	{
		return _jdbcDriverClassLoaded;
	}

	public void setJDBCDriverClassLoaded(boolean cl)
	{
		_jdbcDriverClassLoaded = cl;
		//TODO: Decide whether this should be a bound property or not.
		//		getPropertyChangeReporter().firePropertyChange(ISQLDriver.IPropertyNames.NAME, _name, _name);
	}

//	public synchronized StringWrapper[] getJarFileNameWrappers()
//	{
//		StringWrapper[] wrappers = new StringWrapper[_jarFileNamesList.size()];
//		for (int i = 0; i < wrappers.length; ++i)
//		{
//			wrappers[i] = new StringWrapper((String) _jarFileNamesList.get(i));
//		}
//		return wrappers;
//	}
//
//	public StringWrapper getJarFileNameWrapper(int idx)
//		throws ArrayIndexOutOfBoundsException
//	{
//		return new StringWrapper((String) _jarFileNamesList.get(idx));
//	}
//
//	public void setJarFileNameWrappers(StringWrapper[] value)
//	{
//		_jarFileNamesList.clear();
//		if (value != null)
//		{
//			for (int i = 0; i < value.length; ++i)
//			{
//				_jarFileNamesList.add(value[i].getString());
//			}
//		}
//	}

//	public void setJarFileNameWrapper(int idx, StringWrapper value)
//		throws ArrayIndexOutOfBoundsException
//	{
//		_jarFileNamesList.set(idx, value);
//	}

	private String getString(String data)
	{
		return data != null ? data.trim() : "";
	}

	private synchronized PropertyChangeReporter getPropertyChangeReporter()
	{
		if (_propChgReporter == null)
		{
			_propChgReporter = new PropertyChangeReporter(this);
		}
		return _propChgReporter;
	}

    /* (non-Javadoc)
     * @see com.riversql.sql.ISQLDriver#getWebSiteUrl()
     */
    public String getWebSiteUrl() {
        return _websiteUrl;
    }

    /* (non-Javadoc)
     * @see com.riversql.sql.ISQLDriver#setWebSiteUrl(java.lang.String)
     */
    public void setWebSiteUrl(String url) throws ValidationException { 
        String data = getString(url);
        if (!data.equals(_websiteUrl)) {
            final String oldValue = _websiteUrl;
            _websiteUrl = data;
            PropertyChangeReporter pcr = getPropertyChangeReporter();
            pcr.firePropertyChange(ISQLDriver.IPropertyNames.WEBSITE_URL,
                                   oldValue,
                                   _websiteUrl);            
        }
    }
    
    
}
