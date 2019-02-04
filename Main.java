package luciano.martino.fractal;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;

public class Main extends JPanel {
	private static final long serialVersionUID = 1L;

	private double xOffset = 0, yOffset = 0;
	private double scale = 1.;

	public void addNotify() {
		super.addNotify();

		setFocusable(true);
		requestFocusInWindow();

		final double SPEED = .025;
		final double SCALE_SPEED = 1;

		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_RIGHT)
					xOffset += SPEED;
				else if (e.getKeyCode() == KeyEvent.VK_LEFT)
					xOffset -= SPEED;
				else if (e.getKeyCode() == KeyEvent.VK_UP)
					yOffset -= SPEED;
				else if (e.getKeyCode() == KeyEvent.VK_DOWN)
					yOffset += SPEED;
				else if(e.getKeyCode() == KeyEvent.VK_W)
					scale += SCALE_SPEED;
				else if(e.getKeyCode() == KeyEvent.VK_S)
					scale -= SCALE_SPEED;
				repaint();

			}

			public void keyReleased(KeyEvent e) {

			}
		});

	}

	public double[] squareComplex(double cX, double cY) {
		double aX, aY;

		aX = cX * cX - cY * cY;
		aY = 2 * cX * cY;

		return new double[] { aX, aY };
	}

	public void paint(Graphics g) {
		int width = getWidth(), height = getHeight();

		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.GREEN);
		g2d.fillRect(0, 0, width, height);

		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				double cX = x / (double) width * 2. - 1., cY = y / (double) height * 2. - 1.;
				cX *= width / (double) height;

				cX /= scale;
				cY /= scale;

				cX += xOffset;
				cY += yOffset;

				double zX = 0, zY = 0;

				int j = 0;

				double length = 0;
				for (int i = 0; i < 360; i++) {
					double[] zSquared = squareComplex(zX, zY);
					zX = zSquared[0] + cX;
					zY = zSquared[1] + cY;
					j++;
					length = Math.sqrt(zX * zX + zY * zY);
					if (length > 2.)
						break;
				}
				int val = (int) ((j / 360.) * 255);
				pixels[x + y * width] = 255 << 24 | val << 16 | val << 8 | val;

			}
		}
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		bi.setRGB(0, 0, width, height, pixels, 0, width);
		g.drawImage(bi, 0, 0, width, height, null);
	}

	public static void main(String[] args) {

		Main panel = new Main();
		panel.setPreferredSize(new Dimension(80, 60));

		JFrame jf = new JFrame("Hi");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.add(panel);
		jf.setResizable(true);
		jf.pack();
		jf.setLocationRelativeTo(null);
		jf.setVisible(true);

	}
}
