package com.cryptic.entity;

import com.cryptic.collection.node.DualNode;
import com.cryptic.entity.model.Model;
import com.cryptic.entity.model.VertexNormal;
import net.runelite.rs.api.RSModel;
import net.runelite.rs.api.RSNode;
import net.runelite.rs.api.RSRenderable;

public class Renderable extends DualNode implements RSRenderable {

    public int model_height;
    public VertexNormal[] normals;

    public void setSceneId(int sceneId) {
        this.sceneId = sceneId;
    }
    public int sceneId = (int) (System.currentTimeMillis() / 1000L);
    public void render_3D(int orientation, int pitchSinY, int pitchCosY, int yawSinX, int yawCosX, int offsetX, int offsetY, int offsetZ, long objectKey) {
        Model model = get_rotated_model();
        if(model != null) {
            this.model_height = model.model_height;
            model.render_3D(orientation, pitchSinY, pitchCosY, yawSinX, yawCosX, offsetX, offsetY, offsetZ, objectKey);
            sceneId++;
        }
    }

    protected Model get_rotated_model() {
        return null;
    }


    @Override
    public void draw(int orientation, int pitchSin, int pitchCos, int yawSin, int yawCos, int x, int y, int z, long hash) {
        render_3D(orientation,pitchSin,pitchCos,yawSin,yawCos,x,y,z,hash);
    }

    @Override
    public boolean isHidden() {
        return false;
    }

    public Renderable() {
        model_height = 1000;
    }

    //TODO clear underlays etc

    @Override
    public int getModelHeight() {
        return model_height;
    }

    @Override
    public void setModelHeight(int modelHeight) {
        model_height = modelHeight;
    }

    @Override
    public RSModel getModel() {
        return get_rotated_model();
    }


}
