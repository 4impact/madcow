package au.com.ps4impact.madcow.grass

/**
 * A grass parser exception.
 *
 * @author Gavin Bunney
 */
class GrassParseException extends Exception {
    
    String failingGrass;

    GrassParseException(String message)
    {
        this(null, message);
    }

    GrassParseException(String line, String message)
    {
        super(message);
        this.failingGrass = line;
    }

    // extensive class!
}
