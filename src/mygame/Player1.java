/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.ChaseCamera;
import com.jme3.input.InputManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import java.io.Console;
import static sun.net.www.http.HttpClient.New;

/**
 *
 * @author 1545 IRON V4
 */
public class Player1 extends Node{
    private final BetterCharacterControl physicsCharacter;
    private Vector3f walkDirection = new Vector3f(0, 0, 0);
    private Vector3f viewDirection = new Vector3f(0, 0, 0);
    public double theta = 0;
    
    public Player1(String player, AssetManager assetManager, BulletAppState bulletAppState, InputManager inputManager, Camera cam)
    {
        super(player);
    
        Box boxMesh;
        Geometry boxGeo;
        Material boxMat;
        
        setName("player");
        boxMesh = new Box(0.5f, 0.5f, 0.5f);
        boxGeo = new Geometry("Box", boxMesh);
        boxMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        boxMat.setColor("Color", ColorRGBA.White);
        boxGeo.setMaterial(boxMat);
        attachChild(boxGeo);
        
        setLocalTranslation(0, 0.5f, 0);   
        
        physicsCharacter = new BetterCharacterControl(0.5f, 0.5f, 0.5f);
        addControl(physicsCharacter);
        
        bulletAppState.getPhysicsSpace().add(physicsCharacter);
        

   }

   

    
    public Vector3f getWalkDirection() {
        return walkDirection;
    }

    public void setWalkDirection(Vector3f walkDirection) {
        this.walkDirection = walkDirection;
    }

    public Vector3f getViewDirection() {
        return viewDirection;
    }

    public void setViewDirection(Vector3f viewDirection) {
        this.viewDirection = viewDirection;
    }
    
    void update(float tpf, Vector2f mouse, Vector3f cam, boolean up, boolean down)
    {    
        double cX, cY, pX, pY;
        Vector3f dir;
        
        cX = mouse.x;
        cY = mouse.y;
        pX = cam.x;
        pY = cam.y;
        
        theta = Math.atan(Math.abs((cY - pY) / (cX - pX)));
        
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
        dir = new Vector3f(0, 0, 0);
        if(up)
            dir = new Vector3f((float)(-(Math.cos(theta)) * 4), 0, (float)((Math.sin(theta))) * 4);
        
        physicsCharacter.setWalkDirection(dir);
        physicsCharacter.setViewDirection(dir);
        
    }
}
