package Commands;


import net.dv8tion.jda.api.hooks.ListenerAdapter;

/*
This file has been created ahead of time for the refactor
the commands will eventually been handeld by a command handler from JDA-Utilities
This will make it easy to split up the commands and the code more readable.

Also the refactor hasnt happened because Gradle doesnt really like singletons.
Gradle can access a package that is lower than the defined singleton (Direct access. Both in the same directory)
Gradle cant access a singleton if it is up an directory (Commands -> Java, where the singleton is placed cannot be accesed from Commands)
So we need to have a database first before a refactor can happen.
 */
public class ScrumCommands extends ListenerAdapter {
}
