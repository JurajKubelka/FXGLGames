package com.almasb.breakout;

import com.almasb.breakout.control.BallControl;
import com.almasb.breakout.control.BatControl;
import com.almasb.breakout.control.BrickControl;
import com.almasb.ents.Entity;
import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.effect.ParticleControl;
import com.almasb.fxgl.effect.ParticleEmitter;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.EntityView;
import com.almasb.fxgl.entity.RenderLayer;
import com.almasb.fxgl.entity.component.PositionComponent;
import com.almasb.fxgl.gameplay.Level;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.parser.TextLevelParser;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.settings.GameSettings;
import com.almasb.gameutils.math.GameMath;
import javafx.animation.PathTransition;
import javafx.geometry.Point2D;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class BreakoutApp extends GameApplication {

    private BatControl getBatControl() {
        return getGameWorld().getEntitiesByType(EntityType.BAT).get(0).getControlUnsafe(BatControl.class);
    }

    private BallControl getBallControl() {
        return getGameWorld().getEntitiesByType(EntityType.BALL).get(0).getControlUnsafe(BallControl.class);
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("Breakout Underwater");
        settings.setVersion("0.1");
        settings.setWidth(600);
        settings.setHeight(800);
        settings.setIntroEnabled(false);
        settings.setMenuEnabled(false);
        settings.setProfilingEnabled(false);
        settings.setCloseConfirmation(false);
        settings.setApplicationMode(ApplicationMode.DEVELOPER);
    }

    @Override
    protected void initInput() {
        getInput().addAction(new UserAction("Move Left") {
            @Override
            protected void onAction() {
                getBatControl().left();
            }
        }, KeyCode.A);

        getInput().addAction(new UserAction("Move Right") {
            @Override
            protected void onAction() {
                getBatControl().right();
            }
        }, KeyCode.D);
    }

    @Override
    protected void initAssets() {}

    @Override
    protected void initGame() {
        Music music = getAssetLoader().loadMusic("BGM01.wav");
        music.setCycleCount(Integer.MAX_VALUE);

        getAudioPlayer().playMusic(music);

        TextLevelParser parser = new TextLevelParser(new BreakoutFactory());
        Level level = parser.parse("levels/level1.txt");
        getGameWorld().setLevel(level);


        getGameWorld().addEntity(Entities.makeScreenBounds(40));

        Rectangle bg0 = new Rectangle(getWidth(), getHeight());
        LinearGradient gradient = new LinearGradient(getWidth() / 2, 0, getWidth() / 2, getHeight(),
                false, CycleMethod.NO_CYCLE, new Stop(0.2, Color.AQUA), new Stop(0.8, Color.BLACK));

        bg0.setFill(gradient);

        Rectangle bg1 = new Rectangle(getWidth(), getHeight(), Color.color(0, 0, 0, 0.2));
        bg1.setBlendMode(BlendMode.DARKEN);

        Pane bg = new Pane();
        bg.getChildren().addAll(bg0, bg1);

        Entities.builder()
                .viewFromNode(new EntityView(bg, RenderLayer.BACKGROUND))
                .buildAndAttach(getGameWorld());

        ParticleEmitter emitter = new ParticleEmitter();
        emitter.setSourceImage(getAssetLoader().loadTexture("bubble.png").getImage());
        emitter.setBlendFunction((i, x, y) -> BlendMode.SRC_OVER);
        emitter.setEmissionRate(0.25);
        emitter.setExpireFunction((i, x, y) -> Duration.seconds(3));
        emitter.setVelocityFunction((i, x, y) -> new Point2D(0, -GameMath.random(2f, 4f)));
        emitter.setSpawnPointFunction((i, x, y) -> new Point2D(GameMath.random(0, (float)getWidth()), getHeight() + GameMath.random(50)));
        emitter.setScaleFunction((i, x, y) -> new Point2D(GameMath.random(-0.05f, 0), GameMath.random(-0.05f, 0)));

        Entity bubbles = new Entity();
        bubbles.addComponent(new PositionComponent(0, 0));
        bubbles.addControl(new ParticleControl(emitter));

        getGameWorld().addEntity(bubbles);

        // Level info
    }

    @Override
    protected void initPhysics() {
        getPhysicsWorld().setGravity(0, 0);

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.BALL, EntityType.BRICK) {
            @Override
            protected void onCollisionBegin(Entity ball, Entity brick) {
                brick.getControlUnsafe(BrickControl.class).onHit();
            }
        });
    }

    @Override
    protected void initUI() {
        Text text = getUIFactory().newText("Level 1", Color.WHITE, 48);

        QuadCurve curve = new QuadCurve(-100, 0, getWidth() / 2, getHeight(), getWidth() + 100, 0);

        PathTransition transition = new PathTransition(Duration.seconds(4), curve, text);
        transition.setOnFinished(e -> {
            getGameScene().removeUINode(text);
            getBallControl().release();
        });

        getGameScene().addUINode(text);

        transition.play();
    }

    @Override
    protected void onUpdate(double tpf) {}

    public static void main(String[] args) {
        launch(args);
    }
}
