/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package single;

import serialize.*;
import java.util.TimerTask;

/**
 *
 * @author zlsh80826
 */
public class MonsterTimer extends TimerTask {

    Action action;
    Monster monster;

    public MonsterTimer(Monster monster, Action action) {
        this.action = action;
        this.monster = monster;
    }

    @Override
    public void run() {
        if (action == Action.MOVE) {
            monster.action = 0;
        } else if (action == Action.STAND) {
            monster.rest = true;
        } else if (action == Action.DIE) {
            monster.curHp = monster.maxHp;
            monster.die = false;
        } else if (action == Action.DYING) {
            monster.dying = false;
            monster.action = 0;
        } else if(action == Action.HIT){
            monster.action = 0;
            monster.rest = true;
        }
    }

}
