/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

/**
 *
 * @author 1545 IRON V4
 */
public class Tuc extends Node{
    public Tuc(BulletAppState bulletAppState, AssetManager assetManager, Node bus) {   
        Box boxMesh;
        Geometry boxGeo;
        Material boxMat;
        
        setName("tuc");
        boxMesh = new Box(0.5f, 0.5f, 0.5f);
        boxGeo = new Geometry("Box", boxMesh);
        boxMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        boxMat.setColor("Color", ColorRGBA.Red);
        boxGeo.setMaterial(boxMat);
        attachChild(boxGeo);
        
        setLocalTranslation(bus.getLocalTranslation());
        
        RigidBodyControl boxPhysicsNode = new RigidBodyControl(0);
        addControl(boxPhysicsNode);
        bulletAppState.getPhysicsSpace().add(boxPhysicsNode);
    }
    
    public void update(float tpf, Node player) {
        double tX, tZ, pX, pZ;
        Quaternion quat = new Quaternion();

        tZ = getLocalTranslation().z;
        tX = getLocalTranslation().x;
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
        setLocalRotation(quat);
        setLocalTranslation((float)(getLocalTranslation().x - (Math.cos(theta) * tpf)), (float)getLocalTranslation().y, (float)(getLocalTranslation().z - (Math.sin(theta) * tpf)));
        getControl(RigidBodyControl.class).setPhysicsLocation(getLocalTranslation());
        getControl(RigidBodyControl.class).setPhysicsRotation(getLocalRotation());
    }
}
