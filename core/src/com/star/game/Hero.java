package com.star.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

import static java.lang.Math.*;

/**
 * Created by FlameXander on 04.07.2017.
 */

public class Hero extends AbstractShip {
    Joystick joystick;
    int score;
    int money;
    int lifes;

    public Hero() {
        super(Assets.getInstance().mainAtlas.findRegion("ship"), new Vector2(640, 360), new Vector2(0, 0), 3.14f, 100, 32, 200, 400, 0.125f);
        hitArea = new Circle(position.x, position.y, 25);
        lifes = 3;
        isItBot = false;
        joystick = new Joystick();
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - radius, position.y - radius, radius, radius, radius * 2, radius * 2, 1, 1, (float) toDegrees(angle));
    }

    public void update(float dt) {
        super.update(dt);
        if (GameScreen.isAndroid) {
            joystick.update();
            MyGameInputProcessor mgip = (MyGameInputProcessor) Gdx.input.getInputProcessor();
            if (mgip.isTouchedInArea(1280 - 256 - 20, 20, 256, 256) > -1) {
                tryToFire(dt);
            }
            if (joystick.touched) {
                angle = Utils.rotateTo(angle, joystick.getAngle(), rotationSpeed, dt);
                if (currentEnginePower == 0) {
                    currentEnginePower = lowEnginePower;
                }
                currentEnginePower += joystick.power() * dt;
                if (currentEnginePower > maxEnginePower) currentEnginePower = maxEnginePower;
                velocity.add((float) (currentEnginePower * cos(angle) * dt), (float) (currentEnginePower * sin(angle) * dt));
            } else {
                currentEnginePower = 0;
            }
        }
        if (!GameScreen.isAndroid) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
                currentEnginePower = lowEnginePower;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                currentEnginePower += 100 * dt;
                if (currentEnginePower > maxEnginePower) currentEnginePower = maxEnginePower;
            } else {
                currentEnginePower = 0;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                angle += rotationSpeed * dt;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                angle -= rotationSpeed * dt;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.L)) {
                tryToFire(dt);
            }
        }
    }

    public void renderHUD(SpriteBatch batch, BitmapFont font, float x, float y) {
        font.draw(batch, "SCORE: " + score, x, y);
        font.draw(batch, "HP: " + hp + " / " + hpMax + " x" + lifes, x, y - 30);
        font.draw(batch, "MONEY: " + money, x, y - 60);
        joystick.render(batch);
    }

    @Override
    public boolean takeDamage(int dmg) {
        hp -= dmg;
        if (hp <= 0) {
            lifes--;
            hp = hpMax;
            return true;
        }
        return false;
    }


    public void fire() {
        BulletEmitter.getInstance().setupBullet(isItBot, position.x + (float) Math.cos(angle) * 24, position.y + (float) Math.sin(angle) * 24, angle);
        laserSound.play();
    }
}
