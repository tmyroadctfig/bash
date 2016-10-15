package org.crashub.bash.ir;

import org.crashub.bash.spi.Context;
import org.crashub.bash.spi.Scope;

/**
 * @author Luke Quinane
 */
public class REDIR extends Node {

  /**
   * The command to evaluate.
   */
  private final Node command;

  /**
   * The redirect.
   */
  private final STRING redir;

  /**
   * The evaluated redirect.
   */
  private String redirect;

  /**
   * Whether this redirect is an append operation.
   */
  private boolean append;

  public REDIR(Node command, STRING redir, boolean append) {
    this.command = command;
    this.redir = redir;
    this.append = append;
  }

  @Override
  public Object eval(Scope bindings, Context context) {
    command.eval(bindings, context);
    redirect = redir.eval(bindings, context);
    return this;
  }

  public String getRedirect() {
    return redirect;
  }

  public boolean isAppend() {
    return append;
  }
}
