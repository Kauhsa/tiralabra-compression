package kauhsa.utils.dummyoutputstream;

import java.io.OutputStream;

/**
 * OutputStream that does not write anywhere.
 */
public class DummyOutputStream extends OutputStream {

    /**
     * Pretend to write byte.
     *
     * @param i byte to write
     */
    @Override
    public void write(int i) {
    }
}
