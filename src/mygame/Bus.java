/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

/**
 *
 * @author 1545 IRON V4
 */
public class Bus extends Node{
    public int lifes = 50;
    
    public Bus(BulletAppState bulletAppState, AssetManager assetManager, Vector3f pos) {
        Box boxMesh;
        Geometry boxGeo;
        Material boxMat;
        
        setName("bus");
        boxMesh = new Box(2, 1f, 0.75f);
        boxGeo = new Geometry("Box", boxMesh);
        boxMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        boxMat.setColor("Color", ColorRGBA.Gray);
        boxGeo.setName("bus");
        boxGeo.setMaterial(boxMat);
        attachChild(boxGeo);
        
        setLocalTranslation(pos);
    }
}
