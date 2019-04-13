package game;
import main.*;

import render.*;
import sound.*;
import object.*;

import java.util.Random;


public class Stat {
	
	/*
	 * The Constructor function
	 * int class: 0 = default.( nothing special)
	 *            1 = mage
	 *            2 = rougue 
	 *		      3 = Warrior
	 * int type: 0 = Player
	 *			 1 = Enemy
	 * @ 1st trial => make simple to check it is work.
	 */
	private int str;
	private int dex;
	private int con;
	private int wis;
	private int currentHp;
	private int maxHp;
	private int objectLv;
	private int exp;

	private boolean alive;
	private int objectType;
	Random rand = new Random();

	public Stat(){
		
		this.objectLv = 1;
		this.str      = 1;
		this.dex      = 1;
		this.wis      = 1;
		this.con      = 1;
		this.currentHp = 100;
		this.maxHp    = 100;
		this.exp      = 0;
		this.alive    = true;
	
	}
	public Stat(int type){
		if(type==0){
			this.objectLv = 1;
			this.str      = 1;
			this.dex      = 1;
			this.wis      = 1;
			this.con      = 1;
			this.currentHp = 100;
			this.maxHp    = 100;
			this.exp      = 0;
			this.alive    = true;
			this.objectType = 0;//Player
		}else{
			this.objectLv = 1;
			this.str      = 1;
			this.dex      = 1;
			this.wis      = 1;
			this.con      = 1;
			this.currentHp = 20;
			this.maxHp    = 20;
			this.exp      = 5;
			this.alive    = true;
			this.objectType = 1; // Creature;
		}

	}
	/*
	* here are some "set functions"
	* each functions will set the status and 
	* update it.
	* the special case of the setCon() function
	*			-> when the point is positive number
	*				then increase the maximum hp.(not current HP)
	*/
	public void setStr(int point){
		str += point;
	}
	public void setDex(int point){
		dex += point;
	}
	public void setWis(int point){
		wis += point;
	}
	public void setCon(int point){
		con += point;
		if(point >=0){
			maxHp += point*10;
		}		
	}
	/*
	* here are some "get functions"
	* for stat check system, these functions will
	* return the stat;
	*/
	public int getStr(){
		return str;
	}
	public int getDex(){
		return dex;
	}
	public int getWis(){
		return wis;
	}
	public int getCon(){
		return con;
	}
	public int getType(){
		return objectType;
	}
	public int getHP(){
		return currentHp;	
	}	
	public int getMaxHP(){
		return maxHp;
	}
	public int getLv(){
		return objectLv;
	}
	public int getCurXP(){
	
		return exp;

	}
	public int getTotXP(){
	
		return objectLv*10;

	}

	/*
	*	About the Damage related functions
	*	-toDamage function is used to calculate
	*	 how many damages object can give to the opponent.
	*	 -> this damage will be changed later 
	*	-getDamage function is used to deducted the 
	*	 current HP subsitude by the damage come from.
	*   -checkAlive function will check the current statement
	*	 of the object which is alive or dead.
	*/
	public int toDamage(){
		return (str * 10);
	}
	public void getDamage(int dmg){
		currentHp -= dmg;
		if(currentHp <= 0){
			alive = false;
			currentHp=0;
			
		}
	}
	
	public boolean checkAlive(){
		return alive;
	}
	
	/*
	*	This part is related with the level up part.
	*	-getExp function will add exp into the current exp;
	*	 when get exp function called, then this function will	
	*	 check the maxExp for current level everytime.
	*	 And the exp is reached the max, then it call the levelUp
	*	 function;
	*	-levelUp function will call the all "set functions" to	
	*	 update all status.
	*/
	public void getExp(int newExp){
		//int currentExp = exp;
		exp += newExp;
		if(exp/10 == objectLv){
			levelUp();
			exp =0;
		}
	}
	public void levelUp(){
		setStr(1);
		setDex(1);
		setWis(1);
		setCon(1);
		objectLv+=1;
		currentHp = maxHp; // update their current hp to max
	}

	/*
	 * giveExp function is just for the Enemy types.
	 * Enemy already have the value of the exp, and return their 
	 * exp value to the player.
	 */
	 public int giveExp(){
		return exp;
	 }


}