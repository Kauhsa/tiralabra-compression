package kauhsa;

import java.io.IOException;
import java.io.InputStream;
import kauhsa.compression.lzw.LZW;
import kauhsa.utils.PrintOutputStreamWrapper;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

public class LZWApp {

    private static final int DEFAULT_MAX_DICTIONARY_SIZE = 10000;

    /**
     * Run LZW encoder/decoder.
     * 
     * @param args command line arguments
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        Options options = getOptions();
        CommandLine line = getCommandLine(options, args);
        InputStream in = System.in;
        PrintOutputStreamWrapper out = new PrintOutputStreamWrapper(System.out);

        if (line.hasOption("help")) {
            printHelp(options);
        } else if (line.hasOption("encode")) {
            LZW.encode(in, out);
        } else if (line.hasOption("decode")) {
            LZW.decode(in, out);
        } else if (line.hasOption("encode-chunks")) {
            String valueString = line.getOptionValue("encode-chunks");
            int valueInt = valueString == null ? DEFAULT_MAX_DICTIONARY_SIZE : Integer.parseInt(valueString);
            LZW.encodeInChunks(in, out, valueInt);
        } else if (line.hasOption("decode-chunks")) {
            LZW.decodeChunks(in, out);
        } else {
            printHelp(options);
        }

        out.flush();
    }

    /**
     * Get CommandLine from command line arguments.
     * 
     * @param options Options to use
     * @param args command line arguments
     * @return CommandLine object
     */
    private static CommandLine getCommandLine(Options options, String[] args) {
        CommandLineParser parser = new PosixParser();
        CommandLine line = null;
        try {
            line = parser.parse(options, args);
        } catch (ParseException exp) {
            System.err.println("Parsing failed.  Reason: " + exp.getMessage());
        }
        return line;
    }

    /**
     * Get Options for this program.
     * 
     * @return Options for this program.
     * @throws IllegalArgumentException 
     */
    @SuppressWarnings("static-access")
    private static Options getOptions() throws IllegalArgumentException {
        Options options = new Options();
        options.addOption("h", "help", false, "Show this message.");
        options.addOption("d", "decode", false, "Decode data from STDIN.");
        options.addOption("D", "decode-chunks", false, "Decode data that has been encoded using --encode-chunks parameter.");
        options.addOption("e", "encode", false, "Encode data from STDIN.");
        options.addOption(OptionBuilder.withLongOpt("encode-chunks").hasOptionalArg().withArgName("SIZE").withDescription("Encode data with maximum LZW dictionary size. Default size is " + DEFAULT_MAX_DICTIONARY_SIZE + ".").create("E"));
        return options;
    }

    /**
     * Print help of command line arguments to System.out.
     * @param options Options to use for help text.
     */
    private static void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("lzw", options);
    }
}
