package com.riversql.util;

public interface IMessageHandler
{
//	/**
//	 * Show a message describing the passed exception.
//	 * 
//	 * @param	th		Exception.
//	 */
	void showMessage(Throwable th);
//
//	/**
//	 * Show a message.
//	 * 
//	 * @param	msg		The message.
//	 */
	void showMessage(String msg);
//
//	/**
//	 * Show an error message describing the passed exception. The implementation
//	 * of <TT>IMessageHandler</TT> may or may not treat this differently to
//	 * <TT>showMessage(Throwable)</TT>.
//	 * 
//	 * @param	th		Exception.
//	 */
	void showErrorMessage(Throwable th);
//
//	/**
//	 * Show an error message. The implementation
//	 * of <TT>IMessageHandler</TT> may or may not treat this differently to
//	 * <TT>showMessage(String)</TT>.
//	 * 
//	 * @param	th		Exception.
//	 */
	void showErrorMessage(String msg);
//
   void showWarningMessage(String msg);
}