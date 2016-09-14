/*
 * The MIT License (MIT)
 *
 * FXGL - JavaFX Game Library
 *
 * Copyright (c) 2015-2016 AlmasB (almaslvl@gmail.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.almasb.spaceinvaders.control;

import com.almasb.ents.AbstractControl;
import com.almasb.ents.Entity;
import com.almasb.ents.component.Required;
import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.entity.component.BoundingBoxComponent;
import com.almasb.fxgl.entity.component.PositionComponent;
import com.almasb.fxgl.time.MasterTimer;
import com.almasb.spaceinvaders.Config;
import com.almasb.spaceinvaders.EntityFactory;
import com.almasb.spaceinvaders.component.InvincibleComponent;

/**
 * @author Almas Baimagambetov (AlmasB) (almaslvl@gmail.com)
 */
@Required(PositionComponent.class)
@Required(BoundingBoxComponent.class)
@Required(InvincibleComponent.class)
public class PlayerControl extends AbstractControl {

    private PositionComponent position;
    private BoundingBoxComponent bbox;
    private InvincibleComponent invicibility;

    private MasterTimer timer;

    private double dx = 0;
    private double attackSpeed = Config.PLAYER_ATTACK_SPEED;

    private boolean canShoot = true;
    private long lastTimeShot = 0;

    @Override
    public void onAdded(Entity entity) {
        position = entity.getComponentUnsafe(PositionComponent.class);
        bbox = entity.getComponentUnsafe(BoundingBoxComponent.class);
        invicibility = entity.getComponentUnsafe(InvincibleComponent.class);

        timer = FXGL.getMasterTimer();
    }

    @Override
    public void onUpdate(Entity entity, double tpf) {
        dx = Config.PLAYER_MOVE_SPEED * tpf;

        if (!canShoot) {
            if ((timer.getNow() - lastTimeShot) / 1000000000.0 >= 1.0 / attackSpeed) {
                canShoot = true;
            }
        }
    }

    public void left() {
        if (position.getX() - dx >= 0)
            position.translateX(-dx);
    }

    public void right() {
        if (position.getX() + bbox.getWidth() + dx <= Config.WIDTH)
            position.translateX(dx);
    }

    public void shoot() {
        if (!canShoot)
            return;

        canShoot = false;
        lastTimeShot = timer.getNow();

        Entity bullet = EntityFactory.newLaser(getEntity());

        getEntity().getWorld().addEntity(bullet);

        FXGL.getAudioPlayer()
                .playSound("shoot" + (int)(Math.random() * 4 + 1) + ".wav");
    }

    public void enableInvincibility() {
        invicibility.setValue(true);
    }

    public void disableInvincibility() {
        invicibility.setValue(false);
    }

    public boolean isInvincible() {
        return invicibility.getValue();
    }

    public void increaseAttackSpeed(double value) {
        attackSpeed += value;
    }
}
