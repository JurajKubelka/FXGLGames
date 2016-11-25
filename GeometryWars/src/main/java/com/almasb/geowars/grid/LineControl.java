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

package com.almasb.geowars.grid;

import com.almasb.ents.AbstractControl;
import com.almasb.ents.Entity;
import com.almasb.fxgl.entity.component.MainViewComponent;
import com.almasb.geowars.component.GraphicsComponent;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Line;

import java.util.List;

/**
 * @author Almas Baimagambetov (AlmasB) (almaslvl@gmail.com)
 */
public class LineControl extends AbstractControl {

    private PointMass end1, end2;
    private Line line;
    private GraphicsContext g;

    public LineControl(PointMass end1, PointMass end2) {
        this.end1 = end1;
        this.end2 = end2;
    }

    @Override
    public void onAdded(Entity entity) {
        entity.getComponent(MainViewComponent.class).ifPresent(viewComponent -> {
            List<Node> list = viewComponent.getView().getChildrenUnmodifiable();
            if (!list.isEmpty()) {
                line = (Line) list.get(0);
            }
        });

        g = entity.getComponentUnsafe(GraphicsComponent.class).getValue();
        //assert line != null;
    }

    @Override
    public void onUpdate(Entity entity, double tpf) {
//        line.setStartX(end1.getPosition().getX());
//        line.setStartY(end1.getPosition().getY());
//        line.setEndX(end2.getPosition().getX());
//        line.setEndY(end2.getPosition().getY());

        g.strokeLine(end1.getPosition().getX(), end1.getPosition().getY(),
                end2.getPosition().getX(), end2.getPosition().getY());
    }
}
