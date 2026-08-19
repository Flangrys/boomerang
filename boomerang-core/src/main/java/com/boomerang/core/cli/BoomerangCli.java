package com.boomerang.core.cli;

import org.apache.commons.cli.*;

public record BoomerangCli(
        Integer port, String host
) {
    private static final Options OPTIONS = new Options();

    private static final Option HELP_OPTION = Option
            .builder("h")
            .option("help")
            .desc("Shows this message")
            .get();

    private static final Option PORT_OPTION = Option
            .builder("p")
            .option("port")
            .hasArg()
            .argName("port")
            .type(Integer.class)
            .desc("Set the port in which the server will listen")
            .get();

    private static final Option HOST_OPTION = Option
            .builder()
            .option("host")
            .hasArg()
            .argName("port")
            .type(String.class)
            .desc("Se the host in which the server will listen")
            .get();

    private static final OptionGroup LOGGING_MODE_OPTION = new OptionGroup()
            .addOption(new Option(null, "verbose", false, "Prints extra logging messages."))
            .addOption(new Option(null, "debug", false, "Prints traces and other extra logging messages."))
            .addOption(new Option(null, "quiet", false, "Prints only warning messages an so on."));

    static {
        OPTIONS.addOption(HELP_OPTION);
        OPTIONS.addOption(PORT_OPTION);
        OPTIONS.addOptionGroup(LOGGING_MODE_OPTION);
    }

    public static BoomerangCli parse(String[] args) {
        try {
            final CommandLineParser parser = new DefaultParser();
            final CommandLine commands = parser.parse(OPTIONS, args);

            return new BoomerangCli(
                    commands.getParsedOptionValue(PORT_OPTION, 25565),
                    commands.getParsedOptionValue(HOST_OPTION, "localhost")
            );

        } catch (ParseException exc) {
            throw new RuntimeException("Cannot parse command line options", exc);
        }
    }
}
