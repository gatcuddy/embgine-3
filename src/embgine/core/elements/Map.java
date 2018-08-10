package embgine.core.elements;

import org.joml.Vector4f;

import embgine.core.Block;
import embgine.core.Index;
import embgine.core.loaders.BlockLoader;
import embgine.core.scripts.MapScript;
import embgine.graphics.Camera;
import embgine.graphics.Texture;
import embgine.graphics.Transform;
import embgine.graphics.shaders.Shader;
import embgine.graphics.shapes.Shape;

public class Map extends Element{
	
	private static Index index;
	private static Shader shader;
	private static Camera camera;
	private static Shape mapRect;
	
	private Block[][] map;
	private BlockLoader edgeTile;
	
	private int mapWidth;
	private int mapHeight;
	
	private boolean edgeMode;
	
	public Map(Transform transform, MapScript script, boolean enabled, int type, Block[][] m, boolean edge, BlockLoader tile) {
		super(transform, script, enabled, type);
		
		if(script != null) {
			script.setParent(this);
		}
		
		map = m;
		
		mapWidth = map.length;
		mapHeight = map[0].length;
		
		edgeMode = edge;
		edgeTile = tile;
	}
	
	public static void setup(Index x) {
		index = x;
		
		camera = index.getCamera();
		
		mapRect = Shape.RECT;
		
		shader = Shader.TIL2DSHADER;
	}
	
	public void subRender(int layer) {
		
		float mapX = transform.getX();
		float mapY = transform.getY();
		
		mapRect.getTransform().setSize(Index.TILE * (transform.getWidth() / mapWidth), Index.TILE * (transform.getHeight() / mapHeight));
		
		Transform cameraTransform = camera.getTransform();
		int x = Math.round(cameraTransform.getX());
		int y = Math.round(cameraTransform.getY());
		float gwHalf =  index.getGameWidth() / 2;
		float ghHalf = index.getGameHeight() / 2 ;
		
		int  left = Math.round( (x - gwHalf - mapX) / Index.TILE );
		int right = (int) Math.ceil( (x + gwHalf - mapX) / Index.TILE );
		int    up = Math.round( (y - ghHalf - mapY) / Index.TILE );
		int  down = (int) Math.ceil( (y + ghHalf - mapY) / Index.TILE );
		
		for(int i = left; i <= right; ++i) {
			for(int j = up; j <= down; ++j) {
				Block b = access(i, j);
				if(b != null && b.getLayer() == layer) {
					
					Texture t = b.getTexture();
					Vector4f frame = t.getFrame(b.getValue());
					
					mapRect.getTransform().setPosition(mapX + (i * Index.TILE), mapY + (j * Index.TILE));

					t.bind();
					
					shader.enable(new float[] {frame.x, frame.y, frame.z, frame.w, 1f, 1f, 1f, 1f});
					shader.setMvp(mapRect.getMatrix());
					mapRect.getVAO().render();
					shader.disable();
					
					t.unbind();
				}
			}
		}
	}
	
	public boolean onScreenUpdate(Camera camera) {
		onScreen  = true;
		return true;
	}
	
	public int accessX(float lx) {
		return (int) Math.round((lx - transform.getX()) / (Index.TILE));
	}
	
	public int accessY(float ly) {
		return (int) Math.round((ly - transform.getY()) / (Index.TILE));
	}
	
	public Block access(int x, int y) {
		if(edgeMode) {
			return repeatAccess(x, y);
		}else {
			return edgeAccess(x, y);
		}
	}
	
	public float positionX(int mx) {
		return Index.TILE * (mx + transform.getX());
	}

	public float positionY(int my) {
		return Index.TILE * (my + transform.getY());
	}

	private Block edgeAccess(int x, int y) {
		try {
			return map[x][y];
		}catch(Exception ex) {
			try {
				return (Block)edgeTile.create();
			} catch(Exception ex2) {
				return null;
			}
		}
	}
	
	private Block repeatAccess(int x, int y) {
		if(x < 0) {
			x = 0;
		}else if(x >= mapWidth){
			x = mapWidth-1;
		}
		if(y < 0) {
			y = 0;
		}else if(y >= mapHeight){
			y = mapHeight-1;
		}
		return map[x][y];
	}
	
	public int getWidth() {
		return mapWidth;
	}
	
	public int getHeight() {
		return mapHeight;
	}
	
	public void setBlock(Block b, int x, int y) {
		map[x][y] = b;
	}

	public void subUpdate() {
		script.update();
	}
	
}