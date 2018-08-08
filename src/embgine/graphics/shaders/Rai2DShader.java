package embgine.graphics.shaders;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform4f;

import static org.lwjgl.opengl.GL20.glUniform1f;

public class Rai2DShader extends Shader {
	
	private int enableLoc;
	private int texLoc;
	private int colorLoc;
	
	private double timer;//used internally
	
	public Rai2DShader() {
		super("embgine/shaders/rai2d.vs", "embgine/shaders/rai2d.fs", 6);
		
	}
	
	protected void getUniforms() {
		colorLoc = glGetUniformLocation(program, "gradient");
		enableLoc = glGetUniformLocation(program, "enable");
		texLoc = glGetUniformLocation(program, "frame");
	}

	@Override
	protected void subRoutine(float[] p) {
		glUniform4f(texLoc, p[2], p[3], p[4], p[5]);
		glUniform1f(enableLoc, p[0]);
		
		timer = (p[1]) % (Math.PI*2);
		
		glUniform1f(colorLoc, (float)timer);
	}
	
}
