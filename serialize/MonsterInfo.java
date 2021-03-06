/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serialize;

import java.io.Serializable;

/**
 *
 * @author zlsh80826
 */
public class MonsterInfo implements Serializable{
    public int curHp;
    public int action;
    public boolean reverse;
    public float x;
    public float y;
    public boolean rest;
    public boolean die;
    public boolean dying;
    
    public MonsterInfo(float x, float y, boolean reverse, int action, int curHp, boolean die, boolean dying){
        this.x = x;
        this.y = y;
        this.reverse = reverse;
        this.action = action;
        this.curHp = curHp;       
        this.rest = true;
        this.die = die;
        this.dying = dying;
    }
    
    
    public void setInfo(Monster monster){
        this.x = monster.x;
        this.y = monster.y;
        this.reverse = monster.reverse;
        this.curHp = monster.curHp;
        this.action = monster.action;
        this.rest = monster.rest;
        this.dying = monster.dying;
        this.die = monster.die;
    }
}
