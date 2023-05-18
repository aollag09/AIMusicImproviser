package com.github.aimusicimpro.ui.processors;

import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.example.SpectrogramPanel;
import be.tarsos.dsp.pitch.PitchDetector;
import be.tarsos.dsp.pitch.Yin;
import be.tarsos.dsp.util.fft.FFT;
import com.github.aimusicimpro.core.AudioInputConstants;
import com.github.aimusicimpro.ui.components.MusicLiveFFTBarChart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The FFT Processor computing the spectrum analysis
 *
 * @author MowMow
 */
public class MusicLiveFFTProcessor implements AudioProcessor {

    /**
     * FFT algorithm API
     */
    private final FFT fft;

    /**
     * UI Panel
     */
    private final SpectrogramPanel spectrogram;

    private final MusicLiveFFTBarChart barChart;

    PitchDetector detector;

    public MusicLiveFFTProcessor(SpectrogramPanel iPanel, MusicLiveFFTBarChart barChart) {
        this.spectrogram = iPanel;
        this.barChart = barChart;
        this.fft = new FFT(AudioInputConstants.DEFAULT_BUFFER_SIZE);
        this.detector = new Yin(AudioInputConstants.SAMPLE_RATE, AudioInputConstants.DEFAULT_BUFFER_SIZE);
    }


    @Override
    public boolean process(AudioEvent audioEvent) {

        float[] audioFloatBuffer = audioEvent.getFloatBuffer();
        float[] amplitudes = new float[AudioInputConstants.DEFAULT_BUFFER_SIZE / 2];
        float[] transformBuffer = new float[AudioInputConstants.DEFAULT_BUFFER_SIZE * 2];
        System.arraycopy(audioFloatBuffer, 0, transformBuffer, 0, audioFloatBuffer.length);

        // Compute the DFT
        fft.forwardTransform(transformBuffer);
        fft.modulus(transformBuffer, amplitudes);

        if (barChart != null) {
            barChart.setData(amplitudes);
            barChart.repaint();
        }

        // Compute the pitch
        var pitch = detector.getPitch(audioEvent.getFloatBuffer());

        if (spectrogram != null) {
            // Update the Spectrum panel
            spectrogram.drawFFT(pitch.getPitch(), amplitudes, fft);
            spectrogram.repaint();
            return true;
        } else
            return false;

    }

    @Override
    public void processingFinished() {
    }

}
