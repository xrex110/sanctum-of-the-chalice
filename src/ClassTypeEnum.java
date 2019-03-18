public enum ClassTypeEnum {
    //TODO: Intantiate kit arrays for the starter kits!

    ARCHER("Archer", "Attack your enemies at range!", new Item[] {}, 1),
    WARRIOR("Warrior", "", new Item[] {}, 5),
    MAGE("Mage", "", new Item[] {}, 6),
    TANK("Tank", "", new Item[] {}, 9);
    
    public String name;
    public Item[] kit;
    public int spriteTile;
    private ClassTypeEnum(String name, String description, Item[] kit, int spriteTile) {
        this.name = name;
        this.kit = kit;
        this.spriteTile = spriteTile;
    }

}
