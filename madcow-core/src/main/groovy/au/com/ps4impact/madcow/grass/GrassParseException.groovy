package au.com.ps4impact.madcow.grass

/**
 * A grass parser exception.
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
        failingGrass = line;
    }

    // extensive class!
}
