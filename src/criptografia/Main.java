/*
* -------------------------------------------------------------------------------
* CRIPTOGRAFIA EM JAVA - AUTOR - YUGI TUMRO
* -------------------------------------------------------------------------------
* PRODUZ ARQUIVOS RANDOMICOS A PARTIR DE QUALQUER TIPO DE ARQUIVO
* SUPORTA CHAVES NO FORMATO STRING COM QUALQUER TAMANHO
* GERA ARQUIVOS ALEATORIOS A CADA CIFRAGEM MESMO UTILIZANDO-SE A MESMA CHAVE DE CIFRA
* PERMITE FAZER SUPERCIFRAGEM, OU SEJA, CRIPTOGRAFAR O ARQUIVO COM DUAS OU MAIS CHAVES.
*  */

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

public class Main {
    
public static void main(String[] args) {

  DataHora();
  System.out.println("INÍCIO...");

  // EXEMPLO DE 2 CIFRAGENS SIMPLES: -----------------------------------------------------------

  CriarArquivo("C:/Pessoal/a.txt",1024*100); // Criar arquivo de 1 KB com 'a'
  System.out.println("ARQUIVO TESTE CRIADO!");

  // CODIFICAÇÃO 1: ----------------------------------------------------------------------

  int ini = Hora();
   Cript("C:/Pessoal/a.txt","C:/Pessoal/b1.txt","abacate",'C'); // Codificar
  int fim = Hora();
  System.out.println("ARQUIVO TESTE CRIPTOGRAFADO! TEMPO DE " + (fim - ini) + " Segundos!");
  ini = Hora();
  Cript("C:/Pessoal/b1.txt","C:/Pessoal/c1.txt","abacate",'D'); // Decodificar
  fim = Hora();
  System.out.println("ARQUIVO TESTE DECODIFICADO TEMPO DE " + (fim - ini) + " Segundos!");
  // CODIFICAÇÃO 2: ----------------------------------------------------------------------

  /*
  ini = Hora();
  Cript("C:/Pessoal/a.txt","C:/Pessoal/b2.txt","abacate",'C'); // Codificar
  fim = Hora();
  System.out.println("ARQUIVO TESTE CRIPTOGRAFADO! TEMPO DE " + (fim - ini) + " Segundos!");

  ini = Hora();
  Cript("C:/Pessoal/b2.txt","C:/Pessoal/c2.txt","abacate",'D'); // Decodificar
  fim = Hora();
  System.out.println("ARQUIVO TESTE DECODIFICADO TEMPO DE " + (fim - ini) + " Segundos!");

  // EXEMPLO DE SUPERCIFRAGEM: -----------------------------------------------------------

  ini = Hora();
  Cript("C:/Pessoal/a.txt","C:/Pessoal/t1.txt","coiote",'C'); // Codificar
  Cript("C:/Pessoal/t1.txt","C:/Pessoal/t2.txt","aguia do sertão 1473",'C'); // Codificar
  Cript("C:/Pessoal/t2.txt","C:/Pessoal/t3.txt","aguia do sertão 1473",'D'); // decodificar
  Cript("C:/Pessoal/t3.txt","C:/Pessoal/t4.txt","coiote",'D'); // decodificar
  fim = Hora();
  System.out.println("ARQUIVO TESTE DECODIFICADO TEMPO DE " + (fim - ini) + " Segundos!");
*/
  System.out.println("FIM!!!");


}

// --------------------------------------------------------------------------------------------------------------------
  public static int[] RamdomKey() { // retorna um vetor randomico...
   int[] ret = new int[32];
   int valor;

   int cont = 0;

   for (int ct =  0;ct<32;ct++) {
        ret[ct] = 0;
   }

   while (true) {
        valor = (int) (Math.random()*256);
        ret[cont%32] = (ret[cont%32] + valor)%256;
        cont++;

        if (cont == 3200) {
         break;
        }
   }

   return ret;
  }
// --------------------------------------------------------------------------------------------------------------------
public static int Hora() {
   Calendar calendar = Calendar.getInstance();

   int h = calendar.get(Calendar.HOUR_OF_DAY);
   String hora = String.valueOf(h);
   if (hora.length() == 1)
         hora = "0" + hora;

   int m = calendar.get(Calendar.MINUTE);
   String minuto = String.valueOf(m);
   if (minuto.length() == 1)
         minuto = "0" + minuto;

   int s = calendar.get(Calendar.SECOND);
   String sec = String.valueOf(s);
   if (sec.length() == 1)
         sec = "0" + sec;

          return (h * 3600) + (m * 60) + s;
}

// --------------------------------------------------------------------------------------------------------------------
public static void DataHora() {
   String[] diasSemana = {"Domingo", "Segunda-Feira", "Terça-Feira",
                                 "Quarta-Feira", "Quinta-Feira", "Sexta-Feira", "Sábado"};

          String[] meses = {"Janeiro","Fevereiro","Março","Abril","Maio","Junho",
                                 "Julho","Agosto","Setembro","Outubro","Novembro","Dezembro"};

          Calendar calendar = Calendar.getInstance();

          String str = "Bom dia! " + diasSemana[calendar.get(Calendar.DAY_OF_WEEK)-1]
                                 + ", " + calendar.get(Calendar.DAY_OF_MONTH) + " de "
                                 + meses[calendar.get(Calendar.MONTH)] + " de "
                                 + calendar.get(Calendar.YEAR) + " - "
                                 + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
          System.out.println(str);
}


// --------------------------------------------------------------------------------------------------------------------
  public static void CriarArquivo(String destino,int tam){
          try{

                 File fileDestiny = new File(destino);
                 FileOutputStream o = new FileOutputStream(fileDestiny);
                 BufferedOutputStream out = new BufferedOutputStream(o);

                 for (int ct=0; ct<tam; ct++) {
                  out.write('a');
                 }

                 out.close();

          }catch(Exception e){
                 System.out.println("Erro = "+e);
          }
   }


// --------------------------------------------------------------------------------------------------------------------
public static void Cript(String origem, String destino, String Chave,char Operacao){
         try{
                File fileOrigin = new File(origem);
                File fileDestiny = new File(destino);
                FileInputStream i = new FileInputStream(fileOrigin);
                FileOutputStream o = new FileOutputStream(fileDestiny);
                BufferedInputStream in = new BufferedInputStream(i);
                BufferedOutputStream out = new BufferedOutputStream(o);

                int [] ch = new int[32];
                ch = Transform( stringHexa(gerarHash(Chave, "SHA-256")));

                int cont = 0;
                String tmp = "";
                int op = 1;

                if (Operacao == 'C' || Operacao == 'c')
                op = 1;
                else
                 op = -1;

                int x;
                int[] valores = new int[32];
                String xValores = "";

                if (op == 1) { // CIFRAGEM
                  valores = RamdomKey();
                  xValores = ConverteParaString(valores);

                  for (int pag=0;pag<32;pag++) {
                   x = (ch[pag] + valores[pag]);
                   if (x > 255) {
                         x = x - 256;
                   }
                   if (x < 0) {
                        x = x + 256;
                }

                   out.write(x);
                  }
                } else { // DECIFRAGEM
                 for (int pag=0;pag<32;pag++) {
                        x = in.read();
                 x = x - ch[pag];

                   if (x > 255) {
                         x = x - 256;
                   }
                   if (x < 0) {
                        x = x + 256;
                }

                   valores[pag] = x;
                  }
                   xValores = ConverteParaString(valores);
                }

                //  Calcular nova sequencia da Chave!!!
           cont = 0;
           tmp = Chave;
           Chave = ConverteParaString(ch);
           ch = Transform( stringHexa(gerarHash(Chave+tmp, "SHA-256")));

           tmp = xValores;
           xValores = ConverteParaString(valores);
           valores = Transform( stringHexa(gerarHash(xValores+tmp, "SHA-256")));

          if (op == 1) {
                   // Cifragem do arquivo...
                   while((x = in.read()) != -1){

                        x = (x + ch[cont] + valores[cont])%256;
                        ++ cont;

                          out.write(x);

                        //  Calcular nova sequencia da Chave!!!
                          if (cont > 31) {
                         cont = 0;
                         tmp = Chave;
                         Chave = ConverteParaString(ch);
                         ch = Transform( stringHexa(gerarHash(Chave+tmp, "SHA-256")));

                         tmp = xValores;
                         xValores = ConverteParaString(valores);
                         valores = Transform( stringHexa(gerarHash(xValores+tmp, "SHA-256")));
                   }
                   }
          } else {

                 // Decifragem do arquivo...
                   while((x = in.read()) != -1){

                        x = x - ((ch[cont] + valores[cont])%256);
                        ++ cont;

                        if (x < 0) {
                          x = x + 256;
                        }

                          out.write(x);

                        //  Calcular nova sequencia da Chave!!!
                          if (cont > 31) {
                         cont = 0;
                         tmp = Chave;
                         Chave = ConverteParaString(ch);
                         ch = Transform( stringHexa(gerarHash(Chave+tmp, "SHA-256")));

                         tmp = xValores;
                         xValores = ConverteParaString(valores);
                         valores = Transform( stringHexa(gerarHash(xValores+tmp, "SHA-256")));
                   }
                   }

          }

                in.close();
                out.close();

         }catch(Exception e){
                System.out.println("Erro = "+e);
         }
  }

// ---------------------------------------------------------------------------------------------------------
        public static byte[] gerarHash(String frase, String algoritmo) {
         try {
           MessageDigest md = MessageDigest.getInstance(algoritmo);
           md.update(frase.getBytes());
           return md.digest();
         } catch (NoSuchAlgorithmException e) {
           return null;
         }
   }

// ---------------------------------------------------------------------------------------------------------

        private static String stringHexa(byte[] bytes) {
         StringBuilder s = new StringBuilder();
         for (int i = 0; i < bytes.length; i++) {
                 int parteAlta = ((bytes[i] >> 4) & 0xf) << 4;
                 int parteBaixa = bytes[i] & 0xf;
                 if (parteAlta == 0) s.append('0');
                 s.append(Integer.toHexString(parteAlta | parteBaixa));
         }
         if (s.toString().length() != 64) {
          System.out.println("erro com SHA-256" );
          System.exit(0);

         }


         return s.toString();
  }

//===============================================================================================================


   public static int[] Transform(String entrada) {

        int[] ret = new int[32];
        int tam = entrada.length() - 1;
        int cont = 0;

        for (int ct=0; ct < tam; ct=ct+2){

         int valor = 0, valor2 = 0;

         char r;
         int num, ct2;
         char[] car = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};

         r = entrada.charAt(ct);
         num = 0;
         ct2 = 0;
         while (ct2 < 16) {
          num = (r == car[ct2]) ? ct2 * 16 : 0;
          valor = valor + num;
          ++ct2;
         }

         r = entrada.charAt(ct+1);
         num = 0;
         ct2 = 0;
         while (ct2 < 16) {
          num = (r == car[ct2]) ? ct2 : 0;
          valor2 = valor2 + num;
          ++ct2;
         }

         ret[cont] = valor + valor2;
         ++ cont;
        }


        return ret;
   }

// ---------------------------------------------------------------------------------------------------------------------

   public static String ConverteParaString(int[] entrada) {
        String ret = "";

        int tam = entrada.length;

        for (int ct=0;ct<tam;ct++) {
         ret = ret + (char)entrada[ct];
        }
        if (ret.length() != 32) {
         System.out.println("erro" );
         System.exit(0);
        }

        return ret;
   }
}