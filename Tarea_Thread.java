import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Tarea_Thread {
    private static JFrame WDW1 = new JFrame("Program 1");
    private static JFrame WDW2 = new JFrame("Program 2");
    private static JFrame WDW3 = new JFrame("Program 3");
    private static JScrollPane SP1;
    private static JScrollPane SP2;
    private static JScrollPane SP3;
    private static JTextArea   TA1  = new JTextArea("");
    private static JTextArea   TA2  = new JTextArea("");
    private static JTextArea   TA3  = new JTextArea("");
    private static Font fntLABEL = new Font("Arial",Font.BOLD,24);
    private static Font fntTEXT  = new Font("Lucida Console",Font.BOLD,18);
    private static JLabel LBL1Start  = new javax.swing.JLabel();
    private static JLabel LBL1Finish = new javax.swing.JLabel();
    private static JLabel LBL2Start  = new javax.swing.JLabel();
    private static JLabel LBL2Finish = new javax.swing.JLabel();
    private static JLabel LBL3Start  = new javax.swing.JLabel();
    private static JLabel LBL3Finish = new javax.swing.JLabel();
    private static final int N = 150000;

    private static long [] V1 = new long[N];
    private static int[] V2 = new int[N];

    private static final int M = 30000;

    private static int[] V3P = new int[M];

    private static int[] V3B = new int[M];

    private static int[] ESTADO = new int[3 + 1];

    public static void ConfigurarControles(JFrame WDW,
                                           int WW,
                                           int HH,
                                           int LEFT,
                                           int TOP,
                                           JScrollPane SP,
                                           JTextArea   TA,
                                           JLabel      LBLStart,
                                           JLabel      LBLFinish
    ){
        WDW.setSize(WW, HH);
        WDW.setLocation(LEFT,TOP);
        WDW.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        WDW.setVisible(true);

        LBLStart.setBounds(25, 20, 300, 40);
        LBLFinish.setBounds(5, 10, 300, 40);

        LBLStart.setFont(fntLABEL);
        LBLFinish.setFont(fntLABEL);

        //LBLStart.setBounds(spLeft,10,lblWidth,lblHeight);
        //LBLFinish.setBounds(spLeft,10+spHeight+10,lblWidth,lblHeight);

        TA.setEditable(false);
        TA.setBounds(25,60,300,500);
        TA.setBackground(Color.WHITE);
        TA.setFont(fntTEXT);
        TA.setForeground(Color.GREEN);
        TA.setBackground(Color.BLACK);

        SP = new JScrollPane(TA);
        SP.setBounds(25,50,300,300);

        WDW.add(LBLStart);
        WDW.add(SP);
        WDW.add(LBLFinish);
        WDW.setVisible(true);
    }
    private static void LoadVector() {

        ESTADO[1] = 0; //Libre
        ESTADO[2] = 0;
        ESTADO[3] = 0;

        Random r = new Random();
        for(int i = 0; i<N;i++){
            V1[i] = 0;
            V2[i] = r.nextInt(10000);
        }

        for(int i=0;i<M;i++){
            V3B[i] = r.nextInt(10000);
            V3P[i] = r.nextInt(10000);
        }
    }

    public static void main(String[] args) {
        AtomicInteger AI1 = new AtomicInteger(1);
        AtomicInteger AI2 = new AtomicInteger(1);
        AtomicInteger AI3 = new AtomicInteger(1);
        LoadVector();
        ConfigurarControles(WDW1,500,800,100,40,SP1,TA1,LBL1Start,LBL1Finish);
        ConfigurarControles(WDW2,500,800,500,40,SP2,TA2,LBL2Start,LBL2Finish);
        ConfigurarControles(WDW3,500,800,700,40,SP3,TA3,LBL3Start,LBL3Finish);
        new Thread(new Runnable() {
            @Override
            public void run() {
                long inicio = System.currentTimeMillis();
                LBL1Start.setText("Time Execution: " + inicio / 1000 + " segundos");
                System.out.println("Iniciando el proceso");
                ImprimirSecuenciaFibonacci(N);
                AI1.set(0);
                long fin = System.currentTimeMillis() - inicio;
                LBL1Finish.setText("Time Execution: " + fin / 1000 + " segundos");
                System.out.println("\n");
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                long inicio = System.currentTimeMillis();
                LBL2Start.setText("Time Execution: " + inicio / 1000 + " segundos");
                System.out.println("Iniciando el proceso");
                radixSort(V2,N);
                AI2.set(0);
                long fin = System.currentTimeMillis() - inicio;
                LBL2Finish.setText("Time Execution: " + fin / 1000 + " segundos");
                System.out.println("\n");
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                long inicio = System.currentTimeMillis();
                LBL3Start.setText("Time Execution: " + inicio / 1000 + " segundos");
                System.out.println("Iniciando el proceso");
                int Peso = 15000;
                TA3.append("El peso optimo es: " + mochila(V3B,V3P,Peso));
                AI3.set(0);
                long fin = System.currentTimeMillis() - inicio;
                LBL3Finish.setText("Time Execution: " + fin / 1000 + " segundos");
                System.out.println("\n");
            }
        }).start();
    }

    public static long FibR(int n){
        V1[0] = 1;
        V1[1] = 1;
        for(int i = 2;i <= n;i++){
            V1[i] = V1[i-1] + V1[i-2];
        }
        return V1[n];
    }

    public static void ImprimirSecuenciaFibonacci(int N) {
        for(int i=0;i<=N-1;i++) {
            TA1.append("n"+i+":"+FibR(i)+"\n");
        }
    }

    public static int getMax(int V[], int n){
        int maximo = V[0];
        for (int i = 1; i < n; i++)
            if (V[i] > maximo)
                maximo = V[i];
        return maximo;
    }

    public static void countSort(int V[], int n, int exp)
    {
        int output[] = new int[n];
        int i;
        int count[] = new int[10];
        Arrays.fill(count, 0);

        for (i = 0; i < n; i++)
            count[(V[i] / exp) % 10]++;

        for (i = 1; i < 10; i++)
            count[i] += count[i - 1];

        for (i = n - 1; i >= 0; i--) {
            output[count[(V[i] / exp) % 10] - 1] = V[i];
            count[(V[i] / exp) % 10]--;
        }

        for (i = 0; i < n; i++)
            V[i] = output[i];
    }

    public static void radixSort(int V[],int N){
        int max = getMax(V, N);

        for (int exp = 1; max / exp > 0; exp *= 10) {
            imprimirArray(V, N);
            countSort(V, N, exp);
        }
        imprimirArray(V,N);
    }

    public static void imprimirArray(int V[],int n){
        TA2.append("Array empezando un ciclo\n");
        for(int i=0;i<n;i++){
            TA2.append(V[i]+"\n");
        }
    }
    public static int mochila(int[] v, int[] w, int W)
    {

        int[][] T = new int[v.length + 1][W + 1];

        for (int i = 1; i <= v.length; i++)
        {
            for (int j = 0; j <= W; j++)
            {
                if (w[i-1] > j) {
                    T[i][j] = T[i-1][j];
                }
                else {
                    T[i][j] = Integer.max(T[i-1][j], T[i-1][j-w[i-1]] + v[i-1]);
                }
            }
        }
        // El valor maximo
        return T[v.length][W];
    }

}
