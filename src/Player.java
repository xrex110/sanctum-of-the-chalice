import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Player extends GameObject {
	 
    SpriteLoader sp = new SpriteLoader();
    static Player player = new Player(0,0);
    public static int TILE_SIZE_X = 32, TILE_SIZE_Y = 32;

	/* Player Stat Part.
     * 
     * Level & HP setup;
     * str, dex, wis, con
     * Damage setup.
     *  - Damage = Str*4;
     * 
     */
    private int userLv;
    private int userHP;
    private int str;
    private int dex;
    private int wis;
    private int con;
    private int userDmg;

    public Player(int x, int y) {
        super(x,y, true);
		this.userLv =1;
		this.str=1;
		this.dex=1;
		this.wis=1;
		this.con=1;
		this.userHP = con*30;
    }

    public boolean moveUp() {
        setY(getY() - TILE_SIZE_Y);
        return true;
    }
    public boolean moveDown() {
        System.out.println(getY());
        setY(getY() + TILE_SIZE_Y);
        return true;
    }

    public boolean moveLeft() {
        setX(getX() - TILE_SIZE_X);
        return true;
    }
    public boolean moveRight() {
        setX(getX() + TILE_SIZE_X);
        return true;
    }
    public void draw(Graphics2D rend) {
        draw(rend, getX(), getY());
    }
    public void draw(Graphics2D rend, int copyX, int copyY) {
        //rend.fillRect(getX(),getY(), 32, 32);
		sp.cacheImage("wizard.png");
        BufferedImage sprite = sp.getSprite("wizard.png",0,32,32);
        rend.drawImage(sprite, null, copyX, copyY);
    }
	 /* 
	* 4 update___(int)-> 
     * these are update functions to update user's each stat.
     * The case of the Con part. 
     * 		the parameter "boolean lvUp" will use for
     * 		increment user's health when user get 
     * 		Con stat up -> get some HP which interacted with
     * 		Con status;
     */  
	public void updateStr(int addStr){
		this.str += addStr;	
    }
    public void updateDex(int addDex){
		this.dex += addDex;
    }
    public void updateWis(int addWis){
		this.wis += addWis;
    }
    public void updateCon(int addCon, boolean lvUp){
		this.con += addCon;
		if(lvUp){
			this.userHP += addCon*3;
		}
    }
    
     /* getPlayerStat() -> this method will use for 
     * 			  print to see user's current stat.
     */
    public void getPlayerStat(){
		System.out.println("User Stat: ");
		System.out.println("HP: "+this.userHP);
		System.out.println("Str: "+ this.str);
		System.out.println("Dex: "+ this.dex);
		System.out.println("Wis: "+ this.wis);
		System.out.println("Con: "+ this.con);
	
    }
   

     /* levelUp() -> this method call the 4 stat update 
     * 			  methods to update stat. 
     */
    public void levelUp(){
		updateStr(1);
		updateDex(1);
		updateWis(1);
		updateCon(1,true);

    }

    public void onDamage(int dmg){
		this.userHp-=dmg;
		if(this.userHP<=0){
			System.out.println(" You Die ");
		}
		return this.userHP;
    }
	/*
	 * getUserATK() -> this method will return the user's
	 *				   current Damage. 
	 *				   This will be changed after discuss
	 */
    public void getUserATK(){
		return (this.str * 3);
    }
	/*
	 * getSpeed() -> This function will return the 
	 *				 Speed of Character
	 */
	public void getSpeed(){
		return (this.dex * 3);
	}

}
