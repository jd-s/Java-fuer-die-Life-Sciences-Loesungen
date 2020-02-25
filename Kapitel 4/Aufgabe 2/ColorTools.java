package de.bit.pl2.ex10;

import org.apache.commons.lang3.StringUtils;

public class ColorTools {
    public static void printClassAndCompl(String hex) {
        int[] rgb = HexToRGB(hex);
        float[] hsl = RGBtoHSL(rgb);
        String colClass = HSLToString(hsl);
        float[] complHsl = getComplHSL(hsl);
        String compl = HSLToString(complHsl);
        String complHex = RGBToHex(HSLtoRGB(complHsl));
        System.out.println(String.format("%s belongs to color class %s and its complementary color is %s " +
                "(color class %s).", hex, colClass, complHex, compl));
    }


    public static int[] HexToRGB(String hex) {
        int[] color = new int[3];
        int j = 0;
        for (int i = 0; i < hex.length(); i += 2) {
            color[j] = Integer.parseInt(hex.substring(i, i + 2), 16);
            j += 1;
        }
        return color;
    }

    public static String RGBToHex(int[] color) {
        String hex = "";
        for (int col : color) {
            hex += StringUtils.leftPad(Integer.toHexString(col), 2, '0');
        }
        return hex.toUpperCase();
    }

    public static String HSLToString(float[] myColor) {
        float h = myColor[0];
        float s = myColor[1];
        float l = myColor[2];

        if (l < 20) return "black";
        if (l > 80) return "white";

        if (s < 25) return "grey";

        if (h < 60) return "red";
        if (h < 180) return "green";
        if (h < 300) return "blue";
        return "red";
    }

    /**
     * Convert RGB values to HSL
     * Adapted from http://www.camick.com/java/source/HSLColor.java
     *
     * @param color integer list with RGB values
     * @return float list with HSL values
     */
    public static float[] RGBtoHSL(int[] color) {
        float r = color[0] / 255.0f;
        float g = color[1] / 255.0f;
        float b = color[2] / 255.0f;

        float max = Math.max(Math.max(r, g), b);
        float min = Math.min(Math.min(r, g), b);

        // hue
        float h = 0;
        if (max == min)
            h = 0;
        else if (max == r)
            h = ((60 * (g - b) / (max - min)) + 360) % 360;
        else if (max == g)
            h = (60 * (b - r) / (max - min)) + 120;
        else if (max == b)
            h = (60 * (r - g) / (max - min)) + 240;

        // luminance
        float l = (max + min) / 2;

        // saturation
        float s = 0;
        if (max == min)
            s = 0;
        else if (l <= .5f)
            s = (max - min) / (max + min);
        else
            s = (max - min) / (2 - max - min);

        return new float[]{h, s * 100, l * 100};
    }

    /**
     * Returns the complementary color by replacing hue with the opposite hue
     *
     * @param color float list with HSL values
     * @return float list with complementary HSL values
     */
    public static float[] getComplHSL(float[] color) {
        color[0] += 180;
        if (color[0] > 360) {
            color[0] -= 360;
        }
        color[2] = 100 - color[2];
        return color;
    }

    /**
     * Convert HSL values to RGB
     * Adapted from http://stackoverflow.com/a/9493060/4939630
     *
     * @param color float list with HSL values
     * @return integer list with RGB values
     */
    public static int[] HSLtoRGB(float[] color) {
        float h = color[0];
        float s = color[1];
        float l = color[2];

        h /= 360f;
        s /= 100f;
        l /= 100f;

        float q = 0;

        if (l < 0.5)
            q = l * (1 + s);
        else
            q = (l + s) - (s * l);

        float p = 2 * l - q;

        float r = Math.max(0, HueToRGB(p, q, h + (1.0f / 3.0f)));
        float g = Math.max(0, HueToRGB(p, q, h));
        float b = Math.max(0, HueToRGB(p, q, h - (1.0f / 3.0f)));

        r = Math.min(r, 1.0f);
        g = Math.min(g, 1.0f);
        b = Math.min(b, 1.0f);

        return new int[]{(int) (r * 255 + 0.5), (int) (g * 255 + 0.5), (int) (b * 255 + 0.5)};
    }

    private static float HueToRGB(float p, float q, float t) {
        if (t < 0) {
            t += 1;
        }
        if (t > 1) {
            t -= 1;
        }
        if (6 * t < 1) return p + (q - p) * 6 * t;
        if (2 * t < 1) return q;
        if (3 * t < 2) return p + (q - p) * (2.0f / 3.0f - t) * 6;
        return p;
    }
}
