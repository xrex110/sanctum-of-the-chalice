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
	//private int maxHp;
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
		this.con      = 10;
		this.currentHp = 100;
		//this.maxHp    = 100;
		this.exp      = 0;
		this.alive    = true;
	
	}
	public Stat(int type){
		if(type==0){
			this.objectLv = 1;
			this.str      = 1;
			this.dex      = 1;
			this.wis      = 1;
			this.con      = 10;
			this.currentHp = 100;
			//this.maxHp    = 100;
			this.exp      = 0;
			this.alive    = true;
			this.objectType = 0;//Player
		}else{
			this.objectLv = 1;
			this.str      = 1;
			this.dex      = 1;
			this.wis      = 1;
			this.con      = 2;
			this.currentHp = 20;
			//this.maxHp    = 20;
			this.exp      = 5;
			this.alive    = true;
			this.objectType = 1; // Creature;
		}

	}
	public Stat(int lv, int str, int dex, int wis, int con, int currentHp, int exp, boolean alive){
		
		this.objectLv = lv;
		this.str      = str;
		this.dex      = dex;
		this.wis      = wis;
		this.con      = con;
		this.currentHp = currentHp;
		//this.maxHp    = 100;
		this.exp      = exp;
		this.alive    = alive;
	
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
	public void setHP(int point){
		currentHp += point;
	}
	public void setCon(int point){
		con += point;
				
	}
	public boolean addStat(Stat other) {
		boolean affected = false;
		//this.objectLv += other.objectLv;
		this.str      += other.str;
		this.dex      += other.dex;
		this.wis      += other.wis;
		this.con      += other.con;
		int oldHp = this.currentHp;
		//this.maxHp    += other.maxHp;
		this.currentHp += other.currentHp;
		if (this.currentHp > getMaxHP()) {
			this.currentHp = getMaxHP();
		}
		getExp(other.exp);
		if (other.str != 0 || other.dex != 0 || other.wis != 0 
				|| other.con != 0 || other.exp != 0 
				|| oldHp != this.currentHp) {
			affected = true;
		}

		return affected;
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
		
		return con * 10;
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

	//set all stats to zero
	public void zeroStats() {
		this.objectLv = 0;
		this.str      = 0;
		this.dex      = 0;
		this.wis      = 0;
		this.con      = 0;
		this.currentHp = 0;
		//this.maxHp    = 0;
		this.exp      = 0;
		this.alive    = false;
	

	}

	public Stat copyStats() {
		Stat cop = new Stat();
		cop.objectLv = this.objectLv;
		cop.str      = this.str;
		cop.dex      = this.dex;
		cop.wis      = this.wis;
		cop.con      = this.con;
		cop.currentHp = this.currentHp;
		//cop.maxHp    = this.maxHp;
		cop.exp      = this.exp;
		cop.alive    = this.alive;
		return cop;
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
		currentHp = getMaxHP(); // update their current hp to max
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
