package kauhsa.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Wrapper that enables use of PrintStream as OutputStream.
 */
public class PrintOutputStreamWrapper extends OutputStream {
    private final PrintStream ps;
    
    /**
     * Create new PrintOutputStreamWrapper.
     * 
     * @param ps PrintStream where to output.
     */
    public PrintOutputStreamWrapper(PrintStream ps) {
        this.ps = ps;
    }    

    @Override
    public void write(int i) throws IOException {
        ps.write(i);
    }
    
    @Override
    public void close() {
        ps.close();
    }
    
    @Override
    public void flush() {
        ps.flush();
    }
}
