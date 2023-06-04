package com.rs.game.dialogue;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.actions.CreateAction;
import com.rs.game.player.content.SkillsDialogue;

public class CreateActionD extends DialogueEventListener {

    private int[] anims;
    private Item[][] materials;
    private Item[][] products;
    private int[] reqs;
    private double[] xp;
    private int skill;
    private int delay;
    
    public CreateActionD(Player player, Item[][] materials, Item[][] products, int delay) {
    	super(player);
        this.materials = materials;
        this.products = products;
        this.delay = delay;
        this.skill = -1;
        this.anims = null;
        this.xp = null;
    }

    public CreateActionD(Player player, Item[][] materials, Item[][] products, double[] xp, int skill, int delay) {
    	super(player);
        this.materials = materials;
        this.products = products;
        this.delay = delay;
        this.skill = skill;
        this.anims = null;
        this.xp = xp;
    }

    public CreateActionD(Player player, Item[][] materials, Item[][] products, double[] xp, int[] anims, int skill, int delay) {
    	super(player);
        this.materials = materials;
        this.products = products;
        this.delay = delay;
        this.skill = skill;
        this.anims = anims;
        this.xp = xp;
    }

    public CreateActionD(Player player, Item[][] materials, Item[][] products, double[] xp, int[] anims, int[] reqs, int skill, int delay) {
    	super(player);
        this.materials = materials;
        this.products = products;
        this.delay = delay;
        this.skill = skill;
        this.anims = anims;
        this.xp = xp;
        this.reqs = reqs;
    }

    public CreateActionD(Player player, Item[][] materials, Item[][] products, double[] xp, int anim, int skill, int delay) {
    	super(player);
        this.materials = materials;
        this.products = products;
        this.delay = delay;
        this.skill = skill;
        this.anims = new int[products.length];
        for (int i = 0; i < products.length; i++)
            this.anims[i] = anim;
        this.xp = xp;
    }

    public CreateActionD(Player player, Item[][] materials, Item[][] products, double[] xp, int anim, int[] reqs, int skill, int delay) {
    	super(player);
        this.materials = materials;
        this.products = products;
        this.delay = delay;
        this.skill = skill;
        this.anims = new int[products.length];
        for (int i = 0; i < products.length; i++)
            this.anims[i] = anim;
        this.xp = xp;
        this.reqs = reqs;
    }

    @Override
    public void start() {
        Item[] products = new Item[this.products.length];
        for (int i = 0; i < this.products.length; i++) {
            products[i] = this.products[i][0];
        }
        SkillsDialogue.sendSkillsDialogue(player, SkillsDialogue.MAKE, 28, products, null);
    }

    @Override
    public void listenToDialogueEvent(int button) {
        player.getAction().setAction(new CreateAction(materials, products, xp, anims, reqs, skill, delay, SkillsDialogue.getItemSlot(button)));
    }
}
