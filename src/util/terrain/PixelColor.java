package util.terrain;

/**
 * Representation of a pixel on a terrain image, speed is chosen from my preference on how an orienteer
 * would traverse that terrain
 *
 * @author Alex Wall (asw8675)
 */
public enum PixelColor {

    PAVED_ROAD(0x473303, "P", 3.8),
    OPEN_LAND(0xF89412, "O", 3.7),
    FOOTPATH(0x000000, "F", 3.6),
    EASY_MOVE_FOREST(0xFFFFFF, "E", 3.5),
    SLOW_RUN_FOREST(0x02D03C, "S", 3.0),
    ROUGH_MEADOW(0xFFC000, "R", 2.5),
    WALK_FOREST(0x028828, "W", 2.0),
    LAKE_SWAMP_MARSH(0x0000FF, "L", 1.0),
    IMPASSIBLE_VEGETATION(0x054918, "I", 0.01),
    OUT_OF_BOUNDS(0xCD0065, "B", 0.001),

    // For the fall season where FOOTPATHs are covered with leaves
    FALL_FOOTPATH(0x78788C, "A", 3.25),

    // For the spring season where pixels are now underwater
    MUD(0x8D4C00, "M", 1.5),

    // For the winter season where the water terrain freezes
    FROZEN_WATER(0x7BFFFF, "Z", 3.75),

    // ME, I am the orienteer with red pixels
    ME(0xFF0000, "X", 0.00);

    private static final int ALPHA_BYTES = 0xFF000000;
    public static final int RGB_BYTES = 0x00FFFFFF;

    /**
     * the hex color for a pixel
     */
    public final int hex;

    /**
     * the abbreviation of a pixel for debugging purposes
     */
    public final String abbrev;

    /**
     * the speed of the orienteer if they were to cross this pixel
     */
    public final Double speed;

    PixelColor(int hex, String abbrev, Double speed) {
        this.hex = hex;
        this.abbrev = abbrev;
        this.speed = speed;
    }

    public int getRGBFill() {
        return hex | ALPHA_BYTES;
    }

    /**
     * retrieve a pixel constant by its hex
     *
     * @param hex the hex number
     * @return a pixel constant
     * @throws IllegalArgumentException if the pixel cannot be found
     */
    public static PixelColor fromHex(int hex) {
        for (PixelColor pc : values()) {
            if (hex == pc.hex) {
                return pc;
            }
        }
        throw new IllegalArgumentException(String.format("No PixelColor found for \"%s\".", hex));
    }

}
