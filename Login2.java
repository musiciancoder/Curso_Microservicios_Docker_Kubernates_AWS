import java.util.Scanner;

public class Login {
    public static void main(String[] args) {

        String[] usernames = {"andres", "admin", "pepe"};
        String[] passwords = {"123", "1234", "12345"};

        Scanner scanner = new Scanner(System.in);

        System.out.println("Ingrese el username");
        String u = scanner.next();

        System.out.println("Ingrese el password");
        String p = scanner.next();

        boolean esAutenticado = false;

        for(int i = 0; i < usernames.length; i++){
            esAutenticado = (usernames[i].equals(u) && passwords[i].equals(p))? true: esAutenticado;
        }

        String mensaje = esAutenticado ? "Bienvenido usuario ".concat(u).concat("!") :
                "Username o contraseña incorrecto!\nLo sentimos, requiere autenticación";
        System.out.println("mensaje = " + mensaje);

    }
}
