package org.crashub.bash.spi;

/**
 * An exception thrown when a command cannot be found.
 */
public class CommandNotFoundException extends RuntimeException {

    /**
     * The name of the command which could not be found.
     */
    private final String commandName;

    /**
     * Creates a new exception.
     *
     * @param commandName the name of the command which could not be found.
     */
    public CommandNotFoundException(String commandName) {
        super(commandName + ": command not found");
        this.commandName = commandName;
    }

    /**
     * Gets the command name.
     *
     * @return the command name.
     */
    public String getCommandName() {
        return commandName;
    }
}
