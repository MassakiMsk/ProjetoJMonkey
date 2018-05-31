/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.InputManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

/**
 *
 * @author 1545 IRON V4
 */
public class Player extends Node{
    
    public double theta;
    
    public Player(BulletAppState bulletAppState, AssetManager assetManager) {
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
        RigidBodyControl boxPhysicsNode = new RigidBodyControl(0);
        addControl(boxPhysicsNode);
        bulletAppState.getPhysicsSpace().add(boxPhysicsNode);
    }
    
    public void update(float tpf, Vector2f mouse, Vector3f cam, boolean up, boolean down) {
        double cX, cY, pX, pY, auxauxTheta;
        Quaternion quat = new Quaternion();
        
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
        
        quat.fromAngleAxis((float)theta, new Vector3f(0, 1, 0));
        setLocalRotation(quat);
        getControl(RigidBodyControl.class).setPhysicsRotation(getLocalRotation());
        getControl(RigidBodyControl.class).activate();
        
        if(up) {
            setLocalTranslation((float)(getLocalTranslation().x - (Math.cos(theta) * tpf * 2)), (float)getLocalTranslation().y, (float)(getLocalTranslation().z + (Math.sin(theta) * tpf * 2)));
            getControl(RigidBodyControl.class).setPhysicsLocation(getLocalTranslation());
        }
        else if(down) {
            setLocalTranslation((float)(getLocalTranslation().x + (Math.cos(theta) * tpf * 2)), (float)getLocalTranslation().y, (float)(getLocalTranslation().z - (Math.sin(theta) * tpf * 2)));
            getControl(RigidBodyControl.class).setPhysicsLocation(getLocalTranslation());
        }
    }
    
    public void clone(Player proximo) {
        proximo.theta = theta;
        proximo.setLocalTranslation(getLocalTranslation());
        proximo.setLocalRotation(getLocalRotation());
    }
}
