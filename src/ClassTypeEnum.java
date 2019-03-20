public enum ClassTypeEnum {
    //TODO: Intantiate kit arrays for the starter kits!

    ARCHER("Archer", "Attack your enemies at range!", new Item[] {}, new Stat(),  1),
    WARRIOR("Warrior", "", new Item[] {}, new Stat(), 5),
    MAGE("Mage", "", new Item[] {}, new Stat(), 6),
    TANK("Tank", "", new Item[] {}, new Stat(), 9);
    
    public String name;
    public Item[] kit;
    public int spriteTile;
    public Stat stats;
    private ClassTypeEnum(String name, String description, Item[] kit, Stat stats, int spriteTile) {
        this.name = name;
        this.kit = kit;
        this.spriteTile = spriteTile;
        this.stats = stats;
    }

}
