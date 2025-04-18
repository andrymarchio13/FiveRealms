package model.characters;

import model.equipment.weapons.Axe;
import model.equipment.weapons.LongSword;
import model.point.Point;

public class Barbarian extends AbstractCharacter{

	public Barbarian() {
		super(rand.nextInt(120,140), rand.nextInt(20,40), rand.nextInt(70,90), rand.nextInt(10,30)); //Random
		this.availableWeapons.add(new Axe());
		this.availableWeapons.add(new LongSword());
		super.spawnWeapon();
	}
}
