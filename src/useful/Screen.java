/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package useful;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import javax.swing.JFrame;

/**
 *
 * @author h-noori
 */
public class Screen {
    private GraphicsDevice vc;

    public Screen()
    {
    GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();        
    vc = env.getDefaultScreenDevice();
    }
    
    public void setFullScreen(DisplayMode dm , JFrame window)
    {
        window.setUndecorated(true);
        window.setResizable(false);
        vc.setFullScreenWindow(window);
    
        if (dm != null && vc.isDisplayChangeSupported())
        {
            try {
            vc.setDisplayMode(dm);
            }
            catch(Exception e){}
            
        }
    }
    
}
