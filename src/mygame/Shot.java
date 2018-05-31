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
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

/**
 *
 * @author 1545 IRON V4
 */
public class Shot extends Node{
    public double theta;
    
    //public Shot(BulletAppState bulletAppState, AssetManager assetManager, Player1 player) {
    public Shot(BulletAppState bulletAppState, AssetManager assetManager, Player player) {
        Box boxMesh;
        Geometry boxGeo;
        Material boxMat;
        
        setName("shot");
        boxMesh = new Box(0.1f, 0.1f, 0.1f);
        boxGeo = new Geometry("Box", boxMesh);
        boxMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        boxMat.setColor("Color", ColorRGBA.Yellow);
        boxGeo.setMaterial(boxMat);
        attachChild(boxGeo);
        
        setLocalTranslation(player.getLocalTranslation());
        setLocalRotation(player.getLocalRotation());
        
        RigidBodyControl boxPhysicsNode = new RigidBodyControl(1);
        addControl(boxPhysicsNode);
        bulletAppState.getPhysicsSpace().add(boxPhysicsNode);
        
        theta = player.theta;
    }
    
    public void update(float tpf) {
        setLocalTranslation((float)(getLocalTranslation().x - Math.cos(theta) * tpf * 5), getLocalTranslation().y, (float)(getLocalTranslation().z + Math.sin(theta) * tpf * 5));
        getControl(RigidBodyControl.class).setPhysicsLocation(getLocalTranslation());
    }
}
