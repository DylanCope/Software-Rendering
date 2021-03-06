package softwarerendering;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import javax.swing.JFrame;

public class Display extends Canvas
{

	private static final long serialVersionUID = -5731461407498870427L;
	private final JFrame         m_frame;
	private final BufferedImage  m_displayImage;
	private final RenderContext  m_frameBuffer;
	private final byte[]         m_displayComponents;
	private final BufferStrategy m_bufferStrategy;
	private final Graphics       m_graphics;
	private final Input          m_input;
	private float				 m_elapsedTime;
	private float				 m_timeAtLastCall;
	
	public static final byte[] WHITE = new byte[] {
		(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF
	};
	public static final byte[] RED = new byte[] {
		(byte) 0xFF, (byte) 0x00, (byte) 0x00, (byte) 0xFF
	};
	
	public Display(int width, int height, String title)
	{
		Dimension size = new Dimension(width, height);
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		m_elapsedTime = 0;
		m_timeAtLastCall = 0;
		
		m_frameBuffer = new RenderContext(width, height);
		m_displayImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		m_displayComponents = 
				((DataBufferByte) m_displayImage.getRaster().getDataBuffer()).getData();
		
		//Create a JFrame designed specifically to show this Display.
		m_frame = new JFrame();
		m_frame.add(this);
		m_frame.pack();
		m_frame.setResizable(false);
		m_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		m_frame.setVisible(true);
		m_frame.setLocationRelativeTo(null);
		m_frame.setTitle(title);

		m_input = new Input();
		addKeyListener(m_input);
		addFocusListener(m_input);
		addMouseListener(m_input);
		addMouseMotionListener(m_input);
		
		createBufferStrategy(2);
		m_bufferStrategy = getBufferStrategy();
		m_graphics = m_bufferStrategy.getDrawGraphics();
		
		setFocusable(true);
		requestFocus();
		
	}
	
	/** Returns the time since the last call in seconds. */
	public float getDelta()
	{
		float t = System.nanoTime();
		float delta = t - m_timeAtLastCall;
		m_timeAtLastCall = t;
		m_elapsedTime += delta;
		return delta / 1000000000f;
	}
	
	public float getElapsedTime()
	{
		return m_elapsedTime / 1000000000f;
	}
	
	public void swapBuffers(){
		m_frameBuffer.copyToByteArray(m_displayComponents);
		m_graphics.drawImage(m_displayImage, 0, 0, 
				m_frameBuffer.getWidth(), m_frameBuffer.getHeight(), null);
		m_bufferStrategy.show();
	}
	
	public RenderContext getFrameBuffer(){
		return m_frameBuffer;
	}
	
	public Input getInput(){
		return m_input;
	}
	
}

