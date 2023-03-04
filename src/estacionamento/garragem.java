package estacionamento;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class garragem {

    private static String logSaidas = "";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        escrever("Digita a capacidade do estacionamento");
        int capacidadeEstacionamento = sc.nextInt();

        String[] placasVeiculos = new String[capacidadeEstacionamento];
        LocalDateTime[] temposEstacionamento = new LocalDateTime[capacidadeEstacionamento];

        int opcoes = 0;
        do {
            escrever("Opções");
            escrever("[1] Solicitar Placa");
            escrever("[2] Sair do Sistema");
            opcoes = sc.nextInt();

            switch (opcoes){
                case 1:
                    escrever("Digita a placa");
                    String placa = sc.next();
                    programa(placa,placasVeiculos,temposEstacionamento);
                    break;
                case 2:
                    escrever("Obrigado! Volte Sempre!");
                    break;
                default:
                    escrever("Opção Invalida");
            }

        }while (opcoes !=2);
    }

    private static void programa(String placa, String[] placasVeiculos, LocalDateTime[] temposEstacionamento) {
        int vagas = verificaPlaca(placa,placasVeiculos);

        if (vagas == -1){
            entradaDeVeiculo(placa,placasVeiculos,temposEstacionamento);
        }else {
            SaidaDoVeiculo(vagas,placasVeiculos,temposEstacionamento);
        }

        imprimirRelatorio(placasVeiculos,temposEstacionamento);
    }

    private static void imprimirRelatorio(String[] placasVeiculos, LocalDateTime[] temposEstacionamento) {

        escrever("============================================================================================================");
        escrever("Veículos que esta no Estacionamento");

        for (int i = 0; i < placasVeiculos.length; i++) {
            String placa = placasVeiculos[i];

            if (placa == null) {
                continue;
            }

            LocalDateTime dataEhoraEEntrada = temposEstacionamento[i];
            String dataFormatada = dataEhoraEEntrada.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            System.out.printf("\tPlaca %s \t Horas da entrada: %s %n",placa,dataFormatada);
        }

        escrever("");
        escrever("Relatorio de Saidas de Veículos: ");
        System.out.printf("%s %n", logSaidas);
    }

    private static void SaidaDoVeiculo(int vagas, String[] placasVeiculos, LocalDateTime[] temposEstacionamento) {
        String placa = placasVeiculos[vagas];
        LocalDateTime entrada = temposEstacionamento[vagas];
        LocalDateTime saida = LocalDateTime.now();
        long minutos = Duration.between(entrada,saida).toMinutes();
        double valorAPagar = calcularValorParaPagar(minutos);

        System.out.printf("Saída do veículo plca %s. Tempo que ficou no estacinamento %s minutos. valor a ser cobrado e: %.2f %n",
                placa, minutos, valorAPagar);

        String historico = String.format("\tplaca %s - tempo de permanêcia no estacionamento: %d minutos - valor cobrado: %.2f %n",
                placa, minutos, valorAPagar);

        logSaidas += historico;

        placasVeiculos[vagas] = null;
        temposEstacionamento[vagas] = null;
    }

    private static double calcularValorParaPagar(long minutos) {
//   • De 0 a 5 minutos. Sem cobrança de valor
//   • De 5 a 60 minutos. R$ 4,00
//   • Acima de 60 minutos é cobrado um valor de R$ 6,00 por hora adicional.
//   • Se ficou 1 hora e 1 minuto paga 4 pela primeira hora mais 6 pela hora adicional
        if (minutos <= 5) {
            return 0.0;
        }else if (minutos > 5 && minutos <= 60) {
            return 4.0;
        }else {
            double valorParaPagar = 4.0;

//          Divide o tempo em minutos para saber o valor em horas
            double horas = (minutos/60.0);

//          Decrementa a primeira hora que tem o valor fixo de 4 reais
            horas--;

//          arredondar o valor para cima
            horas = Math.ceil(horas);

//          Multiplica o valor pelo número de horas adicionais
            double valorAdicional =(horas * 6.00);

//          Soma o valor adicionao ao valo da hora inicial
            valorParaPagar = valorParaPagar + valorAdicional;

            return valorParaPagar;
        }
    }

    private static void entradaDeVeiculo(String placa, String[] placasVeiculos, LocalDateTime[] temposEstacionamento) {
        int vagLivre = vagaLivre(placasVeiculos);
        if (vagLivre == 1) {
            escrever("Não há vagas!!");
        }else {
            LocalDateTime horaDaEntrada = LocalDateTime.now();
            placasVeiculos[vagLivre] = placa;
            temposEstacionamento[vagLivre] = horaDaEntrada;
            System.out.printf("Entrada do veículo da placa: %s realizada.%n",placa);
        }
    }

    private static int vagaLivre(String[] placasVeiculos) {
        for (int i = 0; i < placasVeiculos.length; i++) {
            String valor = placasVeiculos[i];
            if (valor == null) {
                return i;
            }
        }
        return -1;
    }

    private static int verificaPlaca(String placa, String[] placasVeiculos) {
        int vagas = -1;
        for (int i = 0; i < placasVeiculos.length; i++) {
            String valor = placasVeiculos[i];
            if (valor != null){
                if (valor.equals(placa)){
                    vagas = i;
                }
            }
        }
        return vagas;
    }


    public static void escrever(String menssagem) {
        System.out.println(menssagem);
    }

}
