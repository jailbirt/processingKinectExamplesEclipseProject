package virtualDrum;
import java.util.ArrayList;

import javax.swing.LayoutStyle.ComponentPlacement;

import processing.core.PVector;
import ddf.minim.Minim;

class DrumKit
{
    ArrayList instruments ;
    VirtualDrum parent;
    Minim minim;

    DrumKit(Minim eminim, VirtualDrum virtualDrum)
    {
        minim = eminim;
        parent=virtualDrum;
        this.init();
    }

    public void play()
    {
        for(int i=0; i<instruments.size(); i++)
        {
            componentPlayHandler inst = (componentPlayHandler) instruments.get(i);
            inst.checkHit() ;
            inst.checkPlaying() ;
            inst.trigger.draw(parent) ;
            inst.trigger.clear() ;
        }
    }

    public void check (PVector currentPoint)
    {
        for(int i=0; i<instruments.size(); i++)
        {
            componentPlayHandler inst = (componentPlayHandler) instruments.get(i);
            inst.trigger.check( currentPoint );
        }
    }

    public void stop () /// DESTRUCTOR
    {
        for(int i=0; i<instruments.size(); i++)
        {
            componentPlayHandler inst = (componentPlayHandler) instruments.get(i);
            inst.audio.close();
        }

        minim.stop();
    }

    protected void init()
    {
        instruments = new ArrayList();
        instruments.add( new componentPlayHandler(this.minim,"sounds/LEX_Clap5.wav" , new PVector(200,0,1200) , 150, parent) );
        instruments.add( new componentPlayHandler(this.minim,"sounds/LEX_Hat4.wav" , new PVector(-200,0,1200), 150, parent) );
        instruments.add( new componentPlayHandler(this.minim,"sounds/LS-HHK_Kick_0292.wav" , new PVector(-200,200,1200), 150, parent));
        instruments.add( new componentPlayHandler(this.minim,"sounds/LEX_Hit_Crash.wav" , new PVector(200,200,1200), 150, parent));
        instruments.add( new componentPlayHandler(this.minim,"sounds/LEX_Trunk_Shaker7.wav" , new PVector(400,200,1200), 150, parent));
        instruments.add( new componentPlayHandler(this.minim,"sounds/LEX_Hit_Crash.wav" , new PVector(-400,200,1200), 150, parent));
        instruments.add( new componentPlayHandler(this.minim,"sounds/LEX_Clap7.wav" , new PVector(-400,400,1200), 150, parent));
        //instruments.add( new componentPlayHandler(this.minim,"LEX_Secret_Chant.wav" , new PVector(400,400,1200), 150) );
        instruments.add( new componentPlayHandler(this.minim,"sounds/100_RC_Groove_0516b.wav" , new PVector(600,400,1200), 150, parent) );
    }
}
