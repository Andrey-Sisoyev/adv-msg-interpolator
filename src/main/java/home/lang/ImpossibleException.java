package home.lang;

public class ImpossibleException extends RuntimeException {

    // ================================
    // CONSTRUCTORS

    public ImpossibleException() {
    }

    public ImpossibleException(String s) {
        super(s);
    }

    public ImpossibleException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ImpossibleException(Throwable throwable) {
        super(throwable);
    }

    // ================================
    // GETTERS/SETTERS

    // ================================
    // METHODS

}
