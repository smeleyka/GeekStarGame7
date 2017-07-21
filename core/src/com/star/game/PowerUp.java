package com.star.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by FlameXander on 11.07.2017.
 */

public class PowerUp {
    enum Type {
        MONEY_10, MONEY_25, MONEY_50, REPAIR_KIT
    }

    static TextureAtlas.AtlasRegion texture;
    static TextureRegion[][] regions;

    int type;
    Vector2 position;
    float time;

    public PowerUp(float x, float y) {
        if (texture == null) {
            texture = Assets.getInstance().mainAtlas.findRegion("powerUps");
            regions = texture.split(32, 32);
        }
        position = new Vector2(x, y);
        type = (int) (Math.random() * Type.values().length);
    }

    public void render(SpriteBatch batch) {
        batch.draw(regions[0][type], position.x - 16, position.y - 16);
    }

    public void update(float dt) {
        time += dt;
    }
}
