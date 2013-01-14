package kauhsa;

import kauhsa.compression.lzw.LZW;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        byte[] bytes = "hehheh".getBytes();
        LZW.encode(bytes);
    }
}
