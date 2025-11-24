package org.gitgud.data.posts;

import org.gitgud.core.model.Post;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class DonutFromC extends Post {
    static final int WIDTH = 69;
    static final int HEIGHT = 14;

    private final int contentWidth;
    private final int contentHeight;

    // Luminance characters from dim to bright
    private static final char[] LUMINANCE = {'.', ',', '-', '~', ':', ';', '=', '!', '*', '#', '$', '@'};

    // State for rotation angles
    private double A = 0;
    private double B = 0;

    // Scaling factors for projection
    private final double scaleX;
    private final double scaleY;
    private final int centerX;
    private final int centerY;

    // Reusable buffers
    private final double[] zBuffer;
    private final char[] outputBuffer;

    public DonutFromC(int width, int height) {
        this.contentWidth = width;
        this.contentHeight = height;
        int bufferSize = width * height;
        setDimensions(width, height);

        // Calculate dynamic scaling based on the original 80x22 aspect ratio
        // We take the minimum scale to ensure the donut always fits inside the box
        // independent of whether the box is tall/narrow or short/wide.
        double scaleW = width / 80.0;
        double scaleH = height / 22.0;
        double globalScale = Math.min(scaleW, scaleH);

        // 30 and 15 are the original projection constants for 80x22
        this.scaleX = 30.0 * globalScale;
        this.scaleY = 15.0 * globalScale;

        this.centerX = width / 2;
        this.centerY = height / 2;

        // Allocate buffers
        this.zBuffer = new double[bufferSize];
        this.outputBuffer = new char[bufferSize];

        // Initialize empty lines
        setLines(new ArrayList<>(height));
        String blankRow = " ".repeat(width);
        for (int i = 0; i < height; i++) {
            getLines().add(blankRow);
        }
    }

    public DonutFromC() {
        this(WIDTH, HEIGHT);
    }

    @Override
    public void update(double deltaTime) {
        A += 2.0 * deltaTime;
        B += deltaTime;

        // Clear Buffers
        Arrays.fill(outputBuffer, ' ');
        Arrays.fill(zBuffer, 0);

        double c, d, e, f, g, h, D, l, m, n, t;
        int x, y, o, N;

        for (double j = 0; j < 6.28; j += 0.07) {
            for (double i = 0; i < 6.28; i += 0.02) {

                c = Math.sin(i);
                d = Math.cos(j);
                e = Math.sin(A);
                f = Math.sin(j);
                g = Math.cos(A);
                h = d + 2;
                D = 1 / (c * h * e + f * g + 5);
                l = Math.cos(i);
                m = Math.cos(B);
                n = Math.sin(B);
                t = c * h * g - f * e;

                // Dynamic Projection
                // uses pre-calculated scales and centers
                x = (int) (centerX + scaleX * D * (l * h * m - t * n));
                y = (int) (centerY + scaleY * D * (l * h * n + t * m));

                o = x + contentWidth * y;

                N = (int) (8 * ((f * e - c * d * g) * m - c * d * e - f * g - l * d * n));

                if (y > 0 && y < contentHeight && x > 0 && x < contentWidth && D > zBuffer[o]) {
                    zBuffer[o] = D;
                    outputBuffer[o] = LUMINANCE[Math.max(N, 0)];
                }
            }
        }

        List<String> newFrame = new ArrayList<>(contentHeight);
        for (int row = 0; row < contentHeight; row++) {
            newFrame.add(new String(outputBuffer, row * contentWidth, contentWidth));
        }

        setLines(newFrame);
    }
}