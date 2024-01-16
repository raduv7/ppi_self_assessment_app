package sas.model.exceptionHandling;

public enum ExceptionSingleton {
    INSTANCE;

    private final String message;

    ExceptionSingleton() {
        message = "ExceptionSingleton message called";
        System.out.println("ExceptionSingleton constructor called");
    }

    public void printMessage() {
        System.out.println(message);
    }

    public static void main(String[] args) {
        ExceptionSingleton.INSTANCE.printMessage();
    }
}
