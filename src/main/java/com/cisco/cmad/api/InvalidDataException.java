package com.cisco.cmad.api;

public class InvalidDataException extends BlogException
{
  public InvalidDataException()
  {
  }

  public InvalidDataException(String message)
  {
    super(message);
  }

  public InvalidDataException(Throwable cause)
  {
    super(cause);
  }

  public InvalidDataException(String message, Throwable cause)
  {
    super(message, cause);
  }

  public InvalidDataException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
  {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}