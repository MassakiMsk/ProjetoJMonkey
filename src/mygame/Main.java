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

    private boolean up = false, down = false, space = false, pressed = false;
    private final float farmSize = 10;
    private final float farmHeight = 1;
    private final float close = 0.1f;
    private final float closeHeight = 1;
    private final Quaternion quat = new Quaternion();
    private BulletAppState bulletAppState;
    private Node farm, bus;
    private Shot shot;
    private Tuc tuc;
    //private Player1 player;
    private Player player;
    private ArrayList<Tuc> tucs = new ArrayList<>();
    private ArrayList<Shot> shots = new ArrayList<>();
    private int aux = 0;
    
    
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
        bulletAppState.getPhysicsSpace().addCollisionListener(this);
    }

    @Override
    public void simpleUpdate(float tpf) {
        player.update(tpf, inputManager.getCursorPosition(), cam.getScreenCoordinates(player.getLocalTranslation()), up, down);
        for(Tuc currentTuc : tucs){
            currentTuc.update(tpf, player);
        }
        
        for(Shot currentShot : shots) {
            currentShot.update(tpf);
        }
        
        aux++;
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
        if (event.getNodeA().getName().equals("tuc") || event.getNodeB().getName().equals("tuc")) {
<<<<<<< HEAD
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
=======
            if(event.getNodeA().getName().equals("shot")) {

            }
            else if(event.getNodeB().getName().equals("shot")) {
                
>>>>>>> 4fb2922be18c9de1db77e459b3293338aea09654
            }
        }
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
        //player = new Player1("player", assetManager, bulletAppState, inputManager, cam);
        player = new Player(bulletAppState, assetManager);
        rootNode.attachChild(player);
    }
    
    public void createBus() {
        //bus = new Bus(bulletAppState, assetManager, new Vector3f(0, 1f, 8.5f));
        bus = new Bus(bulletAppState, assetManager, new Vector3f(0, 1f, -8.5f));
        
        rootNode.attachChild(bus);
    }
    
    public void createTuc() {
        tuc = new Tuc(bulletAppState, assetManager, bus);
        
<<<<<<< HEAD
=======
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
        
        RigidBodyControl boxPhysicsNode = new RigidBodyControl(0);
        tuc.addControl(boxPhysicsNode);
        bulletAppState.getPhysicsSpace().add(boxPhysicsNode);
        
>>>>>>> 4fb2922be18c9de1db77e459b3293338aea09654
        tucs.add(tuc);
        rootNode.attachChild(tuc);
    }
    
<<<<<<< HEAD
=======
    public void updateTuc(float tpf) {
        
        double tX, tZ, pX, pZ, auxauxTheta;
        

        
        for(Node currentTuc : tucs){
            tZ = currentTuc.getLocalTranslation().z;
            tX = currentTuc.getLocalTranslation().x;
            pX = player.getLocalTranslation().x;
            pZ = player.getLocalTranslation().z;

            double theta = Math.atan(Math.abs((tZ - pZ) / (tX - pX)));
            if(tX < pX) {
                if(tZ > pZ) {
                    theta = (1.5078 * 2) - theta;
                }
                else {
                    theta = (1.5078 * 2) + theta;
                }
            }
            else {
                if(tZ < pZ) {
                    theta = (1.5078 * 4) - theta;
                }
            }
            quat.fromAngleAxis((float)theta, new Vector3f(0, -1, 0));
            currentTuc.setLocalRotation(quat);
            currentTuc.setLocalTranslation((float)(currentTuc.getLocalTranslation().x - (Math.cos(theta) * tpf)), (float)currentTuc.getLocalTranslation().y, (float)(currentTuc.getLocalTranslation().z - (Math.sin(theta) * tpf)));
            currentTuc.getControl(RigidBodyControl.class).setPhysicsLocation(currentTuc.getLocalTranslation());
            currentTuc.getControl(RigidBodyControl.class).setPhysicsRotation(currentTuc.getLocalRotation());
        }
    }
    
    public void updatePlayer(float tpf) {
        double cX, cY, pX, pY, auxauxTheta;
        
        cX = inputManager.getCursorPosition().x;
        cY = inputManager.getCursorPosition().y;
        pX = cam.getScreenCoordinates(player.getLocalTranslation()).x;
        pY = cam.getScreenCoordinates(player.getLocalTranslation()).y;
        double theta = Math.atan(Math.abs((cY - pY) / (cX - pX)));
        
        if(cX < pX) {
            if(cY > pY) {
                theta = (1.5078 * 2) - theta;
            }
            else {
                theta = (1.5078 * 2) + theta;
            }
        }
        else {
            if(cY < pY) {
                theta = (1.5078 * 4) - theta;
            }
        }
        
        auxauxTheta = theta * 180 / Math.PI;
        quat.fromAngleAxis((float)theta, new Vector3f(0, 1, 0));
        player.setLocalRotation(quat);
        player.getControl(RigidBodyControl.class).setPhysicsRotation(player.getLocalRotation());
        player.getControl(RigidBodyControl.class).activate();
        
        if(up) {
            player.setLocalTranslation((float)(player.getLocalTranslation().x - (Math.cos(theta) * tpf * 2)), (float)player.getLocalTranslation().y, (float)(player.getLocalTranslation().z + (Math.sin(theta) * tpf * 2)));
            player.getControl(RigidBodyControl.class).setPhysicsLocation(player.getLocalTranslation());
        }
        else if(down) {
            player.setLocalTranslation((float)(player.getLocalTranslation().x + (Math.cos(theta) * tpf * 2)), (float)player.getLocalTranslation().y, (float)(player.getLocalTranslation().z - (Math.sin(theta) * tpf * 2)));
            player.getControl(RigidBodyControl.class).setPhysicsLocation(player.getLocalTranslation());
        }
        
        auxTheta = theta;
    }
    
>>>>>>> 4fb2922be18c9de1db77e459b3293338aea09654
    public void createShot() {
        shot = new Shot(bulletAppState, assetManager, player);
        
<<<<<<< HEAD
=======
        boxMesh = new Box(0.3f, 0.1f, 0.1f);
        boxGeo = new Geometry("Box", boxMesh);
        boxMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        boxMat.setColor("Color", ColorRGBA.Yellow);
        boxGeo.setName("shot");
        boxGeo.setMaterial(boxMat);
        shot.attachChild(boxGeo);
        
        shot.setLocalTranslation(player.getLocalTranslation());
        shot.setLocalRotation(player.getLocalRotation());
        
        RigidBodyControl boxPhysicsNode = new RigidBodyControl(0);
        shot.addControl(boxPhysicsNode);
        bulletAppState.getPhysicsSpace().add(boxPhysicsNode);
        
>>>>>>> 4fb2922be18c9de1db77e459b3293338aea09654
        shots.add(shot);
        rootNode.attachChild(shot);
    }
    
<<<<<<< HEAD
=======
    public void updateShot(float tpf) {
        int i = 0;
        double theta;
        for(Node currentShot : shots) {
            theta = thetas.get(i);
            i++;
            currentShot.setLocalTranslation((float)(currentShot.getLocalTranslation().x - Math.cos(theta) * tpf * 5), currentShot.getLocalTranslation().y, (float)(currentShot.getLocalTranslation().z + Math.sin(theta) * tpf * 5));
            currentShot.getControl(RigidBodyControl.class).setPhysicsLocation(currentShot.getLocalTranslation());
        }
    }
>>>>>>> 4fb2922be18c9de1db77e459b3293338aea09654
}
