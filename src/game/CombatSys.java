package game;
import main.*;

import object.*;
import sound.*;
import java.util.*;
public class CombatSys {
	/*
	*	Combat Rule
	*		- movement collision
	*			-> When the object trying to move and there willing position
	*			   already taken by the other object 
	*			  1) check object's type ( player or enemy) 
	*			  2) if they are different type, then stat check & update the status.
	*			
	*				 - call toDamage function to the other object.
	*				 - update each object's current hp based on the damage.
	*/
    
    public static void genericCombat(GameObject a, GameObject b) {
        if(a instanceof Player && b instanceof EnemyObject) combatPlayer((Player)a,(EnemyObject)b);
        else if(a instanceof EnemyObject && b instanceof Player) combatEnemy((EnemyObject)a,(Player) b); 
    }

	public static void combatPlayer(Player atk, EnemyObject def){

		//case of the different type object -> player attack enemy
		if(atk.stat.getType() != def.stat.getType() ){

				int atkDamage = 0;
				atkDamage = atk.stat.toDamage();
				def.stat.getDamage(atkDamage);
				System.out.println("Attack!");
				System.out.println("Enemy get "+ atkDamage + " Damage");
				if(def.stat.checkAlive()==false){
					//check the object die or not.		
					
					if(def.stat.getType()==1){
					System.out.println("Enemy Die");
						//def.remove(0);
						//enemy die.
						//give exp to the user.
						int exp = def.stat.giveExp();
						
						atk.stat.getExp(exp);
						//System.out.println("Player get "+exp+ " exp");
						//destroy the enemy object.-> ??
					}
				}			
				//System.out.println("\n\nEvent number : " + atk.stat.getEventNum()+" \n");
                /*
				System.out.println("Enemy Health: " + def.stat.getHP());
				System.out.println("\n\n Player Stat: ");
				System.out.println("\nPlayer HP: "+atk.stat.getHP());
				System.out.println("Level: "+atk.stat.getLv());
				System.out.println("STR: "+atk.stat.getStr());
				System.out.println("DEX: "+atk.stat.getDex());
				System.out.println("Wis: "+atk.stat.getWis());
				System.out.println("Con: "+atk.stat.getCon()+"\n");	*/			
		}
	}

	public static void combatEnemy(EnemyObject atk, Player def){

		if(atk.stat.getType() != def.stat.getType()){
			int atkDamage = 0;
			atkDamage = atk.stat.toDamage();
			def.stat.getDamage(atkDamage);
			System.out.println("Enemy Attack!");
			System.out.println("Player get: "+ atkDamage+ " Damage");
			if(def.stat.checkAlive() == false){
				//check player is die or not.
				System.out.println("Player die!\n ");
                if(SaveHandler.manualSave) {
                    SaveHandler.loadGame("manual.save");
                } else {
                    SaveHandler.loadGame("auto.save");
                }
				//game over!
				
				//TODO: game over page or restart?
			}
		}
	}
}
