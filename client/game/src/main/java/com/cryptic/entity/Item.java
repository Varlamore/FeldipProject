package com.cryptic.entity;

import com.cryptic.Client;
import com.cryptic.cache.def.ItemDefinition;
import com.cryptic.entity.model.Model;
import net.runelite.api.Tile;
import net.runelite.rs.api.RSTileItem;

public final class Item extends Renderable implements RSTileItem {

    public Item() {
    }

    public Item(int id, int amount) {
        this.id = id;
        this.quantity = amount;
    }

    public final Model get_rotated_model() {
        ItemDefinition itemDef = ItemDefinition.get(id);
        return itemDef.getModel(quantity);
    }

    public ItemDefinition getDefinition() {
        return ItemDefinition.get(id);
    }

    public int id;
    public int x;
    public int y;
    public int quantity;

    @Override
    public int getSpawnTime() {
        return 0;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getQuantity() {
        return quantity;
    }

    @Override
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public Tile getTile() {
        if (x == -1 || y == -1) {
            return null;
        }

        Tile[][][] tiles = Client.instance.getScene().getTiles();
        return tiles[Client.instance.getPlane()][x][y];
    }
}
