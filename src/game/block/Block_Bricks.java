package game.block;

import embgine.core.loaders.BlockLoader;
import embgine.graphics.Texture;

public class Block_Bricks extends BlockLoader{

	public Block_Bricks() {
		super(true, new Texture("game/textures/block_bricks.png"));
	}

}
