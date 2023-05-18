package com.github.aimusicimpro.core;

public class AudioInputConstants {


    /**
     * Sample Rate of the input sound in Hz
     */
    public static final int SAMPLE_RATE = 44100;

    /**
     * Default value for the size of the buffer.
     * The size of the buffer defines how much samples are processed in one step
     */
    public static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    /**
     * How much consecutive buffers overlap (in samples).
     * Half of the AudioBufferSize is common (512, 1024) for an FFT.
     */
    public static final int DEFAULT_BUFFER_OVERLAP = DEFAULT_BUFFER_SIZE / 2;

}
