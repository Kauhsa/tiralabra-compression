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
        byte[] bytes = "oleoleoleoleoleole".getBytes();
        byte[] encoded = LZW.encode(bytes);
        
    }
}
