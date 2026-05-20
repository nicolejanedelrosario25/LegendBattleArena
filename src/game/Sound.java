/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game;

import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 *
 * @author DELL
 */
public class Sound {
    
    private Clip[] clips = new Clip[6];
    private int currentIndex = -1;
//    URL soundURL[] = new URL[6];
    private Clip[] musicClips = new Clip[4];
    private Clip[] seClips    = new Clip[6];
    private int currentMusicIndex = -1;    
    
    private String[] musicPaths = {
            "/resources/sound/intro_bgm.wav",
            "/resources/sound/cs_bgm.wav",
            "/resources/sound/game_bgm.wav", 
            "/resources/sound/battle_bgm.wav",
    };
    
    private String[] sePaths = {
            "/resources/sound/hover.wav",
            "/resources/sound/select_.wav",
            "/resources/sound/select_i.wav",  
            "/resources/sound/attack_se.wav"
    };
    public Sound(){      
        
        for(int i = 0; i < musicPaths.length; i++){
            musicClips[i] = loadClip(musicPaths[i]);
        }
        
        for(int i = 0; i < sePaths.length; i++){
            seClips[i] = loadClip(sePaths[i]); 
        }
    }
    
    private Clip loadClip(String path){
        try{
            URL url = getClass().getResource(path);
            if(url == null){
                System.out.println("Not Found: " + path);
                return null;
            }
            AudioInputStream ais = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            return clip;
            
        }catch(Exception e){
            System.out.println("Failed to load: " + path);
            return null;
        }
    }
    
    public void setFile(int i){
        currentMusicIndex = i;
    }
    public void play(){
        if(currentMusicIndex < 0 || currentMusicIndex >= musicClips.length){
            return;
        }
        Clip clip = musicClips[currentMusicIndex];
        if(clip == null){
            return;
        }
        clip.stop();
        clip.setFramePosition(0);
        clip.start();
        
    }
    
    public void loop(){
        if (currentMusicIndex < 0 || currentMusicIndex >= musicClips.length){
            return;
        }
        Clip clip = musicClips[currentMusicIndex];
        if (clip != null){
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }

    }
    
    public void stop(){
         if (currentMusicIndex < 0 || currentMusicIndex >= musicClips.length){
             return;
         }
        Clip clip = musicClips[currentMusicIndex];
        // FIXED: Only stop playback, never close — closing is what caused the lag
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
    
    public void playSE(int i){
        if(i < 0 || i >= seClips.length){
            return;
        }
        Clip clip = seClips[i];
        if(clip == null){
            return;
        }
        if(clip.isRunning()){
            clip.stop();
        }
        clip.setFramePosition(0);
        clip.start();
    }
}
