import java.io.*;

public class FileIO {

	public static void main(String[] args) throws IOException {

		File f = new File("/home/bridgeit/Desktop/Contract.pdf");

		OutputStream oos = new FileOutputStream("/home/bridgeit/Desktop/Contract-new.pdf");

		byte[] buf = new byte[8192];

		InputStream is = new FileInputStream(f);

		int c = 0;

		while ((c = is.read(buf, 0, buf.length)) > 0) {
			oos.write(buf, 0, c);
			oos.flush();
		}

		oos.close();
		System.out.println("stop");
		is.close();

	}

}