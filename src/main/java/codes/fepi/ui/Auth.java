package codes.fepi.ui;

import spark.Spark;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.Objects;
import java.util.UUID;

public class Auth {

	private static String AUTH_PAGE = "<form action='/auth' method='post'><input type='password' name='pw'></form>";
	private static final String token = UUID.randomUUID().toString();
	private static String pwCache;

	public static void init() {
		Spark.before((req, res) -> {
			String auth = req.cookie("auth");
			if ((auth == null || !auth.equals(token)) && !req.pathInfo().equals("/auth")) {
				Spark.halt(401, AUTH_PAGE);
			}
		});
		Spark.post("/auth", (req, res) -> {
			String pw = req.queryParams("pw");
			if (Objects.equals(pw, getPassword())) {
				res.cookie("auth", token);
				res.redirect("/");
				return "ok";
			}
			res.status(401);
			return "no";
		});
	}

	private static String getPassword() {
		if (pwCache == null) {
			try {
				char[] buf = new char[20];
				int count = new InputStreamReader(new URL("http://127.0.0.1/auth").openConnection().getInputStream()).read(buf);
				pwCache = new String(buf, 0, count);
			} catch (Exception e) {
				System.out.println("Auth service not available, running without security");
				return "";
			}
		}
		return pwCache;
	}
}
