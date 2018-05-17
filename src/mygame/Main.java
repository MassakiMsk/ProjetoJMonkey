package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
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

    private boolean up = false, down = false, space = false, pressed = false;
    private final float farmSize = 10;
    private final float farmHeight = 1;
    private final float close = 0.1f;
    private final float closeHeight = 1;
    private final Quaternion quat = new Quaternion();
    private BulletAppState bulletAppState;
    private Node farm, player, tuc, bus, shot;
    private ArrayList<Node> tucs = new ArrayList<>();
    private ArrayList<Node> shots = new ArrayList<>();
    private ArrayList<Double> thetas = new ArrayList<>();
    int aux = 0;
    double auxTheta;
    
    
    public static void main(String[] args) {
        Main app = new Main();
        app.showSettings = false;
        app.start();
    }

    @Override
    public void simpleInitApp() {
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        
        flyCam.setEnabled(paused);
        cam.setLocation(new Vector3f(0, 40, 0));
        quat.fromAngleAxis((float)(Math.PI / 2), new Vector3f(1, 0, 0));
        cam.setRotation(quat);
        
        initKeys();
        createFarm();
        createPlayer();
        createBus();
        createTuc();
        bulletAppState.setDebugEnabled(true);
    }

    @Override
    public void simpleUpdate(float tpf) {
        updateTuc(tpf);
        updatePlayer(tpf);
        updateShot(tpf);
 //       aux++;
        if(aux == 10000) {
            aux = 0;
            createTuc();
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
    }

    @Override
    public void collision(PhysicsCollisionEvent event) {
    }
    
    private void initKeys() {
        inputManager.addMapping("CharForward", new KeyTrigger(KeyInput.KEY_T));
        inputManager.addMapping("CharBackward", new KeyTrigger(KeyInput.KEY_G));
        inputManager.addMapping("CharSpace", new KeyTrigger(KeyInput.KEY_SPACE));

        inputManager.addListener(actionListener, "CharSpace");
        inputManager.addListener(this, "CharForward", "CharBackward");
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
        player = new Node("player");
        
        Box boxMesh;
        Geometry boxGeo;
        Material boxMat;
        
        boxMesh = new Box(0.5f, 0.5f, 0.5f);
        boxGeo = new Geometry("Box", boxMesh);
        boxMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        boxMat.setColor("Color", ColorRGBA.White);
        boxGeo.setName("playerBody");
        boxGeo.setMaterial(boxMat);
        player.attachChild(boxGeo);
        
        player.setLocalTranslation(0, 0.5f, 0);
        RigidBodyControl boxPhysicsNode = new RigidBodyControl(0);
        player.addControl(boxPhysicsNode);
        bulletAppState.getPhysicsSpace().add(boxPhysicsNode);
        rootNode.attachChild(player);
    }
    
    public void createBus() {
        bus = new Node();
        
        Box boxMesh;
        Geometry boxGeo;
        Material boxMat;
        
        boxMesh = new Box(2, 1f, 0.75f);
        boxGeo = new Geometry("Box", boxMesh);
        boxMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        boxMat.setColor("Color", ColorRGBA.Gray);
        boxGeo.setName("bus");
        boxGeo.setMaterial(boxMat);
        bus.attachChild(boxGeo);
        
        bus.setLocalTranslation(0, 1f, 8.5f);
        rootNode.attachChild(bus);
    }
    
    public void createTuc() {
        tuc = new Node("tuc");
        
        Box boxMesh;
        Geometry boxGeo;
        Material boxMat;
        
        boxMesh = new Box(0.5f, 0.5f, 0.5f);
        boxGeo = new Geometry("Box", boxMesh);
        boxMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        boxMat.setColor("Color", ColorRGBA.Red);
        boxGeo.setName("tuc");
        boxGeo.setMaterial(boxMat);
        tuc.attachChild(boxGeo);
        
        tuc.setLocalTranslation(bus.getLocalTranslation());
        tucs.add(tuc);
        rootNode.attachChild(tuc);
    }
    
    public void updateTuc(float tpf) {
        for(Node currentTuc : tucs){
            double theta = Math.atan((currentTuc.getLocalTranslation().x - player.getLocalTranslation().x) / (currentTuc.getLocalTranslation().z - player.getLocalTranslation().z));
            quat.fromAngleAxis((float)theta, new Vector3f(0, 1, 0));
            currentTuc.setLocalRotation(quat);
            if(player.getLocalTranslation().z - currentTuc.getLocalTranslation().z > 0)
                currentTuc.setLocalTranslation((float)(currentTuc.getLocalTranslation().x + (Math.sin(theta) * tpf)), (float)currentTuc.getLocalTranslation().y, (float)(currentTuc.getLocalTranslation().z + (Math.cos(theta) * tpf)));
            else
                currentTuc.setLocalTranslation((float)(currentTuc.getLocalTranslation().x - (Math.sin(theta) * tpf)), (float)currentTuc.getLocalTranslation().y, (float)(currentTuc.getLocalTranslation().z - (Math.cos(theta) * tpf)));
        }
    }
    
    public void updatePlayer(float tpf) {
        double theta = Math.atan((inputManager.getCursorPosition().x - cam.getScreenCoordinates(player.getLocalTranslation()).x) / (inputManager.getCursorPosition().y - cam.getScreenCoordinates(player.getLocalTranslation()).y));
        quat.fromAngleAxis((float)theta, new Vector3f(0, -1, 0));
        player.setLocalRotation(quat);
        player.getControl(RigidBodyControl.class).setPhysicsRotation(player.getLocalRotation());
        player.getControl(RigidBodyControl.class).activate();
        
        if(up) {
            if(cam.getScreenCoordinates(player.getLocalTranslation()).y - inputManager.getCursorPosition().y < 0) {
                player.setLocalTranslation((float)(player.getLocalTranslation().x - (Math.sin(theta) * tpf * 2)), (float)player.getLocalTranslation().y, (float)(player.getLocalTranslation().z + (Math.cos(theta) * tpf * 2)));
            }
            else {
                player.setLocalTranslation((float)(player.getLocalTranslation().x + (Math.sin(theta) * tpf * 2)), (float)player.getLocalTranslation().y, (float)(player.getLocalTranslation().z - (Math.cos(theta) * tpf * 2)));
            }
            player.getControl(RigidBodyControl.class).setPhysicsLocation(player.getLocalTranslation());
        }
        else if(down) {
            if(cam.getScreenCoordinates(player.getLocalTranslation()).y - inputManager.getCursorPosition().y < 0) {
                player.setLocalTranslation((float)(player.getLocalTranslation().x + (Math.sin(theta) * tpf * 2)), (float)player.getLocalTranslation().y, (float)(player.getLocalTranslation().z - (Math.cos(theta) * tpf * 2)));
            }
            else {
                player.setLocalTranslation((float)(player.getLocalTranslation().x - (Math.sin(theta) * tpf * 2)), (float)player.getLocalTranslation().y, (float)(player.getLocalTranslation().z + (Math.cos(theta) * tpf * 2)));
            }
            player.getControl(RigidBodyControl.class).setPhysicsLocation(player.getLocalTranslation());
        }
        /*if(right) {
            player.setLocalTranslation((player.getLocalTranslation().x - tpf * 2), player.getLocalTranslation().y, player.getLocalTranslation().z);
            player.getControl(RigidBodyControl.class).setPhysicsLocation(player.getLocalTranslation());
        }
        else if(left) {
            player.setLocalTranslation((player.getLocalTranslation().x + tpf * 2), player.getLocalTranslation().y, player.getLocalTranslation().z);
            player.getControl(RigidBodyControl.class).setPhysicsLocation(player.getLocalTranslation());
        }*/
        auxTheta = theta;
    }
    
    public void createShot() {
        shot = new Node("shot");
        
        Box boxMesh;
        Geometry boxGeo;
        Material boxMat;
        
        boxMesh = new Box(0.1f, 0.1f, 0.3f);
        boxGeo = new Geometry("Box", boxMesh);
        boxMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        boxMat.setColor("Color", ColorRGBA.Yellow);
        boxGeo.setName("shot");
        boxGeo.setMaterial(boxMat);
        shot.attachChild(boxGeo);
        
        shot.setLocalTranslation(player.getLocalTranslation());
        shot.setLocalRotation(player.getLocalRotation());
        shots.add(shot);
        thetas.add(auxTheta);
        rootNode.attachChild(shot);
    }
    
    public void updateShot(float tpf) {
        int i = 0;
        double theta;
        for(Node currentShot : shots) {
            theta = thetas.get(i);
            i++;
            currentShot.setLocalTranslation((float)(currentShot.getLocalTranslation().x + Math.sin(theta) * tpf / 10), currentShot.getLocalTranslation().y, (float)(currentShot.getLocalTranslation().z + Math.cos(theta) * tpf / 10));
        }
    }
}
