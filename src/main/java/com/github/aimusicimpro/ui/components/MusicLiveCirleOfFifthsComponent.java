package com.github.aimusicimpro.ui.components;

import com.github.aimusicimpro.core.music.theory.Music;
import com.github.aimusicimpro.core.music.theory.Note;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representing the Circle of Fifth
 * We will draw a cycle with several layers, from the center to exterior :
 * - Minor Degree
 * - Minor Keys
 * - Major Keys
 * - Major Degree
 *
 * @author MowMow
 */
public class MusicLiveCirleOfFifthsComponent extends JComponent {

    /**
     * List of major keys of the circle of fifths in the right order
     */
    public static final String[] MAJOR_KEYS = Music.CircleOfFifths.get("major");

    // CONSTANTS
    /**
     * List of minor keys of the circle of fifths in the right order
     */
    public static final String[] MINOR_KEYS = Music.CircleOfFifths.get("minor");
    /**
     * List of degrees in the right order
     */
    public static final String[] MAJOR_DEGREES = {"IV", "I", "V", "ii", "vi", "iii", "iv°"};
    /**
     * List of degrees in the right order
     */
    public static final String[] MINOR_DEGREES = {"VI", "III", "VII", "iv", "i", "v", "ii°"};
    /**
     * Number of keys major & minor
     */
    public static final int NB_KEYS = MAJOR_KEYS.length;
    /**
     * Number of Degrees
     */
    public static final int NB_DEGREES = MAJOR_DEGREES.length;
    /**
     * Black Border Size
     */
    public static final double CIRCLE_SIZE_BORDER = 0.00;
    /**
     * Circle Size Major Degree
     */
    public static final double CIRCLE_SIZE_MAJOR_DEGREE = 0.95;
    /**
     * Circle Size Major Key
     */
    public static final double CIRCLE_SIZE_MAJOR_KEY = 0.85;
    /**
     * Circle Size Minor Key
     */
    public static final double CIRCLE_SIZE_MINOR_KEY = 0.55;
    /**
     * Circle Size Minor Degree
     */
    public static final double CIRCLE_SIZE_MINOR_DEGREE = 0.40;
    /**
     * Circle Size Minor Degree
     */
    public static final double CIRCLE_INTERIOR = 0.30;
    /**
     * Preferred height
     */
    public static final int PREFERRED_HEIGHT = 500;
    /**
     * Preferred width
     */
    public static final int PREFERRED_WIDTH = 500;
    /**
     * Step to create the arc shape with acceptable precision
     */
    public static final double ARC_STEP = Math.PI / 50;
    /**
     * Colors for all circles
     */
    public static final Color COLOR_BACKGROUND = new Color(187, 188, 193);
    public static final Color COLOR_BORDER = new Color(0, 0, 0);

    // COLORS
    public static final Color COLOR_KEYS = new Color(255, 255, 255);
    public static final Color COLOR_DEGREES = new Color(253, 253, 253);
    /**
     * Color for split lines
     */
    public static final Color COLOR_SPLIT_LINES = new Color(40, 40, 40);
    /**
     * Color for arc chords
     */
    public static final Color COLOR_MAJOR_CHORDS = new Color(243, 209, 209);
    public static final Color COLOR_MINOR_CHORDS = new Color(201, 232, 249);
    public static final Color COLOR_DOMINANT_CHORDS = new Color(215, 246, 215);
    /**
     * Color of the characters
     */
    public static final Color COLOR_KEYS_CHAR = new Color(0, 0, 0);
    public static final Color COLOR_RIGHT_KEY_CHAR = Color.red;
    public static final double SIZE_MAJOR_KEYS = 24.0 / 500.0;
    public static final double SIZE_MINOR_KEYS = 15.0 / 500.0;
    public static final double SIZE_MAJOR_DEGREE = 15.0 / 500.0;
    public static final double SIZE_MINOR_DEGREE = 12.0 / 500.0;

    // DATA MEMBERS

    private Note key;

    /**
     * Basic constructor
     */
    public MusicLiveCirleOfFifthsComponent() {
        super();
        key = new Note("C");
    }

    /**
     * Quick testing
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("Circle of Fifths");
        MusicLiveCirleOfFifthsComponent cof = new MusicLiveCirleOfFifthsComponent();

        frame.setSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));
        frame.add(cof);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }


    @Override
    public Dimension getPreferredSize() {
        return new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();
        draw(key, g2d);
    }

    /**
     * Draw a key note
     */
    public void draw(Note notetone, Graphics2D bufferedGraphics) {
        // draw background
        bufferedGraphics.setColor(COLOR_BACKGROUND);
        bufferedGraphics.fillRect(0, 0, getWidth(), getHeight());

        // draw circles background
        drawCirclesBackground(bufferedGraphics);

        // paint the chord with the right colors
        drawMajArcChords(notetone, bufferedGraphics);
        drawMinArcChords(notetone, bufferedGraphics);
        drawDominantArcChords(notetone, bufferedGraphics);

        // draw the circle borders
        drawCircleBorders(bufferedGraphics);

        // split circles
        splitCircles(bufferedGraphics);

        // draw all the letters
        drawKeys(notetone, bufferedGraphics);

        // draw degrees
        drawMajDegrees(notetone, bufferedGraphics);
        drawMinDegrees(notetone, bufferedGraphics);

        // repaint
        repaint();
    }

    /**
     * Spilt all circles with 12 lines
     */
    private void splitCircles(Graphics bufferedGraphics) {

        for (int i = 0; i < NB_KEYS; i++) {

            // compute current angle
            double deltaAngle = Math.PI * 2 / NB_KEYS;
            double angle = deltaAngle * i + deltaAngle / 2;
            double cos = Math.cos(angle);
            double sin = Math.sin(angle);

            // center
            int centerx = getWidth() / 2;
            int centery = getHeight() / 2;

            // compute start & end point
            int startx = (int) (centerx + cos * CIRCLE_INTERIOR * getWidth() / 2);
            int starty = (int) (centery + sin * CIRCLE_INTERIOR * getHeight() / 2);

            int endx = (int) (centerx + cos * CIRCLE_SIZE_MAJOR_DEGREE * getWidth() / 2);
            int endy = (int) (centery + sin * CIRCLE_SIZE_MAJOR_DEGREE * getHeight() / 2);

            bufferedGraphics.setColor(COLOR_SPLIT_LINES);
            bufferedGraphics.drawLine(startx, starty, endx, endy);

        }

    }

    /**
     * Draw the circle borders
     */
    private void drawCircleBorders(Graphics2D bufferedGraphics) {

        // border
        double size = CIRCLE_SIZE_BORDER + CIRCLE_SIZE_MAJOR_DEGREE + 1;
        bufferedGraphics.setStroke(new BasicStroke(2));
        drawCenteredCircle(size, COLOR_BACKGROUND, bufferedGraphics);

        // border
        bufferedGraphics.setStroke(new BasicStroke(1));
        size = CIRCLE_SIZE_MAJOR_KEY + CIRCLE_SIZE_BORDER;
        drawCenteredCircle(size, COLOR_BORDER, bufferedGraphics);

        // border
        bufferedGraphics.setStroke(new BasicStroke(2));
        size = CIRCLE_SIZE_MINOR_KEY + CIRCLE_SIZE_BORDER;
        drawCenteredCircle(size, COLOR_BORDER, bufferedGraphics);

        // border
        bufferedGraphics.setStroke(new BasicStroke(1));
        size = CIRCLE_SIZE_MINOR_KEY + CIRCLE_SIZE_BORDER + 1;
        drawCenteredCircle(size, COLOR_BORDER, bufferedGraphics);

        // border
        bufferedGraphics.setStroke(new BasicStroke(1));
        size = CIRCLE_SIZE_MINOR_DEGREE + CIRCLE_SIZE_BORDER;
        drawCenteredCircle(size, COLOR_BORDER, bufferedGraphics);

        // border
        bufferedGraphics.setStroke(new BasicStroke(1));
        size = CIRCLE_INTERIOR + CIRCLE_SIZE_BORDER;
        drawCenteredCircle(size, COLOR_BORDER, bufferedGraphics);

    }

    /**
     * Draw all the chords with a specific color
     */
    private void drawMajArcChords(Note keyNoteMajor, Graphics2D bufferedGraphics) {
        int keyindex = getKeyIndex(keyNoteMajor);
        if(keyindex == -1)
            return;

        // constants
        double deltaAngle = Math.PI * 2 / NB_KEYS;
        double angleOffset = -deltaAngle / 2 - Math.PI / 2;


        // Draw 1
        // Major Major side
        int[] majorchords = computeRange(keyindex, 1, 4);

        // compute angle
        double minangle = deltaAngle * majorchords[0] + angleOffset;
        double maxangle = deltaAngle * majorchords[majorchords.length - 1] + angleOffset;

        // draw
        fillArc(COLOR_MAJOR_CHORDS,
                minangle,
                maxangle,
                CIRCLE_SIZE_MINOR_KEY,
                CIRCLE_SIZE_MAJOR_DEGREE, bufferedGraphics);


        // Draw 2
        // Major minor side
        majorchords = computeRange(keyindex, 4, 4);

        // compute angle
        minangle = deltaAngle * majorchords[0] + angleOffset;
        maxangle = deltaAngle * majorchords[majorchords.length - 1] + angleOffset;

        // draw
        fillArc(COLOR_MAJOR_CHORDS,
                minangle,
                maxangle,
                CIRCLE_SIZE_MINOR_DEGREE,
                CIRCLE_SIZE_MINOR_KEY, bufferedGraphics);

    }

    /**
     * Draw all the chords with a specific color
     */
    private void drawMinArcChords(Note keyNoteMajor, Graphics2D bufferedGraphics) {
        int keyindex = getKeyIndex(keyNoteMajor);
        if(keyindex == -1)
            return;

        // constants
        double deltaAngle = Math.PI * 2 / NB_KEYS;
        double angleOffset = -deltaAngle / 2 - Math.PI / 2;


        // Draw 1
        // Major Major side
        int[] minorchords = computeRange(keyindex, -2, 4);

        // compute angle
        double minangle = deltaAngle * minorchords[0] + angleOffset;
        double maxangle = deltaAngle * minorchords[minorchords.length - 1] + angleOffset;

        // draw
        fillArc(COLOR_MINOR_CHORDS,
                minangle,
                maxangle,
                CIRCLE_SIZE_MINOR_KEY,
                CIRCLE_SIZE_MAJOR_DEGREE, bufferedGraphics);


        // Draw 2
        // Major minor side
        minorchords = computeRange(keyindex, 1, 4);

        // compute angle
        minangle = deltaAngle * minorchords[0] + angleOffset;
        maxangle = deltaAngle * minorchords[minorchords.length - 1] + angleOffset;

        // draw
        fillArc(COLOR_MINOR_CHORDS,
                minangle,
                maxangle,
                CIRCLE_SIZE_MINOR_DEGREE,
                CIRCLE_SIZE_MINOR_KEY, bufferedGraphics);

    }

    /**
     * Draw all the chords with a specific color
     */
    private void drawDominantArcChords(Note keyNoteMajor, Graphics2D bufferedGraphics) {
        int keyindex = getKeyIndex(keyNoteMajor);
        if(keyindex == -1)
            return;

        // constants
        double deltaAngle = Math.PI * 2 / NB_KEYS;
        double angleOffset = -deltaAngle / 2 - Math.PI / 2;


        // Draw 1
        // Dominant Major side
        int[] minorchords = computeRange(keyindex, -5, 2);

        // compute angle
        double minangle = deltaAngle * minorchords[0] + angleOffset;
        double maxangle = deltaAngle * minorchords[minorchords.length - 1] + angleOffset;

        // draw
        fillArc(COLOR_DOMINANT_CHORDS,
                minangle,
                maxangle,
                CIRCLE_SIZE_MINOR_KEY,
                CIRCLE_SIZE_MAJOR_DEGREE, bufferedGraphics);


        // Draw 2
        // Dominant minor side
        minorchords = computeRange(keyindex, -1, 2);

        // compute angle
        minangle = deltaAngle * minorchords[0] + angleOffset;
        maxangle = deltaAngle * minorchords[minorchords.length - 1] + angleOffset;

        // draw
        fillArc(COLOR_DOMINANT_CHORDS,
                minangle,
                maxangle,
                CIRCLE_SIZE_MINOR_DEGREE,
                CIRCLE_SIZE_MINOR_KEY, bufferedGraphics);

    }

    /**
     * Fill a arc with a specific color
     */
    private void fillArc(Color c,
                         double startAngle,
                         double endAngle,
                         double minSize,
                         double maxSize, Graphics2D bufferedGraphics) {

        List<Integer> xs = new ArrayList<>();
        List<Integer> ys = new ArrayList<>();

        int minSizeX = (int) (minSize * getWidth() / 2);
        int maxSizeX = (int) (maxSize * getWidth() / 2);
        int minSizeY = (int) (minSize * getHeight() / 2);
        int maxSizeY = (int) (maxSize * getHeight() / 2);

        // center
        int centerx = getWidth() / 2;
        int centery = getHeight() / 2;

        // First point ( bottom left )
        xs.add(centerx + (int) (minSizeX * Math.cos(startAngle)));
        ys.add(centery + (int) (minSizeY * Math.sin(startAngle)));

        // Second point ( top left )
        xs.add(centerx + (int) (maxSizeX * Math.cos(startAngle)));
        ys.add(centery + (int) (maxSizeY * Math.sin(startAngle)));

        // Arc on top
        for (double angle = startAngle; angle < endAngle; angle += ARC_STEP) {
            xs.add(centerx + (int) (maxSizeX * Math.cos(angle)));
            ys.add(centery + (int) (maxSizeY * Math.sin(angle)));
        }

        // Third point ( top right )
        xs.add(centerx + (int) (maxSizeX * Math.cos(endAngle)));
        ys.add(centery + (int) (maxSizeY * Math.sin(endAngle)));

        // Forth point ( bottom right )
        xs.add(centerx + (int) (minSizeX * Math.cos(endAngle)));
        ys.add(centery + (int) (minSizeY * Math.sin(endAngle)));

        // Arc on bottom
        for (double angle = endAngle; angle > startAngle; angle -= ARC_STEP) {
            xs.add(centerx + (int) (minSizeX * Math.cos(angle)));
            ys.add(centery + (int) (minSizeY * Math.sin(angle)));
        }

        int n = xs.size();
        int[] axs = new int[n];
        int[] ays = new int[n];
        for (int i = 0; i < n; i++) {
            axs[i] = xs.get(i);
            ays[i] = ys.get(i);
        }

        // Build Shape
        Shape shape = new Polygon(axs, ays, n);

        // Draw Shape
        bufferedGraphics.setColor(c);
        bufferedGraphics.fill(shape);

    }

    /**
     * Draws all the Key Letters
     */
    private void drawKeys(Note keyNoteMajor, Graphics bufferedGraphics) {

        int keyindex = getKeyIndex(keyNoteMajor);

        for (int i = 0; i < NB_KEYS; i++) {

            // compute current angle
            double deltaAngle = Math.PI * 2 / NB_KEYS;
            double startAngle = -Math.PI / 2;
            double angle = startAngle + deltaAngle * i;
            double cos = Math.cos(angle);
            double sin = Math.sin(angle);

            // center
            int centerx = getWidth() / 2;
            int centery = getHeight() / 2;

            // MAJ

            // compute start & end point
            double majsize = (CIRCLE_SIZE_MAJOR_KEY + CIRCLE_SIZE_MINOR_KEY) / 2;
            int majx = (int) (centerx + cos * majsize * getWidth() / 2);
            int majy = (int) (centery + sin * majsize * getHeight() / 2);

            // adjust with the size of keys
            majx += -getFontSize(SIZE_MAJOR_KEYS) / 2;
            majy += getFontSize(SIZE_MAJOR_KEYS) / 2;

            // Compute char
            String majorkey = MAJOR_KEYS[i];
            char[] data = majorkey.toCharArray();
            int length = data.length;

            bufferedGraphics.setFont(new Font("TimesRoman", Font.BOLD, getFontSize(SIZE_MAJOR_KEYS)));
            bufferedGraphics.setColor(COLOR_KEYS_CHAR);
            if (i == keyindex)
                bufferedGraphics.setColor(COLOR_RIGHT_KEY_CHAR);
            bufferedGraphics.drawChars(data, 0, length, majx, majy);


            // MIN

            // compute start & end point
            double minsize = (CIRCLE_SIZE_MINOR_KEY + CIRCLE_SIZE_MINOR_DEGREE) / 2;
            int minx = (int) (centerx + cos * minsize * getWidth() / 2);
            int miny = (int) (centery + sin * minsize * getHeight() / 2);

            // adjust with the size of keys
            minx += -getFontSize(SIZE_MINOR_KEYS) / 2;
            miny += getFontSize(SIZE_MINOR_KEYS) / 2;

            // Compute char
            String minorKey = MINOR_KEYS[i];
            data = minorKey.toCharArray();
            length = data.length;
            bufferedGraphics.setFont(new Font("TimesRoman", Font.PLAIN, getFontSize(SIZE_MINOR_KEYS)));
            bufferedGraphics.setColor(COLOR_KEYS_CHAR);
            if (i == keyindex)
                bufferedGraphics.setColor(COLOR_RIGHT_KEY_CHAR);
            bufferedGraphics.drawChars(data, 0, length, minx, miny);

        }

    }

    /**
     * Compute the index of the input key
     */
    private int getKeyIndex(Note keyNoteMajor) {
        if (keyNoteMajor == null)
            return -1;
        int index = 0;
        Note current = new Note(MAJOR_KEYS[index]);
        while (index < NB_KEYS && !current.equals(keyNoteMajor)) {
            current = new Note(MAJOR_KEYS[index]);
            index++;
        }
        return index - 1;
    }

    /**
     * Compute the list of case of specific size around the keyindex with a specific offset
     */
    private int[] computeRange(int keyindex, int offset, int size) {
        int[] range = new int[size];
        int index = 0;
        for (int i = keyindex - offset; i < keyindex + size - offset; i++) {
            range[index] = i;
            index++;
        }
        return range;
    }

    /**
     * Draw degrees ( I, II, III, IV ... )
     * for major only
     */
    private void drawMajDegrees(Note keyNoteMajor, Graphics bufferedGraphics) {
        int keyindex = getKeyIndex(keyNoteMajor);
        int majoffset = 1;
        int[] majorlist = computeRange(keyindex, majoffset, NB_DEGREES);

        // loop on the major list
        drawDegree(bufferedGraphics, keyindex, majorlist, CIRCLE_SIZE_MAJOR_KEY, CIRCLE_SIZE_MAJOR_DEGREE,
                SIZE_MAJOR_DEGREE,
                MAJOR_DEGREES);
    }

    private void drawDegree(Graphics bufferedGraphics, int keyindex, int[] majorlist, double circleSizeMajorKey,
                            double circleSizeMajorDegree, double sizeMajorDegree, String[] majorDegrees) {
        for (int j = 0; j < majorlist.length; j++) {
            int i = majorlist[j];

            // compute current angle
            double deltaAngle = Math.PI * 2 / NB_KEYS;
            double startAngle = -Math.PI / 2;
            double angle = startAngle + deltaAngle * i;
            double cos = Math.cos(angle);
            double sin = Math.sin(angle);

            // center
            int centerx = getWidth() / 2;
            int centery = getHeight() / 2;

            // compute start & end point
            double majsize = (circleSizeMajorKey + circleSizeMajorDegree) / 2;
            int majx = (int) (centerx + cos * majsize * getWidth() / 2);
            int majy = (int) (centery + sin * majsize * getHeight() / 2);

            // adjust with the size of keys
            majx += -getFontSize(sizeMajorDegree) / 2;
            majy += getFontSize(sizeMajorDegree) / 2;

            // Compute char
            String majorkey = majorDegrees[j];
            char[] data = majorkey.toCharArray();
            int length = data.length;

            bufferedGraphics.setFont(new Font("TimesRoman", Font.ITALIC, getFontSize(sizeMajorDegree)));
            bufferedGraphics.setColor(COLOR_KEYS_CHAR);
            if (i == keyindex)
                bufferedGraphics.setColor(COLOR_RIGHT_KEY_CHAR);
            bufferedGraphics.drawChars(data, 0, length, majx, majy);
        }
    }

    /**
     * Draw degrees ( I, II, III, IV ... )
     * for minor only
     */
    private void drawMinDegrees(Note keyNoteMajor, Graphics bufferedGraphics) {
        int keyindex = getKeyIndex(keyNoteMajor);
        int minoffset = 4;
        int[] minorlist = computeRange(keyindex, minoffset, NB_DEGREES);

        int index = 0;
        for (int i = keyindex - minoffset; i < keyindex + NB_DEGREES - minoffset; i++) {
            minorlist[index] = i;
            index++;
        }

        // loop on the major list
        drawDegree(bufferedGraphics, keyindex, minorlist, CIRCLE_INTERIOR, CIRCLE_SIZE_MINOR_DEGREE, SIZE_MINOR_DEGREE,
                MINOR_DEGREES);

    }

    private void drawCirclesBackground(Graphics bufferedGraphics) {

        // major degree
        double size = CIRCLE_SIZE_MAJOR_DEGREE;
        fillCenteredCircle(size, COLOR_DEGREES, bufferedGraphics);

        // major keys
        size = CIRCLE_SIZE_MAJOR_KEY;
        fillCenteredCircle(size, COLOR_KEYS, bufferedGraphics);

        // minor keys
        size = CIRCLE_SIZE_MINOR_KEY;
        fillCenteredCircle(size, COLOR_KEYS, bufferedGraphics);

        // minor degree
        size = CIRCLE_SIZE_MINOR_DEGREE;
        fillCenteredCircle(size, COLOR_DEGREES, bufferedGraphics);

        // interior
        size = CIRCLE_INTERIOR;
        fillCenteredCircle(size, COLOR_BACKGROUND, bufferedGraphics);


    }

    private void fillCenteredCircle(double size, Color color, Graphics bufferedGraphics) {
        bufferedGraphics.setColor(color);
        bufferedGraphics.fillOval(
                (int) (((1 - size) * getWidth()) / 2),
                (int) (((1 - size) * getHeight()) / 2),
                (int) (getWidth() * size),
                (int) (getHeight() * size));
    }

    private void drawCenteredCircle(double size, Color color, Graphics bufferedGraphics) {
        bufferedGraphics.setColor(color);
        bufferedGraphics.drawOval(
                (int) (((1 - size) * getWidth()) / 2),
                (int) (((1 - size) * getHeight()) / 2),
                (int) (getWidth() * size),
                (int) (getHeight() * size));
    }

    private int getFontSize(double coeff) {
        return (int) (coeff * Math.min(getHeight(), getWidth()));

    }

    public void setKey(Note pitchNote) {
        this.key = pitchNote;
    }
}
