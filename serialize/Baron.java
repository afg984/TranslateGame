/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serialize;

import java.io.Serializable;
import java.util.ArrayList;
import processing.core.PApplet;
import static processing.core.PApplet.nf;
import processing.core.PImage;

/**
 *
 * @author zlsh80826
 */
public class Baron extends Monster implements Serializable {

    // stand 
    // move
    // hit 
    // die
    // attack
    
    public Baron(PApplet parent, float x, float y, StoryMap map){
        this.frame = new ArrayList<Integer>();
        for(int i=0; i<10; ++i)
            frame.add(0);
        this.imageCount = new ArrayList<Integer>();
        this.imageCount.add(24);
        this.imageCount.add(24);
        this.imageCount.add(10);
        this.imageCount.add(10);
        this.imageCount.add(1);
        this.imageCount.add(1);
        this.imageCount.add(60);
        this.imageCount.add(60);
        this.imageCount.add(28);
        this.imageCount.add(28);
        this.parent = parent;
        this.x = x;
        this.y = y;
        this.fileName = new ArrayList<String>();
        this.fileName.add( "Stand" );
        this.fileName.add( "StandR" );
        this.fileName.add( "Move" );
        this.fileName.add( "MoveR" );
        this.fileName.add( "Hit" );
        this.fileName.add( "HitR" );
        this.fileName.add( "Die" );
        this.fileName.add( "DieR" );
        this.fileName.add( "Attack" );
        this.fileName.add( "AttackR" );
        this.reverse = false;
        this.map = map;
        
        String imagePrefix = "material/monster/Baron/Baron";
        String imageSuffix = ".png";
        
        this.images = new ArrayList<ArrayList<PImage>>();
        for(int j=0; j<fileName.size(); ++j){ 
            ArrayList<PImage> temp = new ArrayList<PImage>();
            for(int i=0; i< imageCount.get(j); ++i){
                String file_name = imagePrefix + fileName.get(j) + nf(i, 3) + imageSuffix;
                temp.add(parent.loadImage(file_name));
            }
            images.add(temp);
        }
        this.count = 0;
        this.action = 0;
        this.active = false;
    }
    
    public void display(){
        if(active){
            parent.image(images.get(this.getAction()).get(frame.get(this.getAction())),
                    this.x - images.get(this.getAction()).get(frame.get(this.getAction())).width + map.getX(),
                    this.y - images.get(this.getAction()).get(frame.get(this.getAction())).height + map.getY());
            if( ++count % 12 == 0){
                count = 0;
                int temp = (frame.get(this.getAction())+1) % (imageCount.get(this.getAction()));
                frame.set(this.getAction(), temp );
            }
        }
    }   
    
    public void setStand(){
        action = 0;
    }
    
    public void setMove(){
        action = 2;
    }
    
    public void setHit(){
        action = 4;
    }
    
    public void setDie(){
        action = 6;
    }
    
    public void setAttack(){
        action = 8;
    }
    

        
    
}
