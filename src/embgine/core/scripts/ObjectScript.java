package embgine.core.scripts;

import embgine.core.elements.GameObject;

abstract public class ObjectScript extends Script<GameObject>{
	
	abstract public void render();
	
	abstract public void staticInit();
}
