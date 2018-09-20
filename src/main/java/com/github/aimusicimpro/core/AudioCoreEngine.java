package com.github.aimusicimpro.core;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.io.jvm.JVMAudioInputStream;

/**
 * The Common Audio Engine
 * @author MowMow
 *
 */
public class AudioCoreEngine {

  
  /** The Audio Dispactcher
   *  register all your Audio Processers to this dispatcher that will send you the sound stream */
  public AudioDispatcher dispatcher;
  
  
  public AudioCoreEngine( JVMAudioInputStream iInputAudioStream ) {
    
    // Create the audio dispatcher from the input audio stream
    dispatcher = new AudioDispatcher( iInputAudioStream,
        AudioInputConstants.DEFAULT_BUFER_SIZE, 
        AudioInputConstants.DEFAULT_BUFER_OVERLAP );
    
  }
  
  
  
  
}
