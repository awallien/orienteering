package util.season;

/**
 * @author Alex Wall (asw8675)
 */
public enum Season {

    WINTER("winter"),
    SUMMER("summer"),
    FALL("fall"),
    SPRING("spring");

    Season(String season){ }

    /**
     * retrieve the Season type from the given string
     * @param season the string
     * @return the Season constant type
     * @throws IllegalArgumentException if the season for string is not found
     */
    public static Season fromString(String season) {
        for(Season s: Season.values()) {
            if (season.equalsIgnoreCase(s.name())){
                return s;
            }
        }
        throw new IllegalArgumentException(String.format("No season found for \"%s\".", season));
    }
}
