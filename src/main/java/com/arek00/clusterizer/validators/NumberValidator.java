package com.arek00.clusterizer.validators;


public class NumberValidator {

    public static void isPositive(String errorMessage, int... arguments) {
        for (int argument : arguments) {
            if(argument < 0) {
                throw new IllegalArgumentException(errorMessage);
            }
        }
    }

    public static void isPositive(String errorMessage, double... arguments) {
        for (double argument : arguments) {
            if(argument < 0) {
                throw new IllegalArgumentException(errorMessage);
            }
        }
    }

    public static void isNegative(String errorMessage, int... arguments) {
        for (int argument : arguments) {
            if(argument >= 0) {
                throw new IllegalArgumentException(errorMessage);
            }
        }
    }

    public static void isNegative(String errorMessage, double... arguments) {
        for (double argument : arguments) {
            if(argument >= 0) {
                throw new IllegalArgumentException(errorMessage);
            }
        }
    }

    public static void inRange(String errorMessage, int min, int max, int... arguments) {
        for (int argument : arguments) {
            if(argument < min || argument > max) {
                throw new IllegalArgumentException(errorMessage);
            }
        }
    }

    public static void inRange(String errorMessage, double min, double max, double... arguments) {
        for (double argument : arguments) {
            if(argument < min || argument > max) {
                throw new IllegalArgumentException(errorMessage);
            }
        }
    }

    public static void lesserThan(String errorMessage, int statement, int... arguments) {
        for (int argument : arguments) {
            if(argument > statement) {
                throw new IllegalArgumentException(errorMessage);
            }
        }
    }

    public static void lesserThan(String errorMessage, double statement, double... arguments) {
        for (double argument : arguments) {
            if(argument > statement) {
                throw new IllegalArgumentException(errorMessage);
            }
        }
    }

    public static void greaterThan(String errorMessage, int statement, int... arguments) {
        for (int argument : arguments) {
            if(argument > statement) {
                throw new IllegalArgumentException(errorMessage);
            }
        }
    }

    public static void greaterThan(String errorMessage, double statement, double... arguments) {
        for (double argument : arguments) {
            if(argument > statement) {
                throw new IllegalArgumentException(errorMessage);
            }
        }
    }

    public static void equals(String errorMessage, int statement, int... arguments) {
        for (int argument : arguments) {
            if(argument != statement) {
                throw new IllegalArgumentException(errorMessage);
            }
        }
    }

    public static void equals(String errorMessage, double statement, double... arguments) {
        for (double argument : arguments) {
            if(argument != statement) {
                throw new IllegalArgumentException(errorMessage);
            }
        }
    }


}
