package org.crashub.bash.repl;

import jline.Terminal;
import jline.TerminalFactory;
import jline.console.ConsoleReader;
import org.crashub.bash.Script;
import org.crashub.bash.spi.BaseContext;
import org.crashub.bash.spi.Context;
import org.crashub.bash.spi.Scope;
import org.crashub.bash.spi.SimpleScope;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * The base repl, useful for interactive testing of the shell.
 *
 * @author Julien Viet
 */
public class Main {

  public static void main(String[] args) throws Exception {
    Terminal term = TerminalFactory.create();
    final PrintStream out = System.out;
    PrintStream err = System.err;
    FileInputStream in = new FileInputStream(FileDescriptor.in);
    ConsoleReader reader = new ConsoleReader(null, in, out, term);
    Context context = new BaseContext(new BaseContext.CommandResolver() {
      @Override
      public BaseContext.Command resolveCommand(String command) {
        if ("echo".equals(command)) {
          return new BaseContext.Command() {
            @Override
            public Object execute(BaseContext context, Scope bindings, List<String> parameters, InputStream standardInput, OutputStream standardOutput) {
              PrintWriter writer = new PrintWriter(standardOutput, true);
              for (String parameter : parameters) {
                writer.println(parameter);
              }
              return null;
            }
          };
        } if ("grep".equals(command)) {
          return new BaseContext.Command() {
            @Override
            public Object execute(BaseContext context, Scope bindings, List<String> parameters, InputStream standardInput, OutputStream standardOutput) {
              PrintWriter writer = new PrintWriter(standardOutput, true);
              Pattern pattern = Pattern.compile(parameters.get(0));
              Scanner scanner = new Scanner(standardInput);
              while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (pattern.matcher(line).find()) {
                  writer.println(line);
                }
              }
              return null;
            }
          };
        } else if ("sort".equals(command)) {
          return new BaseContext.Command() {
            @Override
            public Object execute(BaseContext context, Scope bindings, List<String> parameters, InputStream standardInput, OutputStream standardOutput) {
              PrintWriter writer = new PrintWriter(standardOutput, true);
              ArrayList<String> lines = new ArrayList<String>();
              Scanner scanner = new Scanner(standardInput);
              while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                lines.add(line);
              }
              Collections.sort(lines);
              for (String line : lines) {
                writer.println(line);
              }
              return null;
            }
          };
        } else {
          return null;
        }
      }
    }, System.out);
    SimpleScope globalScope = new SimpleScope();
    while (true) {
      String s = reader.readLine("> ");
      Script script = new Script(s);
      try {
        Object ret = script.eval(globalScope, context);
        if (ret != null) {
          out.print("-> ");
          out.println(ret);
        }
      }
      catch (Exception e) {
        e.printStackTrace(err);
      }
    }
  }

}
