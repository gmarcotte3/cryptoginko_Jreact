package com.marcotte.blockhead.util;

public class BlockHeadException extends Exception
{
  public BlockHeadException()
  {
    super();
  }


  public BlockHeadException(String errormessage)
  {
    super(errormessage);
  }

  public BlockHeadException(String errormessage, Throwable e)
  {
    super(errormessage, e);
  }
}
