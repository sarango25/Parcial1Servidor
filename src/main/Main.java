package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.google.gson.Gson;

import processing.core.PApplet;

public class Main extends PApplet {

	private ArrayList<Circle> circles;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PApplet.main("main.Main");
	}

	public void settings() {
		size(500, 500);

	}

	public void setup() {
		startClient();

		circles = new ArrayList<>();
	}

	public void draw() {
		background(255);

		for (int i = 0; i < circles.size(); i++) {
			if (circles.get(i).getType() == 1) {
				fill(255, 0, 0);

			}
			if (circles.get(i).getType() == 2) {
				fill(0, 255, 0);
			}
			if (circles.get(i).getType() == 3) {
				fill(0, 0, 255);
			}

			ellipse(circles.get(i).getX(), circles.get(i).getY(), 20, 20);

			circles.get(i).move();

			if (mouseX < circles.get(i).getX() + 10 && mouseX > circles.get(i).getX() - 10
					&& mouseY < circles.get(i).getY() + 10 && mouseY > circles.get(i).getY() - 10) {

				circles.get(i).setMove(false);

				fill(0);
				text(circles.get(i).getGroupName(), circles.get(i).getX(), circles.get(i).getY());
			}else {
				circles.get(i).setMove(true);
			}

		}

	}

	public void startClient() {

		new Thread(() -> {
			try {
				ServerSocket server = new ServerSocket(5000);
				System.out.println("Esperando cliente...");
				Socket socket = server.accept();
				System.out.println("Cliente esta conectado");

				InputStream is = socket.getInputStream();
				OutputStream os = socket.getOutputStream();

				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader breader = new BufferedReader(isr);

				while (true) {

					System.out.println("Esperando mensaje...");
					String mensajeRecibido = breader.readLine(); // BW::X::Y::ALTO::ANCHO

					System.out.println(mensajeRecibido);

					if (mensajeRecibido.equals("del")) {
						circles.clear();
					} else {
						Gson gson = new Gson();

						Circle obj = gson.fromJson(mensajeRecibido, Circle.class);

						circles.add(obj);
					}

				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}).start();
	}

}
