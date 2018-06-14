package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture;
import java.util.ArrayList;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication implements ActionListener, PhysicsCollisionListener{

    private boolean up = false, down = false, left = false, right = false, space = false, pressed = false;
    private final float farmSize = 10;
    private final float farmHeight = 1;
    private final float close = 0.1f;
    private final float closeHeight = 1;
    private final Quaternion quat = new Quaternion();
    private BulletAppState bulletAppState;
    private Node farm;
    private Shot shot;
    private Tuc tuc;
    private Player1 player;
    //private Player player;
    private ArrayList<Tuc> tucs = new ArrayList<>();
    private ArrayList<Shot> shots = new ArrayList<>();
    private ArrayList<Bus> buses = new ArrayList<>();
    private int aux = 0;
    private int pontos = 0;
    private int qntTucs = 0;
    long tempo = System.currentTimeMillis();
    long tempoBus = System.currentTimeMillis();
    
    public static void main(String[] args) {
        Main app = new Main();
        //app.showSettings = false;
        app.start();
    }

    @Override
    public void simpleInitApp() {
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        
        flyCam.setEnabled(paused);
        cam.setLocation(new Vector3f(0, 35, -15));
        quat.fromAngleAxis((float)((Math.PI / 2) * 0.8f), new Vector3f(1, 0, 0));
        cam.setRotation(quat);
        
        initKeys();
        createFarm();
        createPlayer();
        createBus();
        //bulletAppState.setDebugEnabled(true);
        bulletAppState.getPhysicsSpace().addCollisionListener(this);
        
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection((new Vector3f(-0.5f, -0.5f, -0.5f)).normalizeLocal());
        sun.setColor(ColorRGBA.White);
        rootNode.addLight(sun);
        
    }

    @Override
    public void simpleUpdate(float tpf) {
        player.update(tpf, inputManager.getCursorPosition(), cam.getScreenCoordinates(player.getLocalTranslation()), up, down, left, right);
        for(Tuc currentTuc : tucs){
            currentTuc.update(tpf, player);
        }
        
        for(Shot currentShot : shots) {
            currentShot.update(tpf);
        }
        
        if(tempo + 3000 < System.currentTimeMillis()) {
            tempo = System.currentTimeMillis();
            for(Bus bus: buses) {
                createTuc(bus);
            }
        }
        
        if(tempoBus + 10000 < System.currentTimeMillis()) {
            tempoBus = System.currentTimeMillis();
            createBus();
        }
        
        guiNode.detachAllChildren();
        
        if(tucs.size() > 10) {
            rootNode.detachAllChildren();
            
            BitmapText textoFinal = new BitmapText(guiFont, false);                
            textoFinal.setSize(guiFont.getCharSet().getRenderedSize());
            textoFinal.setText("Pontuacao Final: " + (qntTucs - tucs.size()));
            textoFinal.setLocalTranslation(200, textoFinal.getLineHeight() + 250, 0);                     
            guiNode.attachChild(textoFinal);
        }
        else {
            BitmapText helloText = new BitmapText(guiFont, false);
            helloText.setSize(guiFont.getCharSet().getRenderedSize());
            helloText.setText("Sem Terras: " + tucs.size() + "         Pontuacao: " + (qntTucs - tucs.size()));
            helloText.setLocalTranslation(400, helloText.getLineHeight(), 0);
            guiNode.attachChild(helloText);
            BitmapText helloText1 = new BitmapText(guiFont, false);
            helloText1.setSize(guiFont.getCharSet().getRenderedSize());
            helloText1.setText("    Com 10 sem terras você perde sua fazenda!");
            helloText1.setLocalTranslation(0, helloText.getLineHeight(), 0);
            guiNode.attachChild(helloText1);
        }
        
    }

    @Override
            
        
    public void simpleRender(RenderManager rm) {
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        
        switch (name) {
            case "CharForward":
                if (isPressed) {
                    up = true;
                }
                else {
                    up = false;
                }
                break;
            case "CharBackward":
                if (isPressed) {
                    down = true;
                }
                else {
                    down = false;
                }
                break;
        }
        switch(name) {
            case "CharLeft":
                if(isPressed) {
                    left = true;
                }
                else {
                    left = false;
                }
                break;
            case "CharRight":
                if(isPressed) {
                    right = true;
                }
                else {
                    right = false;
                }
                break;
        }
    }

    @Override
    public void collision(PhysicsCollisionEvent event) {
        if (event.getNodeA().getName().equals("tuc") || event.getNodeB().getName().equals("tuc")) {
            if(event.getNodeA().getName().equals("shot") || event.getNodeB().getName().equals("shot")) {
                rootNode.detachChild(event.getNodeA());
                bulletAppState.getPhysicsSpace().remove(event.getNodeA().getControl(RigidBodyControl.class));
                tucs.remove(event.getNodeA());
                
                rootNode.detachChild(event.getNodeB());
                bulletAppState.getPhysicsSpace().remove(event.getNodeB().getControl(RigidBodyControl.class));
                tucs.remove(event.getNodeB());
            }
        }

        else if(event.getNodeA().getName().equals("farm") || event.getNodeB().getName().equals("farm")) {
            if(event.getNodeA().getName().equals("shot")) {
                rootNode.detachChild(event.getNodeA());
                bulletAppState.getPhysicsSpace().remove(event.getNodeA().getControl(RigidBodyControl.class));
                tucs.remove(event.getNodeA());
            }
            else if(event.getNodeB().getName().equals("shot")) {
                rootNode.detachChild(event.getNodeB());
                bulletAppState.getPhysicsSpace().remove(event.getNodeB().getControl(RigidBodyControl.class));
                tucs.remove(event.getNodeB());
            }
        }
        
        else if(event.getNodeA().getName().equals("player") || event.getNodeB().getName().equals("player")) {
            if(event.getNodeA().getName().equals("tuc")) {
                rootNode.detachChild(event.getNodeA());
                bulletAppState.getPhysicsSpace().remove(event.getNodeA().getControl(RigidBodyControl.class));
                tucs.remove(event.getNodeA());
            }
            if(event.getNodeB().getName().equals("tuc")) {
                rootNode.detachChild(event.getNodeB());
                bulletAppState.getPhysicsSpace().remove(event.getNodeB().getControl(RigidBodyControl.class));
                tucs.remove(event.getNodeB());
            }
        }
    }
    
    private void initKeys() {
        inputManager.addMapping("CharForward", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("CharBackward", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("CharLeft", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("CharRight", new KeyTrigger(KeyInput.KEY_D));
        
        
        inputManager.addMapping("CharSpace", new KeyTrigger(KeyInput.KEY_SPACE));

        inputManager.addListener(actionListener, "CharSpace");
        inputManager.addListener(this, "CharForward", "CharBackward");
        inputManager.addListener(this, "CharLeft", "CharRight");
    }
    
    ActionListener actionListener = new ActionListener(){
        @Override
        public void onAction(String name, boolean isPressed, float tpf) {
            if(name.equals("CharSpace") && isPressed){
                createShot();
            }
        }
    };
    
    public void createFarm() {
        farm = new Node("farm");
        Box boxMesh;
        Geometry boxGeo;
        Material boxMat;
        
        boxMesh = new Box(farmSize, farmHeight, farmSize);
        boxGeo = new Geometry("Box", boxMesh);
        boxMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        boxMat.setColor("Color", ColorRGBA.Green);
        boxGeo.setName("chao");
        boxGeo.setMaterial(boxMat);
        farm.attachChild(boxGeo);
        
        boxMesh = new Box((farmSize + (close * 2)), closeHeight, close);
        boxGeo = new Geometry("Box", boxMesh);
        boxMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        boxMat.setColor("Color", ColorRGBA.Brown);
        boxGeo.setName("cerca");
        boxGeo.setMaterial(boxMat);
        boxGeo.setLocalTranslation(0, (closeHeight + farmHeight), farmSize + close);
        farm.attachChild(boxGeo);
        
        boxMesh = new Box((farmSize + (close * 2)), closeHeight, close);
        boxGeo = new Geometry("Box", boxMesh);
        boxMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        boxMat.setColor("Color", ColorRGBA.Brown);
        boxGeo.setName("cerca");
        boxGeo.setMaterial(boxMat);
        boxGeo.setLocalTranslation(0, (closeHeight + farmHeight), -(farmSize + close));
        farm.attachChild(boxGeo);
        
        boxMesh = new Box(close, closeHeight, farmSize);
        boxGeo = new Geometry("Box", boxMesh);
        boxMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        boxMat.setColor("Color", ColorRGBA.Brown);
        boxGeo.setName("cerca");
        boxGeo.setMaterial(boxMat);
        boxGeo.setLocalTranslation((farmSize + close), (closeHeight + farmHeight), 0);
        farm.attachChild(boxGeo);
        
        boxMesh = new Box(close, closeHeight, farmSize);
        boxGeo = new Geometry("Box", boxMesh);
        boxMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        boxMat.setColor("Color", ColorRGBA.Brown);
        boxGeo.setName("cerca");
        boxGeo.setMaterial(boxMat);
        boxGeo.setLocalTranslation(-(farmSize + close), (closeHeight + farmHeight), 0);
        farm.attachChild(boxGeo);
        
        farm.setLocalTranslation(0, -farmHeight, 0);
        
        RigidBodyControl boxPhysicsNode = new RigidBodyControl(0);
        farm.addControl(boxPhysicsNode);
        bulletAppState.getPhysicsSpace().add(boxPhysicsNode);
        rootNode.attachChild(farm);
    }
    
    public void createPlayer() {
        player = new Player1("player", assetManager, bulletAppState, inputManager, cam);
        //player = new Player(bulletAppState, assetManager);
        rootNode.attachChild(player);
    }
    
    public void createBus() {
        Bus bus = null;
        
        if(buses.size() == 0) {
            bus = new Bus(bulletAppState, assetManager, new Vector3f(0, 1f, 8.5f));
        }
        else if(buses.size() == 1) {
            bus = new Bus(bulletAppState, assetManager, new Vector3f(0, 1f, 8.5f));
        }
        else if(buses.size() == 2) {
            bus = new Bus(bulletAppState, assetManager, new Vector3f(7, 1f, 0));
        }
        else if(buses.size() == 3) {
            bus = new Bus(bulletAppState, assetManager, new Vector3f(-7, 1f, 0));
        }
        if(bus != null) {
            buses.add(bus);
            rootNode.attachChild(bus);
        }
    }
    
    public void createTuc(Bus bus) {
        tuc = new Tuc(bulletAppState, assetManager, bus);
        
        tucs.add(tuc);
        rootNode.attachChild(tuc);
        qntTucs++;
    }
    
    public void createShot() {
        shot = new Shot(bulletAppState, assetManager, player);
        
        shots.add(shot);
        rootNode.attachChild(shot);
    }
    
}
