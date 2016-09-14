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

package com.almasb.spaceinvaders.collision;

import com.almasb.ents.Entity;
import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.app.ServiceType;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.spaceinvaders.EntityFactory;
import com.almasb.spaceinvaders.component.InvincibleComponent;
import com.almasb.spaceinvaders.component.OwnerComponent;
import com.almasb.spaceinvaders.event.GameEvent;

/**
 * @author Almas Baimagambetov (AlmasB) (almaslvl@gmail.com)
 */
public class BulletPlayerHandler extends CollisionHandler {

    public BulletPlayerHandler() {
        super(EntityFactory.EntityType.BULLET, EntityFactory.EntityType.PLAYER);
    }

    @Override
    protected void onCollisionBegin(Entity bullet, Entity player) {
        Object owner = bullet.getComponentUnsafe(OwnerComponent.class).getValue();

        // player shot that bullet so no need to handle collision
        if (owner == EntityFactory.EntityType.PLAYER
                || player.getComponentUnsafe(InvincibleComponent.class).getValue()) {
            return;
        }

        bullet.removeFromWorld();

        FXGL.getService(ServiceType.EVENT_BUS).fireEvent(new GameEvent(GameEvent.PLAYER_GOT_HIT));
    }
}
