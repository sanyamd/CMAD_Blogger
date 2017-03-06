package com.cisco.cmad.api;

import java.io.PrintStream;
import java.io.PrintWriter;

public class BlogException extends RuntimeException
{
  public BlogException()
  {
  }

  public BlogException(String message)
  {
    super(message);
  }

  public BlogException(Throwable cause)
  {
    super(cause);
  }

  public BlogException(String message, Throwable cause)
  {
    super(message, cause);
  }

  public BlogException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
  {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public String getMessage()
  {
    return super.getMessage();
  }

  public String getLocalizedMessage()
  {
    return super.getLocalizedMessage();
  }

  public synchronized Throwable getCause()
  {
    return super.getCause();
  }

  public synchronized Throwable initCause(Throwable cause)
  {
    return super.initCause(cause);
  }

  public String toString()
  {
    return super.toString();
  }

  public void printStackTrace()
  {
    super.printStackTrace();
  }

  public void printStackTrace(PrintStream s)
  {
    super.printStackTrace(s);
  }

  public void printStackTrace(PrintWriter s)
  {
    super.printStackTrace(s);
  }

  public synchronized Throwable fillInStackTrace()
  {
    return super.fillInStackTrace();
  }

  public StackTraceElement[] getStackTrace()
  {
    return super.getStackTrace();
  }

  public void setStackTrace(StackTraceElement[] stackTrace)
  {
    super.setStackTrace(stackTrace);
  }
}