package pe.edu.upeu.entregable1.utils;



import java.util.Scanner;



public class Lectura {
    private static Scanner teclado = new Scanner(System.in);
    public String cadena() {

        return teclado.nextLine();

    }


    public int entero(){

        while (true) {
            String valor = cadena().trim();

            if (!valor.matches("[0-9]+")) {
                System.out.print("Solo se permiten numeros, sin letras ni simbolos. Intente nuevamente: ");
                continue;
            }

            try {
                return Integer.parseInt(valor);
            } catch (NumberFormatException e) {
                System.out.print("El numero es demasiado grande. Intente nuevamente: ");
            }
        }

    }

    public int id() {
        while (true) {
            int valor = entero();
            if (valor > 0) return valor;
            System.out.print("El ID debe ser mayor que cero. Intente nuevamente: ");
        }
    }

    public int celular() {
        while (true) {
            String valor = cadena().trim();
            if (valor.matches("[1-9][0-9]{8}")) {
                return Integer.parseInt(valor);
            }
            System.out.print("Ingrese un celular de 9 digitos, sin letras ni simbolos y sin cero inicial: ");
        }
    }
    public Double decimal(){


            return Double.parseDouble(cadena());

            }




    public void cerrar(){

        teclado.close();

    }

}

