package game;
import main.*;

import object.*;
import sound.*;

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

	public void combat(Object atk, Object def){

		//case of the different type object -> player vs enemy
		// 3/29 -> update the Player.java & EnemyObject.java 
		//	 add Stat stat = new Stat() with their types.
		if(atk.stat.type != def.stat.type){
				int atkDamage = atk.stat.toDamage;
				def.stat.getDamage(atkDamage);
				
				if(def.stat.checkAlive()==false){
					//check the object die or not.
					if(def.stat.getType()==0){
						//exit or show game over
					}else{
						//enemy die.
						//give exp to the user.
						atk.stat.getExp(def.stat.giveExp());
						//destroy the enemy object.-> ??
					}
				}				

		}else{
		
		}


	}
}
