package kauhsa;

import java.io.IOException;
import kauhsa.compression.lzw.LZW;
import kauhsa.utils.PrintOutputStreamWrapper;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class LZWApp {

    public static void main(String[] args) throws IOException {
        Options options = getOptions();
        CommandLine line = getCommandLine(options, args);
        PrintOutputStreamWrapper out = new PrintOutputStreamWrapper(System.out);
        
        if (line.hasOption("help")) {
            printHelp(options);
        } else if (line.hasOption("encode")) {
            LZW.encode(System.in, out);
        } else if (line.hasOption("decode")) {
            LZW.decode(System.in, out);
        } else {
            printHelp(options);
        }
        
        out.flush();
    }

    private static CommandLine getCommandLine(Options options, String[] args) {
        CommandLineParser parser = new GnuParser();
        CommandLine line = null;
        try {
            line = parser.parse(options, args);
        } catch (ParseException exp) {
            System.err.println("Parsing failed.  Reason: " + exp.getMessage());
        }
        return line;
    }

    private static Options getOptions() throws IllegalArgumentException {
        Options options = new Options();
        Option help = new Option("help", "print this message");
        options.addOption(help);
        Option decode = new Option("decode", "decode data from stdin");
        options.addOption(decode);
        Option encode = new Option("encode", "encode data from stdin");
        options.addOption(encode);
        return options;
    }

    private static void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("lzw", options);
    }
}
